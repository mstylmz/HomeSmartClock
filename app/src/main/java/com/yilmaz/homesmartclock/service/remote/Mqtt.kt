package com.yilmaz.homesmartclock.service.remote

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import com.yilmaz.homesmartclock.constants.MqttConstants
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class Mqtt(context: Context){
    private var mClient: MqttAndroidClient? = null
    private var mOptions: MqttConnectOptions? = null

    companion object{
        private val TAG = Mqtt::class.java.simpleName
    }

    init {
        if(mClient == null){
            mClient = MqttAndroidClient(context,
                MqttConstants.broker + ":" + MqttConstants.port,
                MqttConstants.clientId,
                null,
                MqttAndroidClient.Ack.MANUAL_ACK)
            Log.d(TAG, "init mClient")

        }

        if(mOptions == null){
            mOptions = MqttConnectOptions()
            mOptions?.mqttVersion = MqttConstants.version
            mOptions?.isAutomaticReconnect = MqttConstants.automaticReconnect
            mOptions?.connectionTimeout = MqttConstants.connectionTimeout
            mOptions?.isCleanSession = MqttConstants.cleanSession
            mOptions?.maxInflight = MqttConstants.inFlight
            mOptions?.userName = MqttConstants.name
            mOptions?.password = MqttConstants.password.toCharArray()
            Log.d(TAG, "init mOptions")
        }
    }

    fun getClient(): MqttAndroidClient{
        Log.d(TAG, "getClient")
        return mClient as MqttAndroidClient
    }

    fun getOptions(): MqttConnectOptions{
        Log.d(TAG, "getOptions")
        return mOptions as MqttConnectOptions
    }
}