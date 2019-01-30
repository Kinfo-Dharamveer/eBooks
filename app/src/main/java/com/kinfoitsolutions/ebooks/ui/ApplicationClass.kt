package com.drivingschool.android

import android.app.Application
import com.kinfoitsolutions.ebooks.R
import com.orhanobut.hawk.Hawk
import uk.co.chrisjenx.calligraphy.CalligraphyConfig


class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }


}