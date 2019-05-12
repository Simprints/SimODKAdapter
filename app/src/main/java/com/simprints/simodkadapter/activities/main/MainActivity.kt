package com.simprints.simodkadapter.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.simprints.libsimprints.Constants.*
import com.simprints.libsimprints.Identification
import com.simprints.simodkadapter.R
import com.simprints.simodkadapter.events.EventObserver
import com.simprints.simodkadapter.events.SingleObserver
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity(), MainContract.View {

    companion object {
        private const val REGISTER_REQUEST_CODE = 97
        private const val IDENTIFY_REQUEST_CODE = 98
        private const val VERIFY_REQUEST_CODE = 99

        private const val ODK_REGISTRATION_ID_KEY = "odk-registration-id"
        private const val ODK_GUIDS_KEY = "odk-guids"
        private const val ODK_CONFIDENCES_KEY = "odk-confidences"
        private const val ODK_TIERS_KEY = "odk-tiers"
        private const val ODK_SESSION_ID = "odk-session-id"
    }

    override val viewModel: MainContract.ViewModel by inject { parametersOf(intent.action) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.requestRegisterCallout.observe(this, SingleObserver { requestRegisterCallout() })
        viewModel.requestIdentifyCallout.observe(this, SingleObserver { requestIdentifyCallout() })
        viewModel.requestVerifyCallout.observe(this, SingleObserver { requestVerifyCallout() })
        viewModel.requestConfirmIdentityCallout.observe(this, SingleObserver { requestConfirmIdentityCallout() })
        viewModel.returnActionErrorToClient.observe(this, SingleObserver { returnActionErrorToClient() })

        viewModel.returnRegistration.observe(this, EventObserver { returnRegistration(it) })
        viewModel.returnIdentification.observe(this, EventObserver {
            returnIdentification(it.idList, it.confidenceList, it.tierList, it.sessionId)
        })
        viewModel.returnVerification.observe(this, EventObserver {
            returnVerification(it.id, it.confidence, it.tier)
        })
    }

    private fun returnActionErrorToClient() {
        setResult(SIMPRINTS_INVALID_INTENT_ACTION, intent)
        finish()
    }

    private fun requestRegisterCallout() {
        val registerIntent = Intent(SIMPRINTS_REGISTER_INTENT).apply { putExtras(intent) }
        startActivityForResult(registerIntent, REGISTER_REQUEST_CODE)
    }

    private fun requestIdentifyCallout() {
        val identifyIntent = Intent(SIMPRINTS_IDENTIFY_INTENT).apply { putExtras(intent) }
        startActivityForResult(identifyIntent, IDENTIFY_REQUEST_CODE)
    }

    private fun requestVerifyCallout() {
        val verifyIntent = Intent(SIMPRINTS_VERIFY_INTENT).apply { putExtras(intent) }
        startActivityForResult(verifyIntent, VERIFY_REQUEST_CODE)
    }

    private fun requestConfirmIdentityCallout() {
        try {
            startService(Intent(SIMPRINTS_SELECT_GUID_INTENT).apply {
                putExtras(intent)
                setPackage(SIMPRINTS_PACKAGE_NAME)
            })
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
            toast(getString(R.string.failed_confirmation))
        }
        setResult(RESULT_OK)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK || data == null)
            setResult(resultCode, data).also { finish() }
        else
            when (requestCode) {
                REGISTER_REQUEST_CODE -> viewModel.processRegistration(
                        data.getParcelableExtra(SIMPRINTS_REGISTRATION)
                )
                IDENTIFY_REQUEST_CODE -> viewModel.processIdentification(
                        data.getParcelableArrayListExtra<Identification>(SIMPRINTS_IDENTIFICATIONS),
                        data.getStringExtra(SIMPRINTS_SESSION_ID)
                )
                VERIFY_REQUEST_CODE -> viewModel.processVerification(
                        data.getParcelableExtra(SIMPRINTS_VERIFICATION)
                )
                else -> viewModel.processReturnError()
            }
    }

    private fun returnRegistration(registrationId: String) = Intent().let {
        it.putExtra(ODK_REGISTRATION_ID_KEY, registrationId)
        sendOkResult(it)
    }

    private fun returnIdentification(idList: String,
                                     confidenceList: String,
                                     tierList: String,
                                     sessionId: String) =
            Intent().let {
                it.putExtra(ODK_GUIDS_KEY, idList)
                it.putExtra(ODK_CONFIDENCES_KEY, confidenceList)
                it.putExtra(ODK_TIERS_KEY, tierList)
                it.putExtra(ODK_SESSION_ID, sessionId)
                sendOkResult(it)
            }

    private fun returnVerification(id: String, confidence: String, tier: String) =
            Intent().let {
                it.putExtra(ODK_GUIDS_KEY, id)
                it.putExtra(ODK_CONFIDENCES_KEY, confidence)
                it.putExtra(ODK_TIERS_KEY, tier)
                sendOkResult(it)
            }

    private fun sendOkResult(intent: Intent) {
        setResult(RESULT_OK, intent)
        finish()
    }

}
