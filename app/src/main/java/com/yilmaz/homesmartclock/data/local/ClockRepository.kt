package com.yilmaz.homesmartclock.data.local

import android.util.Log
import com.yilmaz.homesmartclock.model.Clock
import com.yilmaz.homesmartclock.service.local.ClockDao
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class ClockRepository(private val clockDao: ClockDao) {

    private val TAG = ClockRepository::class.java.simpleName

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(clock: Clock){
        val result = clockDao.insert(clock)
        Log.d(TAG, "insert result: $result")
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(clock: Clock){
        clockDao.delete(clock)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll(){
        clockDao.deleteAll()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateClocks(clock: Clock){
        //clockDao.update(clock)
    }

    val allClocks:Flow<List<Clock>> = clockDao.getAllClocks()

}