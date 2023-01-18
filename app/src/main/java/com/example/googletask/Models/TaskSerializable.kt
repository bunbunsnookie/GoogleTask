package com.example.googletask.Models

import java.io.Serializable

data class TaskSerializer(
    var title: String,
    var desc: String,
    var date: String,
    var chosen: Boolean,
    var completed: Boolean,
    var fromTask: Int? = null,
    val id: Int? = null
) : Serializable

{

    companion object {
        fun toTask(src: TaskSerializer): Task {
            return Task(
                src.title,
                src.desc,
                src.date,
                src.chosen,
                src.completed,
                src.fromTask,
                src.id
            )
        }

        fun fromTask(src: Task): TaskSerializer {
            return TaskSerializer(
                src.title,
                src.description,
                src.data,
                src.chosen,
                src.completed,
                src.fromTask,
                src.id
            )
        }
    }
}