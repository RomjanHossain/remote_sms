package com.dstore.sms

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import android.telephony.SmsManager.getSmsManagerForSubscriptionId
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.O)
    override
    fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            val action = remoteMessage.data["action"]
            val phoneNumber = remoteMessage.data["phoneNumber"]
            val message = remoteMessage.data["message"]
//            if (action == "FLASH_ON") {

//            if (phoneNumber != null && message != null) {
//
//                sendMessage(phoneNumber, message)
//            } else {
//                val munnaBhai = "+8801748614007"
//                val mahtabBhai = "+8801634222444"
//                // 43.93 taka
//                sendMessage(mahtabBhai, "FireBase 4 :}")
//            }
//            }
            if (phoneNumber != null && message != null) {
                // Start the foreground service to send SMS
                val serviceIntent = Intent(this, SmsForegroundService::class.java).apply {
                    putExtra("phoneNumber", phoneNumber)
                    putExtra("message", message)
                }
                startForegroundService(serviceIntent)
            }else{
                val mahtabBhai = "+8801634222444"

                // Start the foreground service to send SMS
                val serviceIntent = Intent(this, SmsForegroundService::class.java).apply {
                    putExtra("phoneNumber", mahtabBhai)
                    putExtra("message", "message From Forground services")
                }
                startForegroundService(serviceIntent)
            }
        }
    }

    private fun sendMessage(phone: String, msg: String) {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            try {
                Log.d(TAG, "sending message.....")
                val subscriptionId = SmsManager.getDefaultSmsSubscriptionId()
                val smsManager = getSmsManagerForSubscriptionId(subscriptionId)
                smsManager.sendTextMessage(phone, null, msg, null, null)
                Log.d(TAG, "Message SENT")
//            return "SuccessFully send the message"
            } catch (e: Exception) {
                Log.d(TAG, "Error sending msg: $e")
                throw IllegalArgumentException("Something went wrong: $e")
            }
        } else {
            Log.d(TAG, "Permission not granted :}")
        }
    }


    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }

    companion object {
        private const val TAG = "FCMService"
    }
}
