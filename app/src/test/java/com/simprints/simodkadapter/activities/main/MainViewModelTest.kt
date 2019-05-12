package com.simprints.simodkadapter.activities.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Tier
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.activities.main.MainViewModel.Companion.ACTION_CONFIRM_IDENTITY
import com.simprints.simodkadapter.activities.main.MainViewModel.Companion.ACTION_IDENTIFY
import com.simprints.simodkadapter.activities.main.MainViewModel.Companion.ACTION_REGISTER
import com.simprints.simodkadapter.activities.main.MainViewModel.Companion.ACTION_VERIFY
import com.simprints.simodkadapter.activities.main.MainViewModel.ReturnIdentification
import com.simprints.simodkadapter.activities.main.MainViewModel.ReturnVerification
import com.simprints.simodkadapter.events.EventObserver
import com.simprints.simodkadapter.events.Single
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.isA
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    companion object {
        private const val SINGLE_INVOCATION = 1
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var singleObserver: Observer<Single>

    @Test
    fun startPresenterForRegister_ShouldRequestRegister() {
        MainViewModel(ACTION_REGISTER).apply {
            start()
            requestRegisterCallout.observeForever(singleObserver)
        }
        verify(singleObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Single::class.java))
    }

    @Test
    fun startPresenterForIdentify_ShouldRequestIdentify() {
        MainViewModel(ACTION_IDENTIFY).apply {
            start()
            requestIdentifyCallout.observeForever(singleObserver)
        }
        verify(singleObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Single::class.java))
    }

    @Test
    fun startPresenterForVerify_ShouldRequestVerify() {
        MainViewModel(ACTION_VERIFY).apply {
            start()
            requestVerifyCallout.observeForever(singleObserver)
        }
        verify(singleObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Single::class.java))
    }

    @Test
    fun startPresenterWithGarbage_ShouldReturnActionError() {
        MainViewModel(ACTION_CONFIRM_IDENTITY).apply {
            start()
            requestConfirmIdentityCallout.observeForever(singleObserver)
        }
        verify(singleObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Single::class.java))
    }

    @Test
    fun processRegistration_ShouldReturnValidOdkRegistration() {
        val registration = Registration(UUID.randomUUID().toString())
        val ob: EventObserver<String> = Mockito.spy(EventObserver {
            assert(it == registration.guid)
        })

        MainViewModel(ACTION_REGISTER).apply {
            processRegistration(registration)
            returnRegistration.observeForever(ob)
        }
    }

    @Test
    fun processIdentification_ShouldReturnValidOdkIdentification() {
        val id1 = Identification(UUID.randomUUID().toString(), 100, Tier.TIER_1)
        val id2 = Identification(UUID.randomUUID().toString(), 15, Tier.TIER_5)
        val sessionId = UUID.randomUUID().toString()

        val ob: EventObserver<ReturnIdentification> = Mockito.spy(EventObserver {
            assert(it.idList == "${id1.guid} ${id2.guid}")
            assert(it.confidenceList == "${id1.confidence} ${id2.confidence}")
            assert(it.tierList == "${id1.tier} ${id2.tier}")
            assert(it.sessionId == sessionId)
        })

        MainViewModel(ACTION_IDENTIFY).apply {
            processIdentification(arrayListOf(id1, id2), sessionId)
            returnIdentification.observeForever(ob)
        }
    }

    @Test
    fun processVerification_ShouldReturnValidOdkVerification() {
        val verification = Verification(100, Tier.TIER_1, UUID.randomUUID().toString())

        val ob: EventObserver<ReturnVerification> = Mockito.spy(EventObserver {
            assert(it.id == verification.guid)
            assert(it.confidence == verification.confidence.toString())
            assert(it.tier == verification.tier.toString())
        })

        MainViewModel(ACTION_VERIFY).apply {
            processVerification(verification)
            returnVerification.observeForever(ob)
        }
    }

    @Test
    fun processReturnError_ShouldCallActionError() {
        MainViewModel("").apply {
            start()
            returnActionErrorToClient.observeForever(singleObserver)
        }
        verify(singleObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Single::class.java))
    }

    @Test
    fun startPresenterForConfirmIdentity_ShouldRequestConfirmIdentity() {
        MainViewModel(ACTION_CONFIRM_IDENTITY).apply {
            start()
            requestConfirmIdentityCallout.observeForever(singleObserver)
        }
        verify(singleObserver, Mockito.times(SINGLE_INVOCATION)).onChanged(isA(Single::class.java))
    }

}