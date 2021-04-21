package com.example.pangea

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

@Suppress("DEPRECATION")

class Facebook : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this);
    }
}