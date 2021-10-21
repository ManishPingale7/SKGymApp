package com.example.skgym.Room.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skgym.Room.Dao.PlansDbDao
import com.example.skgym.data.PlansDB

@Database(entities = [PlansDB::class], version = 1)
abstract class PlansHisDatabase : RoomDatabase() {
    abstract fun plansDao(): PlansDbDao

    companion object {
        @Volatile
        private var INSTANCE: PlansHisDatabase? = null

        fun getPlansDatabase(context: Context): PlansHisDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlansHisDatabase::class.java,
                    "PlansHisDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}