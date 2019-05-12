package com.simprints.simodkadapter.extensions

import androidx.lifecycle.MutableLiveData
import com.simprints.simodkadapter.events.Event

fun MutableLiveData<Event>.set() {
    this.value = Event()
}

fun MutableLiveData<Event>.post() {
    this.postValue(Event())
}