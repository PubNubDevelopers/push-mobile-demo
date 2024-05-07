package com.pubnub.pushdemo2

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
                "sub-c-3fcd12ba-78be-43bd-9403-71f00a9025fa").build()
        )
    }

    //Return the instance of the PubNub companion object to be used across all instances.
    fun getInstance() : PubNub {
        return pubNub
    }
}