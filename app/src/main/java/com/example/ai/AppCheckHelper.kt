package com.example.ai

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

object AppCheckHelper {
    private const val TAG = "AppCheckHelper"

    fun configureAppCheck(context: Context, intent: Intent?) {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            val firebaseAppCheck = FirebaseAppCheck.getInstance()
            firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance()
            )
            Log.d(TAG, "Firebase App Check initialized successfully for Only4Aline")
        } catch (e: Exception) {
            Log.w(TAG, "App Check initialization warning: ${e.message}")
        }
    }
}
