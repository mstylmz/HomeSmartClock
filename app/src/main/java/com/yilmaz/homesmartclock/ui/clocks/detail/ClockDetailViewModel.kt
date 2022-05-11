package com.yilmaz.homesmartclock.ui.clocks.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.yilmaz.homesmartclock.data.remote.MqttRepository
import kotlinx.coroutines.launch

class ClockDetailViewModel(private val mqttRepository: MqttRepository): ViewModel() {

    val mqttConnectStatus: MutableLiveData<Byte> = mqttRepository.connectStatus

    fun publishMessage(message:String, clockId:String) = viewModelScope.launch {
        mqttRepository.publish(message, clockId)
    }

}

class Factory(private val mqttRepository: MqttRepository) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ClockDetailViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ClockDetailViewModel(mqttRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}