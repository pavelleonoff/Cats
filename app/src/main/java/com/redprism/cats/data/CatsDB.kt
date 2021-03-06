package com.redprism.cats.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Cat::class], version = 1, exportSchema = false)
abstract class CatsDB : RoomDatabase() {
    companion object {
        private const val DB_NAME = "cats.db"
        private var catsDB : CatsDB? = null
        private val LOOK:Any = Any()
        fun getInstance(context: Context):CatsDB{
            synchronized(LOOK) {
                if (catsDB == null) {
                        catsDB = Room.databaseBuilder(context, CatsDB::class.java, DB_NAME)
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return catsDB as CatsDB
        }
    }

    abstract fun catsDao(): CatsDao
}