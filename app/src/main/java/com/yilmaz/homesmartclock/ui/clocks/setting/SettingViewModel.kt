package com.yilmaz.homesmartclock.ui.clocks.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yilmaz.homesmartclock.data.local.ClockRepository
import java.lang.IllegalArgumentException

class SettingViewModel(private val clockRepository: ClockRepository) : ViewModel() {

}

class SettingViewModelFactory(private val clockRepository: ClockRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(clockRepository) as T
        }
        throw IllegalArgumentException("Unkown ViewModel Class")
    }

}