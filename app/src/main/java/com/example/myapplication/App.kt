package com.example.myapplication

import android.app.Application
import com.parse.Parse
import com.parse.ParseUser

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("")
                .clientKey("")
                .server("")
                .build()
        )
        ParseUser.enableAutomaticUser()


        ParseUser.getCurrentUser()?.saveInBackground()
    }
}
