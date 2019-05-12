package com.simprints.simodkadapter.activities.main

import androidx.lifecycle.MutableLiveData
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.BaseView
import com.simprints.simodkadapter.events.Event
import com.simprints.simodkadapter.events.Single


interface MainContract {

    interface View : BaseView<ViewModel> {

//        fun returnActionErrorToClient()
//
//        fun requestRegisterCallout()
//
//        fun requestIdentifyCallout()
//
//        fun requestVerifyCallout()
//
//        fun requestConfirmIdentityCallout()
//
//        fun returnRegistration(registrationId: String)
//
//        fun returnIdentification(idList: String, confidenceList: String, tierList: String, sessionId: String)
//
//        fun returnVerification(id: String, confidence: String, tier: String)

    }

    interface ViewModel : com.simprints.simodkadapter.ViewModel {

        val requestRegisterCallout: MutableLiveData<Single>
        val requestIdentifyCallout: MutableLiveData<Single>
        val requestVerifyCallout: MutableLiveData<Single>
        val requestConfirmIdentityCallout: MutableLiveData<Single>
        val returnActionErrorToClient: MutableLiveData<Single>

        val returnRegistration: MutableLiveData<Event<String>>
        val returnIdentification: MutableLiveData<Event<MainViewModel.ReturnIdentification>>
        val returnVerification: MutableLiveData<Event<MainViewModel.ReturnVerification>>

        fun processRegistration(registration: Registration)

        fun processIdentification(identifications: ArrayList<Identification>, sessionId: String)

        fun processVerification(verification: Verification)

        fun processReturnError()

    }

}