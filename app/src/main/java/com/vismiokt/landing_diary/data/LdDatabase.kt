package com.vismiokt.landing_diary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class LdDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var Instance: LdDatabase? = null

        fun getDatabase(context: Context): LdDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LdDatabase::class.java, "plant_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}