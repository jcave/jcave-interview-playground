package com.onxmaps.playground.calculator.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onxmaps.playground.calculator.dao.CalculationDao
import com.onxmaps.playground.calculator.models.Calculation

@Database(entities = [Calculation::class], version = 1, exportSchema = false)
abstract class PlaygroundDb() : RoomDatabase() {

    abstract fun calculationDao(): CalculationDao

    companion object {
        @Volatile
        private var INSTANCE: PlaygroundDb? = null

        fun getDatabase(context: Context): PlaygroundDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaygroundDb::class.java,
                    "playground_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}