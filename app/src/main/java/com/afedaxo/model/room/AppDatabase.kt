package com.afedaxo.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Dish::class, QuessingSession::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract val quessingSessionDao: QuessingSessionDao
}