package com.mcgrady.xproject.screen_match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.blankj.utilcode.util.*

class ScreenMatchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen_match)

        Utils.init(application)

        /**
         * getScreenDensity   : 获取屏幕密度
         * getScreenDensityDpi: 获取屏幕密度 DPI
         */

        Log.d(TAG,"isPhone: ${PhoneUtils.isPhone()}")
        Log.d(TAG,"isTablet: ${DeviceUtils.isTablet()}")
        Log.d(TAG,"ScreenDensity: ${ScreenUtils.getScreenDensity()}")
        Log.d(TAG,"ScreenDensityDpi: ${ScreenUtils.getScreenDensityDpi()}")

        val width = ScreenUtils.getScreenWidth()
        val height = ScreenUtils.getScreenHeight()
        val appWidth = ScreenUtils.getAppScreenWidth()
        val appHeight = ScreenUtils.getAppScreenHeight()

        Log.d(TAG,"Screen size = ${width}x${height}")
        Log.d(TAG,"App screen size = ${ScreenUtils.getAppScreenWidth()}x${ScreenUtils.getAppScreenHeight()}")

        val dpi = Math.sqrt((appWidth*appWidth + appHeight*appHeight).toDouble())/5.2
        Log.d(TAG,"dpi: sqrt(width^2 + height^2)/5.2inch = $dpi")

        val pxValue = resources.getDimension(R.dimen.dp_1)
        val dpValue = SizeUtils.px2dp(pxValue)

        Log.d(TAG, "px value = $pxValue")
        Log.d(TAG, "dp value = $dpValue")

    }
}