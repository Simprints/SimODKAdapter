package com.simprints.simodkadapter.extensions

import androidx.lifecycle.MutableLiveData
import com.simprints.simodkadapter.events.DataEvent


fun <T> MutableLiveData<DataEvent<T>>.set(content: T) {
    this.value = DataEvent(content)
}

fun <T> MutableLiveData<DataEvent<T>>.post(content: T) {
    this.postValue(DataEvent(content))
}