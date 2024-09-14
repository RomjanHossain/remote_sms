package com.dstore.sms

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.telephony.SmsManager.getSmsManagerForSubscriptionId
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmsApplication(modifier: Modifier) {
    Scaffold(modifier, topBar = {
        CenterAlignedTopAppBar(title = { Text("Auto sms sender") })
    }) {
        val innerPadding = it
        SmsBody(Modifier.fillMaxSize())
    }
}

@Composable
fun SmsBody(modifier: Modifier) {
    var phoneNumber by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // for the snakebar
    var responseSnackbar by remember { mutableStateOf("") }
    val currentContext = LocalContext.current
    //define permission in composable fun
    val getPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            label = {
                Text("Phone Number")
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            value = msg,
            onValueChange = {
                msg = it
            },
//            keyboardActions = KeyboardActions.Default.onSend,
            label = {
                Text("Message")
            },
            singleLine = false,
            maxLines = 10,
            minLines = 5,
        )
        ElevatedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = {
                Log.d("SMS", "Button clicked")
                try {
                    Log.d("SMS", "trying")
                    // if not permission granted
                    if (ContextCompat.checkSelfPermission(
                            currentContext,
                            Manifest.permission.SEND_SMS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // ask for the permission
                        Toast.makeText(
                            currentContext,
                            "Permission not Granted. Asking again",
                            Toast.LENGTH_SHORT
                        ).show()
//                    ActivityCompat.requestPermissions(currentContext, arrayOf(Manifest.permission.SEND_SMS), 200 )
                        ActivityResultContracts.RequestPermission()
                    } else {
                        Log.d("SMS", "sending message.....")
                        val subscriptionId = SmsManager.getDefaultSmsSubscriptionId()
                        val smsManager = getSmsManagerForSubscriptionId(subscriptionId)
                        smsManager.sendTextMessage(phoneNumber, null, msg, null, null)
                        Toast.makeText(
                            currentContext,
                            "Message sent Successfully",
                            Toast.LENGTH_LONG
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(currentContext, "Message not sent: $e", Toast.LENGTH_LONG)
                    Log.d("SMS", "Error on $e")
                }
                // ----------------------------

                Log.d("SMS", "Finished...")
            }
        ) {
            Text("Send")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SmsBodyPrev() {
    SmsBody(modifier = Modifier.fillMaxSize())
}

@SuppressLint("CoroutineCreationDuringComposition")
//@Composable
private fun sendMesage(phone: String, msg: String): String {
    try {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(
            phone, null, msg, null, null
        )
        return "SuccessFully send the message"
    } catch (e: Exception) {
        return "Something went wrong: $e"
    }
}
