package com.example.googletask.Views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googletask.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), AddTaskDialog.CreateTaskDialogInterface {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            fab.setOnClickListener{
                AddTaskDialog().show(supportFragmentManager, "Add task")
            }
        }
    }

    override fun addTask(title: String, desc: String, date: String) {
        TODO("Not yet implemented")
    }
}