package com.afedaxo.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DishEntity::class, QuessingSession::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract val quessingSessionDao: QuessingSessionDao
}