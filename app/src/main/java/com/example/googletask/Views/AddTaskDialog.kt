package com.example.googletask.Views

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.googletask.R
import com.example.googletask.databinding.FragmentAddTaskBinding
import java.text.DateFormat
import java.util.*

class AddTaskDialog : AppCompatDialogFragment() {
    private lateinit var dialogInterface: CreateTaskDialogInterface
    private var _binding: FragmentAddTaskBinding? = null
    private val binding get() = _binding!!
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAddTaskBinding.inflate(LayoutInflater.from(context))

        binding.apply {
            val builder = AlertDialog.Builder(requireActivity())

            builder.setView(root)
                .setTitle(R.string.new_task)
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.add) { dialog, _ ->
                    val title = edTaskName.text.toString()
                    val desc = edTaskDescription.text.toString()
                    val date = edDate.text.toString()
                    dialogInterface.addTask(title, desc, date)
                    dialog.dismiss()
                }

            val dialog = builder.create()

            edTaskName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !s.isNullOrEmpty()
                }
            })

            edSetDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)


                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, yearPicked, monthOfYear, dayOfMonth ->
                        calendar.set(Calendar.YEAR, yearPicked)
                        calendar.set(Calendar.MONTH, monthOfYear)
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val date = DateFormat.getDateInstance().format(calendar.time)
                        edDate.text = date
                    },
                    year,
                    month,
                    day
                )

                datePickerDialog.show()
            }

            dialog.setOnShowListener {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
            }

            return dialog
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogInterface = context as CreateTaskDialogInterface
    }

    interface CreateTaskDialogInterface {
        fun addTask(title: String, desc: String, date: String, chosen: Boolean = false, completed: Boolean = false, fromTask : Int? = null)
    }

}