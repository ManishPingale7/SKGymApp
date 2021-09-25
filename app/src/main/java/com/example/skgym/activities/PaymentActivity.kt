package com.example.skgym.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.skgym.R
import com.example.skgym.data.Plan
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.*
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONArray
import org.json.JSONObject

class PaymentActivity : AppCompatActivity() {
    private lateinit var paymentsClient: PaymentsClient
    private val LOAD_PAYMENT_DATA_REQUEST_CODE = 100

    private val tokenizationSpecification = JSONObject().apply {
        put("type", "PAYMENT_GATEWAY")
        put(
            "parameters", JSONObject(
                mapOf(
                    "gateway" to "example",
                    "gatewayMerchantId" to "exampleGatewayMerchantId"
                )
            )
        )
    }
    private val baseCardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
        })
    }
    private val transactionInfo = JSONObject().apply {
        put("totalPrice", "0.69")
        put("totalPriceStatus", "FINAL")
        put("currencyCode", "USD")
    }

    private val merchantInfo = JSONObject().apply {
        put("merchantName", "Example Merchant")
        put("merchantId", "01234567890123456789")
    }

    private val cardPaymentMethod = JSONObject().apply {
        put("type", "CARD")
        put("tokenizationSpecification", tokenizationSpecification)
        put("parameters", JSONObject().apply {
            put("allowedCardNetworks", JSONArray(listOf("VISA", "MASTERCARD")))
            put("allowedAuthMethods", JSONArray(listOf("PAN_ONLY", "CRYPTOGRAM_3DS")))
            put("billingAddressRequired", true)
            put("billingAddressParameters", JSONObject(mapOf("format" to "FULL")))
        })
    }


    private val googlePayBaseConfiguration = JSONObject().apply {
        put("apiVersion", 2)
        put("apiVersionMinor", 0)
        put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod))
    }

    private val readyToPayRequest: IsReadyToPayRequest =
        IsReadyToPayRequest.fromJson(googlePayBaseConfiguration.toString())

    private val paymentDataRequestJson = JSONObject(googlePayBaseConfiguration.toString()).apply {
        put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod))
        put("transactionInfo", transactionInfo)
        put("merchantInfo", merchantInfo)
    }

    private val paymentDataRequest: PaymentDataRequest =
        PaymentDataRequest.fromJson(paymentDataRequestJson.toString())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        paymentsClient = createPaymentsClient(this)

        val readyToPayTask = paymentsClient.isReadyToPay(readyToPayRequest)

        readyToPayTask.addOnCompleteListener {
            try {
                Log.d("tag1", "onCreate: Error12 ${it.getResult(ApiException::class.java)}")
                it.getResult(ApiException::class.java)?.let { it1 -> setGooglePayAvailable(it1) }
            } catch (e: ApiException) {
                Log.d("tag1", "onCreate: ERROR: " + e.message)
            }
        }

        if (intent.hasExtra("Plan"))
            Toast.makeText(
                applicationContext,
                "Plan: " + intent.getParcelableExtra<Plan>("Plan"),
                Toast.LENGTH_SHORT
            ).show()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOAD_PAYMENT_DATA_REQUEST_CODE -> {
                when (resultCode) {
                    Activity.RESULT_OK ->
                        data?.let { PaymentData.getFromIntent(it)?.let(::handlePaymentSuccess) }

                    Activity.RESULT_CANCELED -> {
                        // The user cancelled without selecting a payment method.
                    }

                    AutoResolveHelper.RESULT_ERROR -> {
                        AutoResolveHelper.getStatusFromIntent(data)?.let {
                            handleError(it.statusCode)
                        }
                    }
                }
            }
        }
    }

    private fun handleError(statusCode: Int) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

    }

    private fun handlePaymentSuccess(paymentData: PaymentData) {
        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
//        val paymentMethodToken = paymentData
//            .JSONObject("tokenizationData")
//            .getString("token")

        // Sample TODO: Use this token to perform a payment through your payment gateway
    }


    private fun setGooglePayAvailable(available: Boolean) {
        Toast.makeText(this, "Available!", Toast.LENGTH_SHORT).show()
        Log.d("tag1", "onCreate: Error123 ")

        if (available) {
            googlePayButton.visibility = View.VISIBLE
            googlePayButton.setOnClickListener { requestPayment() }
        } else {
            // Unable to pay using Google Pay. Update your UI accordingly.
        }
    }

    private fun requestPayment() {
        // TODO: Perform transaction
        AutoResolveHelper.resolveTask(
            paymentsClient.loadPaymentData(paymentDataRequest),
            this, LOAD_PAYMENT_DATA_REQUEST_CODE
        )
    }


    private fun createPaymentsClient(paymentActivity: PaymentActivity): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build()
        return Wallet.getPaymentsClient(paymentActivity, walletOptions)
    }

}
