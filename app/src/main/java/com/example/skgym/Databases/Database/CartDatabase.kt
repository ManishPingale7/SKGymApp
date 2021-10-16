package com.example.skgym.Databases.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skgym.Databases.Dao.CartDao
import com.example.skgym.data.Cart

@Database(entities = [Cart::class], version = 0)
abstract class CartDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: CartDatabase? = null

        fun getDatabase(context: Context): CartDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CartDatabase::class.java,
                    "CartDatabase"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}