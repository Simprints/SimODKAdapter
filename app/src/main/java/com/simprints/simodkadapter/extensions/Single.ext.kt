package com.simprints.simodkadapter.extensions

import androidx.lifecycle.MutableLiveData
import com.simprints.simodkadapter.events.Single

fun MutableLiveData<Single>.set() {
    this.value = Single()
}

fun MutableLiveData<Single>.post() {
    this.postValue(Single())
}