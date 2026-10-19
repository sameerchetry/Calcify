package com.sameer.calcify.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.room.Room
import com.sameer.calcify.data.local.CalcifyDatabase
import com.sameer.calcify.data.local.TrainingSessionEntity
import java.util.concurrent.Executors

/**
 * Small offline-first repository. Room work happens off the main thread;
 * callbacks are returned to the UI thread.
 */
class TrainingSessionRepository(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        CalcifyDatabase::class.java,
        "calcify.db"
    ).build()
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    fun getSessions(onLoaded: (List<TrainingSessionEntity>) -> Unit) {
        executor.execute {
            val sessions = database.trainingSessionDao().getAllSessions()
            mainHandler.post { onLoaded(sessions) }
        }
    }

    fun saveSession(session: TrainingSessionEntity, onSaved: () -> Unit = {}) {
        executor.execute {
            database.trainingSessionDao().insert(session)
            mainHandler.post { onSaved() }
        }
    }
}