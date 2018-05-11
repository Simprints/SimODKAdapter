package com.simprints.simodkadapter.activities.main

import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.extensions.getConfidencesString
import com.simprints.simodkadapter.extensions.getIdsString
import com.simprints.simodkadapter.extensions.getTiersString


class MainPresenter(private val view: MainContract.View,
                    private val action: String) : MainContract.Presenter {

    companion object {
        private const val PACKAGE_NAME = "com.simprints.simodkadapter"
        private const val ACTION_REGISTER = "$PACKAGE_NAME.REGISTER"
        private const val ACTION_IDENTIFY = "$PACKAGE_NAME.IDENTIFY"
        private const val ACTION_VERIFY = "$PACKAGE_NAME.VERIFY"
    }

    override fun start() = when (action) {
        ACTION_REGISTER -> view.requestRegisterCallout()
        ACTION_IDENTIFY -> view.requestIdentifyCallout()
        ACTION_VERIFY -> view.requestVerifyCallout()
        else -> view.returnActionErrorToClient()
    }

    override fun processRegistration(registration: Registration) =
            view.returnRegistration(registration.guid)

    override fun processIdentification(identifications: ArrayList<Identification>) =
            view.returnIdentification(
                    identifications.getIdsString(),
                    identifications.getConfidencesString(),
                    identifications.getTiersString()
            )

    override fun processVerification(verification: Verification) = view.returnVerification(
            verification.guid,
            verification.confidence.toString(),
            verification.tier.toString()
    )

    override fun processReturnError() = view.returnActionErrorToClient()

}
