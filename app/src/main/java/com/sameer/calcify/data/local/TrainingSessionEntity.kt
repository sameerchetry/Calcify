package com.sameer.calcify.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_sessions")
data class TrainingSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mode: String,
    val completedAt: Long,
    val score: Int,
    val accuracyPercent: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val skippedCount: Int,
    val totalQuestions: Int,
    val durationSeconds: Int
)