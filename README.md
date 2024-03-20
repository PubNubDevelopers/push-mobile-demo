# PubNub Mobile Push Notification Demo for Android
> Application to show how to receive a PubNub message as a FCM (Firebase Cloud Message) on Android that has been sent over the PubNub network.

Despite the advantages of in-app messaging, there are cases where you may want to deliver messages via Mobile Push Notifications. PubNub's Mobile Push Gateway provides a method to deliver messages fast and reliably regardless of whether the application is in the foreground or background on iOS and Android devices.

PubNub's Mobile Push Notifications feature bridges native PubNub publishing with third-party push notification services including Firebase Cloud Messaging (FCM) and Apple Push Notification Service (APNs). The push gateway is a great way to make sure that you can send important data and messages out to users even when they're not active in your mobile application.

![Screenshot](https://raw.githubusercontent.com/PubNubDevelopers/push-mobile-demo/main/android/media/screenshot1.png)

## Demo

A hosted version of this demo can be found at http://www.pubnub.com/demos/push/

## Features

* Send a Firebase Cloud Message (FCM) with a data payload to an Android device
* Send a Firebase Cloud Message (FCM) with a notification payload to an Android device

## Installing / Getting started

To run this project yourself you will need a PubNub account and a Firebase Project to generate your google-services.json file.

### Requirements
- [Firebase Console](https://console.firebase.google.com/u/0/)
- [PubNub Account](#pubnub-account) (*Free*)

<a href="https://dashboard.pubnub.com/signup">
	<img alt="PubNub Signup" src="https://i.imgur.com/og5DDjf.png" width=260 height=97/>
</a>

### Get Your PubNub Keys

1. You’ll first need to sign up for a [PubNub account](https://dashboard.pubnub.com/signup/). Once you sign up, you can get your unique PubNub keys from the [PubNub Developer Portal](https://admin.pubnub.com/).

1. Sign in to your [PubNub Dashboard](https://admin.pubnub.com/).

1. Click Apps, then **Create New App**.

1. Give your app a name, and click **Create**.

1. Click your new app to open its settings, then click its keyset.

1. Add your Firebase service token to your PubNub dashboard. (See our [Mobile Android Push Notifications](https://www.pubnub.com/docs/general/push/android) documentation for full steps)  

1. Copy the Publish and Subscribe keys and paste them into your app as specified in the next step.

### Building and Running

For a detailed walkthrough describing how to add Push to your PubNub solution please see our [Mobile Push Tutorial](https://www.pubnub.com/tutorials/push-notifications/)

1. Change the package name of this application to match the package name you specified in your Firebase Console when you created the app

1. Add google-services.json to the Android application

1. Update the PUBNUB_SUBSCRIBE_KEY in your gradle.properties file to match the subscribe key you generated  during the 'Get Your PubNub Keys' phase.

1. Send a PubNub message using the [debug console](https://www.pubnub.com/docs/console/) with the following payload:

```
{
  "pn_gcm": {
   "notification": {
    "title": "Message from PubNub",
    "body": "Hello World"
   }
  }
}
```
Be sure to use the Pub / Sub keys you generated yourself.  The default channel set up in the app is 'push-demo'.

## Contributing
Please fork the repository if you'd like to contribute. Pull requests are always welcome. 

## Further Information

Checkout the following links for more information on adding Mobile Push to your PubNub solution:

- [Mobile Push Tutorial for both FCM and APNS](https://www.pubnub.com/tutorials/push-notifications/)
- [PubNub Mobile Push Documentation for FCM and APNS](https://www.pubnub.com/docs/general/push/send)