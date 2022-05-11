package com.yilmaz.homesmartclock.data.remote

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.yilmaz.homesmartclock.constants.MqttConstants
import com.yilmaz.homesmartclock.enum.MqttConnectionState
import com.yilmaz.homesmartclock.service.remote.Mqtt
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MqttRepository {
    private var mConnectStatus = MutableLiveData<Byte>()
    private var mClient: MqttAndroidClient? = null
    private var mOptions: MqttConnectOptions? = null

    companion object{
        private val TAG:String = MqttRepository::class.java.name
    }

    init {

    }

    @Suppress("RedundantSuspendModifier")
    @SuppressLint("WrongThread")
    @WorkerThread
    suspend
    fun initBroker(context: Context){
        Log.d(TAG, "initBroker: ")
        val mqtt = Mqtt(context)
        mClient = mqtt.getClient()
        mOptions = mqtt.getOptions()
        connectToBroker()
    }

    @Suppress("RedundantSuspendModifier")
    @SuppressLint("WrongThread")
    @WorkerThread
    suspend
    fun connectToBroker(){
        try {
            val token = mClient?.connect(mOptions)
            token?.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.d(TAG, "connectToBroker: success")
                    mConnectStatus.value = MqttConnectionState.CONNECTED.id
                    setMessageArrivedCallback()
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.d(TAG, "connectToBroker: failure")
                    mConnectStatus.value = MqttConnectionState.ERROR.id
                }
            }
        } catch (e: MqttException) {
            val reason:Int = e.reasonCode
            Log.d(TAG, "An error occurred: ${e.message} reason code: $reason")
            if (reason == (MqttException.REASON_CODE_CONNECTION_LOST).toInt() ||
                reason == MqttException.REASON_CODE_SERVER_CONNECT_ERROR.toInt()){
                mConnectStatus.value = MqttConnectionState.DISCONNECTED.id
            }
        }
    }

    @Suppress("RedundantSuspendModifier")
    @SuppressLint("WrongThread")
    @WorkerThread
    suspend
    fun publish(message: String, clockId: String) {
        try {
            mClient?.publish(
                "${MqttConstants.topicTitle}/${clockId}/${MqttConstants.publishMessage}",
                message.toByteArray(),
                MqttConstants.defaultQos,
                false,
                null,
                object : IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(TAG, "onSuccess: ${asyncActionToken?.topics}")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(TAG, "onFailure:  ${asyncActionToken?.topics} exception: ${exception?.message}")
                    }
                })
        } catch (e: MqttException) {
            System.err.println("Error Publishing: " + e.message)
            e.printStackTrace()
        }
    }

    @Suppress("RedundantSuspendModifier")
    @SuppressLint("WrongThread")
    @WorkerThread
    suspend fun subscribe(topics: String) {
        try {
            mClient?.subscribe(
                topics,
                1,
                null,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken) {
                        Log.d(TAG, "subscript topic onSuccess: ")
                    }
                    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                        Log.d(TAG, "subscript topic onFailure: ")
                    }
                })

        } catch (ex: MqttException) {
            System.err.println("Exception whilst subscribing")
            ex.printStackTrace()
        }
    }

    @Suppress("RedundantSuspendModifier")
    @SuppressLint("WrongThread")
    @WorkerThread
    suspend
    fun unSubscribe(topic:String){
        mClient?.unsubscribe(topic, null, object : IMqttActionListener{
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                Log.d(TAG, "unsubscript topic onSuccess: ")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                Log.d(TAG, "unsubscript topic onFailure: ")
            }
        })
    }


    private fun setMessageArrivedCallback(){
        mClient?.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                Log.d(TAG, "connectionLost: ")
                mConnectStatus.value = MqttConnectionState.DISCONNECTED.id
            }

            @Throws(Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                val payload = String(message.payload)
                Log.d(TAG, "messageArrived: id: ${message}")
                if(payload.isEmpty()){
                    return
                }
                //parseMqttMessage(payload)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                Log.d(TAG, "deliveryComplete: ${token.message}")
            }
        })
    }

    @Suppress("RedundantSuspendModifier")
    @SuppressLint("WrongThread")
    @WorkerThread
    suspend
    fun disconnectMqtt(){
        try {
            mClient?.setCallback(null)
            mClient?.close()
            //mClient?.disconnectForcibly()
            mClient?.disconnect(100,null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "MQTT Disconnection success")
                }
                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to disconnect")
                }
            })

        }catch (e: MqttException){
            Log.d(TAG, "Error Publishing: " + e.message)
            e.printStackTrace()
        }

    }


    val connectStatus: MutableLiveData<Byte> = mConnectStatus
}
