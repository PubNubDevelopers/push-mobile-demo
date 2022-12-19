package com.pubnub.pushdemo

import com.google.firebase.installations.FirebaseInstallations
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId

/*
Singleton Class Used to house single instance of the PubNub object to be used across
all Activities.
 */
class PubNubObj {
    companion object PubNubObj {
        private val pubNub : PubNub = PubNub(
            PNConfiguration(UserId(FirebaseInstallations.getInstance().id.toString())).apply {
                //  Not delaring a publish key since we are only receiving data
                subscribeKey = BuildConfig.SUBSCRIBE_KEY
            }
        )
    }

    //Return the instance of the PubNub companion object to be used across all instances.
    fun getInstance() : PubNub {
        return pubNub
    }
}