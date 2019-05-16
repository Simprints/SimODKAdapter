package com.simprints.simodkadapter.activities.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.crashlytics.android.Crashlytics
import com.simprints.libsimprints.Constants.*
import com.simprints.libsimprints.Identification
import com.simprints.simodkadapter.R.string.failed_confirmation
import com.simprints.simodkadapter.events.DataEventObserver
import com.simprints.simodkadapter.events.EventObserver
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject


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

    override val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.requestRegisterCallout.observe(this, EventObserver { requestRegisterCallout() })
        viewModel.requestIdentifyCallout.observe(this, EventObserver { requestIdentifyCallout() })
        viewModel.requestVerifyCallout.observe(this, EventObserver { requestVerifyCallout() })
        viewModel.requestConfirmIdentityCallout.observe(this, EventObserver { requestConfirmIdentityCallout() })
        viewModel.returnActionErrorToClient.observe(this, EventObserver { returnActionErrorToClient() })

        viewModel.returnRegistration.observe(this, DataEventObserver { returnRegistration(it) })
        viewModel.returnIdentification.observe(this, DataEventObserver {
            returnIdentification(it.idList, it.confidenceList, it.tierList, it.sessionId)
        })
        viewModel.returnVerification.observe(this, DataEventObserver {
            returnVerification(it.id, it.confidence, it.tier)
        })

        viewModel.start(intent.action)
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
            Intent(SIMPRINTS_SELECT_GUID_INTENT).apply {
                putExtras(intent)
                setPackage(SIMPRINTS_PACKAGE_NAME)
                sendService(this)
            }
        } catch (ex: Exception) {
            Crashlytics.logException(ex)
            toast(getString(failed_confirmation))
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
                                     sessionId: String) = Intent().let {
        it.putExtra(ODK_GUIDS_KEY, idList)
        it.putExtra(ODK_CONFIDENCES_KEY, confidenceList)
        it.putExtra(ODK_TIERS_KEY, tierList)
        it.putExtra(ODK_SESSION_ID, sessionId)
        sendOkResult(it)
    }

    private fun returnVerification(id: String, confidence: String, tier: String) = Intent().let {
        it.putExtra(ODK_GUIDS_KEY, id)
        it.putExtra(ODK_CONFIDENCES_KEY, confidence)
        it.putExtra(ODK_TIERS_KEY, tier)
        sendOkResult(it)
    }

    private fun sendOkResult(intent: Intent) {
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun sendService(intent: Intent) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        startForegroundService(intent)
    else
        startService(intent)

}
