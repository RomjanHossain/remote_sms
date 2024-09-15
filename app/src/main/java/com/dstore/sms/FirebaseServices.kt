package com.dstore.sms

import android.telephony.SmsManager
import android.telephony.SmsManager.getSmsManagerForSubscriptionId
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
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

            if (phoneNumber!=null && message !=null ) {

                sendMessage(phoneNumber, message)
            }else{
                val munnaBhai = "+8801748614007"
                val mahtabBhai = "+8801634222444"
                // 43.93 taka
                sendMessage(munnaBhai, "Message From FireBase 3 :}")
            }
//            }
        }
    }

    private fun sendMessage(phone: String, msg: String): String {
        try {
            Log.d("SMS", "sending message.....")
            val subscriptionId = SmsManager.getDefaultSmsSubscriptionId()
            val smsManager = getSmsManagerForSubscriptionId(subscriptionId)
            smsManager.sendTextMessage(phone, null, msg, null, null)
            return "SuccessFully send the message"
        } catch (e: Exception) {
            Log.d("SMS", "Error sending msg: $e")
            throw IllegalArgumentException("Something went wrong: $e")
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
