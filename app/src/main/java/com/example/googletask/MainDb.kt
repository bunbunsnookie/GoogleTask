package com.example.googletask

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (
    entities = [Task::class],
    version = 1,
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
        fun getDb(context: Context): MainDb{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "database1.db"
            ).build()
        }
    }
}
