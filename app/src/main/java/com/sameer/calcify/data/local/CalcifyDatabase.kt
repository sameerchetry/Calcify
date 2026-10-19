package com.sameer.calcify.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TrainingSessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CalcifyDatabase : RoomDatabase() {
    abstract fun trainingSessionDao(): TrainingSessionDao
}