package com.simprints.simodkadapter.activities.main

import com.simprints.libsimprints.Identification
import com.simprints.libsimprints.Registration
import com.simprints.libsimprints.Tier
import com.simprints.libsimprints.Verification
import com.simprints.simodkadapter.activities.main.MainPresenter.Companion.ACTION_CONFIRM_IDENTITY
import com.simprints.simodkadapter.activities.main.MainPresenter.Companion.ACTION_IDENTIFY
import com.simprints.simodkadapter.activities.main.MainPresenter.Companion.ACTION_REGISTER
import com.simprints.simodkadapter.activities.main.MainPresenter.Companion.ACTION_VERIFY
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {

    @Mock
    private val view: MainContract.View = MainActivity()

    @Test
    fun startPresenterForRegister_ShouldRequestRegister() {
        MainPresenter(view, ACTION_REGISTER).apply { start() }
        Mockito.verify(view, times(1)).requestRegisterCallout()
    }

    @Test
    fun startPresenterForIdentify_ShouldRequestIdentify() {
        MainPresenter(view, ACTION_IDENTIFY).apply { start() }
        Mockito.verify(view, times(1)).requestIdentifyCallout()
    }

    @Test
    fun startPresenterForVerify_ShouldRequestVerify() {
        MainPresenter(view, ACTION_VERIFY).apply { start() }
        Mockito.verify(view, times(1)).requestVerifyCallout()
    }

    @Test
    fun startPresenterWithGarbage_ShouldReturnActionError() {
        MainPresenter(view, "Garbage").apply { start() }
        Mockito.verify(view, times(1)).returnActionErrorToClient()
    }

    @Test
    fun processRegistration_ShouldReturnValidOdkRegistration() {
        val registerId = UUID.randomUUID().toString()

        MainPresenter(view, ACTION_REGISTER).processRegistration(Registration(registerId))
        Mockito.verify(view, times(1)).returnRegistration(registerId)
    }

    @Test
    fun processIdentification_ShouldReturnValidOdkIdentification() {
        val id1 = Identification(UUID.randomUUID().toString(), 100, Tier.TIER_1)
        val id2 = Identification(UUID.randomUUID().toString(), 15, Tier.TIER_5)
        val sessionId = UUID.randomUUID().toString()

        MainPresenter(view, ACTION_IDENTIFY).processIdentification(arrayListOf(id1, id2), sessionId)
        Mockito.verify(view, times(1)).returnIdentification(
                idList = "${id1.guid} ${id2.guid}",
                confidenceList = "${id1.confidence} ${id2.confidence}",
                tierList = "${id1.tier} ${id2.tier}",
                sessionId = sessionId
        )
    }

    @Test
    fun processVerification_ShouldReturnValidOdkVerification() {
        val verification = Verification(100, Tier.TIER_1, UUID.randomUUID().toString())

        MainPresenter(view, ACTION_IDENTIFY).processVerification(verification)
        Mockito.verify(view, times(1)).returnVerification(
                id = verification.guid,
                confidence = verification.confidence.toString(),
                tier = verification.tier.toString()
        )
    }

    @Test
    fun processReturnError_ShouldCallActionError() {
        MainPresenter(view, "").processReturnError()
        Mockito.verify(view, times(1)).returnActionErrorToClient()
    }

    @Test
    fun startPresenterForConfirmIdentity_ShouldRequestConfirmIdentity() {
        MainPresenter(view, ACTION_CONFIRM_IDENTITY).apply { start() }
        Mockito.verify(view, times(1)).requestConfirmIdentityCallout()
    }

}