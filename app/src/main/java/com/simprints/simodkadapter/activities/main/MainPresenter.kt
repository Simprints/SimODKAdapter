package com.simprints.simodkadapter.activities.main

import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification


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

    override fun processRegistration(registration: Registration) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun processIdentification(identifications: ArrayList<Identification>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun processVerification(verification: Verification) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun processReturnError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
