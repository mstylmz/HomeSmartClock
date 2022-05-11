package com.yilmaz.homesmartclock.ui.clocks.list

import android.content.Context
import androidx.lifecycle.*
import com.yilmaz.homesmartclock.data.local.ClockRepository
import com.yilmaz.homesmartclock.data.remote.MqttRepository
import com.yilmaz.homesmartclock.model.Clock
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.MqttTopic
import java.lang.IllegalArgumentException

class ClockListFragmentViewModel(
    private val mqttRepository: MqttRepository,
    private val clockRepository: ClockRepository
    ) : ViewModel() {

    init {
    }

    fun initBroker(context: Context) = viewModelScope.launch {
        mqttRepository.initBroker(context)
    }

    fun disconnectBroker() = viewModelScope.launch {
        mqttRepository.disconnectMqtt()
    }

    fun subscribe(topic: String) = viewModelScope.launch{
        mqttRepository.subscribe(topic)

    }

    val mqttConnectStatus:MutableLiveData<Byte> = mqttRepository.connectStatus


    //DATABASE
    fun insert(word: Clock) = viewModelScope.launch {
        clockRepository.insert(word)
    }

    val allClocks: LiveData<List<Clock>> = clockRepository.allClocks.asLiveData()
}

class MainViewModelFactory(private val mqttRepository: MqttRepository,
                            private val clockRepository: ClockRepository
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ClockListFragmentViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ClockListFragmentViewModel(mqttRepository, clockRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}

