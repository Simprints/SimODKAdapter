package com.simprints.simodkadapter.activities.main

import androidx.lifecycle.MutableLiveData
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.BaseView
import com.simprints.simodkadapter.events.DataEvent
import com.simprints.simodkadapter.events.Event


interface MainContract {

    interface View : BaseView<ViewModel>

    interface ViewModel : com.simprints.simodkadapter.ViewModel {

        val requestRegisterCallout: MutableLiveData<Event>
        val requestIdentifyCallout: MutableLiveData<Event>
        val requestVerifyCallout: MutableLiveData<Event>
        val requestConfirmIdentityCallout: MutableLiveData<Event>
        val returnActionErrorToClient: MutableLiveData<Event>

        val returnRegistration: MutableLiveData<DataEvent<String>>
        val returnIdentification: MutableLiveData<DataEvent<MainViewModel.ReturnIdentification>>
        val returnVerification: MutableLiveData<DataEvent<MainViewModel.ReturnVerification>>

        fun start(action: String?)

        fun processRegistration(registration: Registration)

        fun processIdentification(identifications: ArrayList<Identification>, sessionId: String)

        fun processVerification(verification: Verification)

        fun processReturnError()

    }

}