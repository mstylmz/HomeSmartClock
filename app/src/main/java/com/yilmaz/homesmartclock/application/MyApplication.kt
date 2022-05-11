package com.yilmaz.homesmartclock.application

import android.app.Application
import android.util.Log
import com.yilmaz.homesmartclock.data.local.ClockRepository
import com.yilmaz.homesmartclock.data.remote.MqttRepository
import com.yilmaz.homesmartclock.service.local.ClockRoomDatabase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class MyApplication: Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    private val clockDatabase by lazy { ClockRoomDatabase.getDatabase(this, applicationScope) }

    val clockRepository by lazy { ClockRepository(clockDatabase.clockDao()) }

    val mqttRepository by lazy { MqttRepository() }

}