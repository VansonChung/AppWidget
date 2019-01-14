package com.van.appwidget

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class PinningReceiver: BroadcastReceiver() {

    companion object {
        private const val TAG = "PinningReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "onReceive : " + intent.toString())
    }
}