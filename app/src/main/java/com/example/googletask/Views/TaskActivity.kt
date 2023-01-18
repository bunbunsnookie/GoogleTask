package com.example.googletask.Views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.googletask.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}