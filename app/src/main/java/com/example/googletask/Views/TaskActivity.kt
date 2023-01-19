package com.example.googletask.Views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.googletask.R
import com.example.googletask.databinding.ActivityTaskBinding
import com.example.googletask.Models.TaskSerializer
import com.example.googletask.Models.MainDb
import com.example.googletask.Models.Task
import androidx.lifecycle.Observer
import com.example.googletask.Models.TasksRepository
import com.example.googletask.Models.Const
import com.example.googletask.Models.SubtasksAdapter
import com.example.googletask.ViewModels.MainActivityViewModel
import com.example.googletask.ViewModels.MainActivityViewModelFactory
import java.text.DateFormat
import java.util.*

class TaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var taskSerializable: TaskSerializer

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = MainDb.getInstance(this)
        val repository = TasksRepository(database)
        val viewModelFactory = MainActivityViewModelFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        binding.apply {
            taskSerializable = intent.getSerializableExtra(Const.TASK_EXTRA) as TaskSerializer
            val adapter = SubtasksAdapter(
                viewModel.getAllSubtasks(taskSerializable.id as Int).value?.reversed()
                    ?: listOf(), viewModel
            )
            val layoutManager = LinearLayoutManager(this@TaskActivity)
            val subtasksObserver = Observer<List<Task>> {
                adapter.subtasks = it.reversed()
                adapter.notifyDataSetChanged()
            }
            viewModel.getAllSubtasks(taskSerializable.id as Int)
                .observe(this@TaskActivity, subtasksObserver)
            rvSubtasks.adapter = adapter
            rvSubtasks.layoutManager = layoutManager

            etTitleEdit.setText(taskSerializable.title)
            etDescriptionEdit.setText(taskSerializable.desc)
            tvDate.text = taskSerializable.date

            val color = getCompletedColor(taskSerializable.completed)
            val text = getCompletedText(taskSerializable.completed)
            edComp.setTextColor(color)
            edComp.text = text

            edStar1.setImageResource(
                getStarDrawable(taskSerializable.chosen)
            )

            edBack1.setOnClickListener { onBackPressed() }

            edComp.setOnClickListener {
                taskSerializable.completed = !taskSerializable.completed
                val btnColor = getCompletedColor(taskSerializable.completed)
                val btnText = getCompletedText(taskSerializable.completed)
                val btnCompleted = it as TextView
                btnCompleted.text = btnText
                btnCompleted.setTextColor(btnColor)
                val task = TaskSerializer.toTask(taskSerializable)
                viewModel.update(task)
            }

            edDelete1.setOnClickListener {
                val intent = Intent()
                intent.putExtra(Const.TASK_EXTRA, taskSerializable)
                setResult(Const.EDIT_TASK_RESULT_CODE, intent)
                finish()
            }

            edAdd.setOnClickListener {
                viewModel.insert(Task(taskSerializable.title,"", taskSerializable.date, taskSerializable.chosen, taskSerializable.completed, taskSerializable.id))
            }

            tvDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)


                val dpd = DatePickerDialog(
                    this@TaskActivity,
                    { _, yearPicked, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, yearPicked)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val date = DateFormat.getDateInstance().format(calendar.time)
                        tvDate.text = date
                    },
                    year,
                    month,
                    day
                )

                dpd.show()
            }

            edStar1.setOnClickListener {
                taskSerializable.chosen = !taskSerializable.chosen
                edStar1.setImageResource(
                    getStarDrawable(taskSerializable.chosen)
                )
                val task = TaskSerializer.toTask(taskSerializable)
                viewModel.update(task)
            }
        }
    }

    override fun onBackPressed() {
        binding.apply {
            taskSerializable.title = etTitleEdit.text.toString()
            taskSerializable.desc = etDescriptionEdit.text.toString()
            taskSerializable.date = tvDate.text.toString()
        }
        viewModel.update(TaskSerializer.toTask(taskSerializable))
        super.onBackPressed()
    }

    private fun getCompletedText(isCompleted: Boolean): String {
        return if (isCompleted) resources.getString(R.string.task_is_completed)
        else resources.getString(R.string.task_is_not_completed)
    }

    private fun getCompletedColor(isCompleted: Boolean): Int {
        return if (isCompleted) ContextCompat.getColor(applicationContext, R.color.mint)
        else ContextCompat.getColor(applicationContext, R.color.black)
    }

    fun getStarDrawable(isImportant: Boolean): Int {
        return when(isImportant) {
            true -> R.drawable.ic_star
            false -> R.drawable.ic_star_outline
        }
    }
}