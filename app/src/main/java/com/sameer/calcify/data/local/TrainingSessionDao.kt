package com.sameer.calcify.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrainingSessionDao {
    @Insert
    fun insert(session: TrainingSessionEntity)

    @Query("SELECT * FROM training_sessions ORDER BY completedAt DESC")
    fun getAllSessions(): List<TrainingSessionEntity>
}