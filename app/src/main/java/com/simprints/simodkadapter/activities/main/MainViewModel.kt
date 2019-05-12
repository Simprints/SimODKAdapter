package com.simprints.simodkadapter.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.events.Event
import com.simprints.simodkadapter.events.Single
import com.simprints.simodkadapter.extensions.getConfidencesString
import com.simprints.simodkadapter.extensions.getIdsString
import com.simprints.simodkadapter.extensions.getTiersString
import com.simprints.simodkadapter.extensions.set


class MainViewModel(private val action: String?) : ViewModel(), MainContract.ViewModel {

    companion object {
        private const val PACKAGE_NAME = "com.simprints.simodkadapter"
        const val ACTION_REGISTER = "$PACKAGE_NAME.REGISTER"
        const val ACTION_IDENTIFY = "$PACKAGE_NAME.IDENTIFY"
        const val ACTION_VERIFY = "$PACKAGE_NAME.VERIFY"
        const val ACTION_CONFIRM_IDENTITY = "$PACKAGE_NAME.CONFIRM_IDENTITY"
    }

    override val requestRegisterCallout = MutableLiveData<Single>()
    override val requestIdentifyCallout = MutableLiveData<Single>()
    override val requestVerifyCallout = MutableLiveData<Single>()
    override val requestConfirmIdentityCallout = MutableLiveData<Single>()
    override val returnActionErrorToClient = MutableLiveData<Single>()

    override val returnRegistration = MutableLiveData<Event<String>>()
    override val returnIdentification = MutableLiveData<Event<ReturnIdentification>>()
    override val returnVerification = MutableLiveData<Event<ReturnVerification>>()

    override fun start() = when (action) {
        ACTION_REGISTER -> requestRegisterCallout.set()
        ACTION_IDENTIFY -> requestIdentifyCallout.set()
        ACTION_VERIFY -> requestVerifyCallout.set()
        ACTION_CONFIRM_IDENTITY -> requestConfirmIdentityCallout.set()
        else -> returnActionErrorToClient.set()
    }

    override fun processRegistration(registration: Registration) =
            returnRegistration.set(registration.guid)

    override fun processIdentification(identifications: ArrayList<Identification>, sessionId: String) =
            returnIdentification.set(ReturnIdentification(
                    identifications.getIdsString(),
                    identifications.getConfidencesString(),
                    identifications.getTiersString(),
                    sessionId
            ))

    override fun processVerification(verification: Verification) =
            returnVerification.set(ReturnVerification(
                    verification.guid,
                    verification.confidence.toString(),
                    verification.tier.toString()
            ))

    override fun processReturnError() = returnActionErrorToClient.set()

    data class ReturnIdentification(val idList: String,
                                    val confidenceList: String,
                                    val tierList: String,
                                    val sessionId: String)

    data class ReturnVerification(val id: String, val confidence: String, val tier: String)

}