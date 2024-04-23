package com.pubnub.pushdemo

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNPushType
import org.greenrobot.eventbus.EventBus

class FCMHandler : FirebaseMessagingService() {
    private val LOG_TAG = "PNPushDemo"
    private val pubnub: PubNub = PubNubObj().getInstance()

    override fun onNewToken(token: String) {
        //Called whenever the FCM token is renewed - re-register the device with PubNub
        sendRegistrationToPubNub(token)
    }

    //Handle when the device has received a push notification from FCM.
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val clientIdentifier = applicationContext.getSharedPreferences("prefs.db", 0).getString("identifier", null)
        var title : String
        var body : String

        // Check if message contains a data payload.
        //  This is always handled by the application regardless of foreground / background.
        //  Display the notification ourselves
        if (remoteMessage.data.isNotEmpty()) {
            remoteMessage.data.let {
                Log.d(LOG_TAG, "Message data payload: " + remoteMessage.data)
                InteractiveDemo().actionComplete(applicationContext, clientIdentifier, "Receive a Push Message (Data Payload) on Android")
                title = remoteMessage.data.get("title").toString()
                body = remoteMessage.data.get("body").toString()
                sendNotification(title, body)
            }
        }

        // Check if message contains a notification payload.
        //  This will never be the case when the app is in the background (since the
        //  notification is handled automatically by FCM).
        //  If we are in the foreground, display a snackbar rather than a notification
        remoteMessage.notification?.let {
            Log.d(LOG_TAG, "Message Notification Body: ${it.body}")
            InteractiveDemo().actionComplete(applicationContext, clientIdentifier, "Receive a Push Message (Notification Payload) in a Foreground Android app")
            title = remoteMessage.notification?.title.toString()
            body = remoteMessage.notification?.body.toString()
            //  EventBus is used to communicate between the MainActivity and the FCM receiving service
            EventBus.getDefault().post(Pair(title, body))
        }
    }

    //  Register the device on PubNub Channels to enable push notifications on those channels.
    //  This is called whenever the token changes
    private fun sendRegistrationToPubNub(token : String) {
            pubnub.addPushNotificationsOnChannels(
                pushType = PNPushType.FCM,
                deviceId = token,
                channels = listOf("push-demo") //provide a list of channels to enable push on them.
            ).async { result ->
                result.onFailure {
                    exception ->
                    Log.d(LOG_TAG, "Push Registration Failed: $exception")
                }.onSuccess {
                    Log.d(LOG_TAG,"Push Registration Succeeded")
                }
            }
    }

    //Send the notification to the device manager to display the notification to the user.
    private fun sendNotification(title: String, body: String) {
        val notificationID = 101
        //  Just launch the app if the notification is tapped
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pend = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val channelID = getString(R.string.default_notification_channel_id)

        var builder = NotificationCompat.Builder(this, channelID)
            .setSmallIcon(R.drawable.ic_pn)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(resources.getColor(R.color.pubnub_red, theme))
            .setContentIntent(pend)
            .setAutoCancel(true)
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationID, builder.build())
    }
}