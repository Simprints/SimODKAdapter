package com.simprints.simodkadapter.activities.main

import androidx.lifecycle.MutableLiveData
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.BaseView
import com.simprints.simodkadapter.events.DataEvent
import com.simprints.simodkadapter.events.Event


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

        val requestRegisterCallout: MutableLiveData<Event>
        val requestIdentifyCallout: MutableLiveData<Event>
        val requestVerifyCallout: MutableLiveData<Event>
        val requestConfirmIdentityCallout: MutableLiveData<Event>
        val returnActionErrorToClient: MutableLiveData<Event>

        val returnRegistration: MutableLiveData<DataEvent<String>>
        val returnIdentification: MutableLiveData<DataEvent<MainViewModel.ReturnIdentification>>
        val returnVerification: MutableLiveData<DataEvent<MainViewModel.ReturnVerification>>

        fun processRegistration(registration: Registration)

        fun processIdentification(identifications: ArrayList<Identification>, sessionId: String)

        fun processVerification(verification: Verification)

        fun processReturnError()

    }

}