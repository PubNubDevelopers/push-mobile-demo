package com.pubnub.pushdemo

import com.google.firebase.installations.FirebaseInstallations
import com.pubnub.api.v2.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.UserId

/*
Singleton Class Used to house single instance of the PubNub object to be used across
all Activities.
 */
class PubNubObj {
    companion object PubNubObj {
        private val pubNub = PubNub.create(
            PNConfiguration.builder(UserId(FirebaseInstallations.getInstance().id.toString()),
                "sub-c-c75d3e26-120c-49c8-8b7e-f78aa7b6bdc7").build()
        )
    }

    //Return the instance of the PubNub companion object to be used across all instances.
    fun getInstance() : PubNub {
        return pubNub
    }
}