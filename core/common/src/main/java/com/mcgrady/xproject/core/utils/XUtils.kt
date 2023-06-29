package com.mcgrady.xproject.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import androidx.annotation.ColorInt
import java.util.*

/**
 * Created by mcgrady on 2022/11/26.
 */
object XUtils {

    /**
     * Return the width of screen, in pixel.
     *
     * @return the width of screen, in pixel
     */
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }

    /**
     * Return the height of screen, in pixel.
     *
     * @return the height of screen, in pixel
     */
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.y
    }

    /**
     * Return the random color.
     *
     * @return the random color
     */
    fun getRandomColor(): Int {
        return getRandomColor(true)
    }

    /**
     * Return the random color.
     *
     * @param supportAlpha True to support alpha, false otherwise.
     * @return the random color
     */
    fun getRandomColor(supportAlpha: Boolean): Int {
        val high = if (supportAlpha) (Math.random() * 0x100).toInt() shl 24 else -0x1000000
        return high or (Math.random() * 0x1000000).toInt()
    }

    /**
     * Set the alpha component of `color` to be `alpha`.
     *
     * @param color The color.
     * @param alpha Alpha component \([0..255]\) of the color.
     * @return the `color` with `alpha` component
     */
    fun setAlphaComponent(
        @ColorInt color: Int, alpha: Int
    ): Int {
        return color and 0x00ffffff or (alpha shl 24)
    }

    /**
     * Set the alpha component of `color` to be `alpha`.
     *
     * @param color The color.
     * @param alpha Alpha component \([0..1]\) of the color.
     * @return the `color` with `alpha` component
     */
    fun setAlphaComponent(
        @ColorInt color: Int, alpha: Float
    ): Int {
        return color and 0x00ffffff or ((alpha * 255.0f + 0.5f).toInt() shl 24)
    }


    private const val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
    fun randomString(): String {
        val salt = StringBuilder()
        val rnd = Random()
        while (salt.length < 18) { // length of the random string.
            val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
            salt.append(SALTCHARS[index])
        }
        return salt.toString()
    }

    fun getRandomString(candidateChars: String = SALTCHARS, length: Int): String {
        val sb = StringBuilder()
        val random = Random()
        for (i in 0 until length) {
            sb.append(
                candidateChars[random.nextInt(
                    candidateChars
                        .length
                )]
            )
        }
        return sb.toString()
    }


}