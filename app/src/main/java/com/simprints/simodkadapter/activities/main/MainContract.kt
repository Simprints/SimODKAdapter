package com.simprints.simodkadapter.activities.main

import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.BasePresenter
import com.simprints.simodkadapter.BaseView


interface MainContract {

    interface View : BaseView<Presenter> {

        fun returnActionErrorToClient()

        fun requestRegisterCallout()

        fun requestIdentifyCallout()

        fun requestVerifyCallout()

    }

    interface Presenter : BasePresenter {

        fun processRegistration(registration: Registration)

        fun processIdentification(identifications: ArrayList<Identification>)

        fun processVerification(verification: Verification)

        fun processReturnError()

    }

}