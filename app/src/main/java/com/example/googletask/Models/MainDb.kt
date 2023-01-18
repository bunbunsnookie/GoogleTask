package com.example.googletask.Models

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (
    entities = [Task::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
       AutoMigration(
           from = 1,
           to = 2,
        )
    ]
)
abstract class MainDb : RoomDatabase() {

    abstract fun getDao(): Dao

    companion object{

        @Volatile
        private lateinit var instance: MainDb

        fun getInstance(context: Context): MainDb{
            synchronized(this){
                if (!::instance.isInitialized){
                    instance = createDatabase(context)
                }
                return instance
            }
        }

        private fun createDatabase(context: Context): MainDb {
            return Room.databaseBuilder(
                context,
                MainDb::class.java,
                "TasksDatabase"
            ).build()
        }
    }
}
