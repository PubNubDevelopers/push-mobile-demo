package com.pubnub.pushdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.pubnub.api.PubNub
import com.pubnub.api.enums.PNPushType
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {
    private val LOG_TAG = "PNPushDemo"
    private var clientIdentifier: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clientIdentifier = applicationContext.getSharedPreferences("prefs.db", 0).getString("identifier", null) //  Works!
        Log.d(LOG_TAG, "Identifier is: " + clientIdentifier.toString())
        InteractiveDemo().actionComplete(applicationContext, clientIdentifier, "Launch Android Application")
        createNotificationChannel()
        getToken()
        val txtReadme = findViewById<TextView>(R.id.txtReadme)
        val readme = SpannableStringBuilder()
            .bold { append("Instructions:\n\n") }
            .bold { append("Please Wait...\n\n")}
            .append("Web-based emulator is still loading.  This may take several seconds.  This text will update when the emulator is ready.\n\n")
        txtReadme.text = readme;
    }

    private fun getToken()
    {
        val pubnub: PubNub = PubNubObj().getInstance() //Using a singleton PubNub object to persist across the activities.
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            try {
                val token = task.result
                if (token != null) {
                    pubnub.addPushNotificationsOnChannels(
                        pushType = PNPushType.FCM,
                        deviceId = token,
                        channels = listOf("push-demo") //provide a list of channels to enable push on them.
                    ).async { result, status ->
                        Log.d(LOG_TAG, "Status: $status, Result: $result")
                        val txtReadme = findViewById<TextView>(R.id.txtReadme)
                        val readme = SpannableStringBuilder()
                            .bold { append("Instructions:\n\n") }
                            .append("This application will receive push messages sent to the 'push-demo' channel.\n\n")
                            .append("Messages sent with the ")
                            .bold { append("'data' ") }
                            .append("payload will always be displayed as a notification, regardless of whether the app is in the foreground or background.\n\n")
                            .append("Messages sent with the ")
                            .bold { append("'notification' ") }
                            .append("payload will be shown as a notification when the app is in the background but as a snackbar when the app is in the foreground.")
                        txtReadme.text = readme;
                    }
                }
            } catch (e: Exception) {
                //  This issue seems to happen on a newly launched emulator through Appetize.io ('switching to device').  Seems to be a known issue.
                //  https://stackoverflow.com/questions/62562243/java-io-ioexception-authentication-failed-in-android-firebase-and-service-not
                Handler(Looper.getMainLooper()).postDelayed(Runnable { getToken(); }, 1000)
            }
        }
    }


    override fun onPause() {
        super.onPause()
        //  EventBus is used to communicate between the MainActivity and the FCM receiving service
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        //  EventBus is used to communicate between the MainActivity and the FCM receiving service
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun doThis(message: Pair<String, String>) {
        val snackbarText = SpannableStringBuilder()
            .bold { append(message.first + "\n" + message.second) }
        val snackbar = Snackbar.make(findViewById(android.R.id.content), snackbarText, Snackbar.LENGTH_LONG)
            .setBackgroundTint(resources.getColor(R.color.pubnub_red, theme))
            .setTextColor(resources.getColor(R.color.white, theme))
        val view = snackbar.view
        val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tv.textSize = 22f
        snackbar.show()
    }

    //Creates the notification channel necessary to display incoming push notifications
    //Register the channel with the system
    private fun createNotificationChannel() {
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.default_notification_channel_name)
            val descriptionText = getString(R.string.default_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel( getString(R.string.default_notification_channel_id),name, importance).apply {
                description = descriptionText
            }
            //Channel settings
            channel.description = descriptionText
            notificationManager.createNotificationChannel(channel)
        }
    }
}