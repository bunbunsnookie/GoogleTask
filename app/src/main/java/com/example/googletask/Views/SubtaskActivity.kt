package com.example.googletask.Views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.googletask.databinding.ActivitySubtaskBinding

class SubtaskActivity : AppCompatActivity() {
    lateinit var binding: ActivitySubtaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubtaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}