package com.simprints.simodkadapter.events


open class Single {

    var hasBeenHandled = false
        private set // Allow external read but not write

    fun getIfNotHandled() {
        hasBeenHandled = true
    }

}

