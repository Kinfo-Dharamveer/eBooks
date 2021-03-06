package com.kinfoitsolutions.ebooks.ui

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kinfoitsolutions.ebooks.ui.data.Constants.CONNECTIVITY_ACTION
import com.kinfoitsolutions.ebooks.ui.service.NetworkChangeReceiver
import id.zelory.compressor.Compressor
import org.greenrobot.eventbus.EventBus
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity: AppCompatActivity() {


    internal lateinit var intentFilter: IntentFilter
    internal lateinit var receiver: NetworkChangeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        intentFilter = IntentFilter()
        intentFilter.addAction(CONNECTIVITY_ACTION)
        receiver = NetworkChangeReceiver()


    }

    protected override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter)
    }



    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }


    fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


}

