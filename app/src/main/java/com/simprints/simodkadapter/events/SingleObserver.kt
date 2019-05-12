package com.simprints.simodkadapter.events


import androidx.lifecycle.Observer

/**
 * An [Observer] for [Single]s, simplifying the pattern of checking if the [Single]'s content has
 * already been handled.
 *
 * [onEventUnhandledContent] is *only* called if the [Single]'s contents has not been handled.
 */
open class SingleObserver(private val onEventUnhandledContent: () -> Unit) : Observer<Single> {
    override fun onChanged(event: Single?) {
        event?.getIfNotHandled()?.let {
            onEventUnhandledContent()
        }
    }
}