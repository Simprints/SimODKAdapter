package com.simprints.simodkadapter.extensions

import androidx.lifecycle.MutableLiveData
import com.simprints.simodkadapter.events.Event


fun <T> MutableLiveData<Event<T>>.set(content: T) {
    this.value = Event(content)
}

fun <T> MutableLiveData<Event<T>>.post(content: T) {
    this.postValue(Event(content))
}