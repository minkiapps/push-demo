package com.aco.pushdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException


class MainActivity : AppCompatActivity() {

    private var tvToken: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvToken = findViewById(R.id.tv_log)

        getToken(object : TokenCallback {
            override fun onTokenSuccess(token: String?) {
                sendRegTokenToServer(token!!)
            }

            override fun onError(error: String) {
                sendRegTokenToServer(error)
            }
        })

        val receiver = MyReceiver()
        val filter = IntentFilter()
        filter.addAction("com.huawei.push.codelab.ON_NEW_TOKEN")
        this@MainActivity.registerReceiver(receiver, filter)
    }

    inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if ("com.huawei.push.codelab.ON_NEW_TOKEN" == intent.action) {
                val token = intent.getStringExtra("token")
                tvToken!!.text = token
            }
        }
    }

    private fun getToken(callback: TokenCallback) {
        object : Thread() {
            override fun run() {
                try {
                    // read from agconnect-services.json
                    val appId = AGConnectServicesConfig.fromContext(this@MainActivity).getString("client/app_id")
                    val token = HmsInstanceId.getInstance(this@MainActivity).getToken(appId, "HCM")
                    if (!TextUtils.isEmpty(token)) {
                        callback.onTokenSuccess(token)
                    } else {
                        callback.onError("Empty token")
                    }

                } catch (e: ApiException) {
                    callback.onError(e.toString())
                }
            }
        }.start()
    }

    interface TokenCallback {
        fun onTokenSuccess(token: String?)
        fun onError(error: String)
    }

    private fun sendRegTokenToServer(token: String) {
        Log.i("PushKitToken", token)
    }
}