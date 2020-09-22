package com.aco.pushdemo
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.huawei.hms.push.HmsMessageService


class MyPushService : HmsMessageService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, "receive token:$token")
        Toast.makeText(this, "receive token:$token", Toast.LENGTH_LONG).show()
        sendTokenToDisplay(token)
    }
    private fun sendTokenToDisplay(token: String) {
        val intent = Intent("com.huawei.push.codelab.ON_NEW_TOKEN")
        intent.putExtra("token", token)
        sendBroadcast(intent)
    }

    companion object {
        private const val TAG = "PushDemoLog"
    }
}