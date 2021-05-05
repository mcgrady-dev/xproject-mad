package com.mcgrady.module_test.util

/**
 * Created by mcgrady on 2/5/21.
 */
class ScreenUtil {

    fun printAspectRatio(width: Int, height: Int) {
        println("Dimensions: $width x $height")
        val gcd = gcd(width, height)
        println("Gcd: $gcd")
        println("Aspect: ${width/gcd} : ${height/gcd}")
        println("-------------------------------------")
    }

    fun gcd(width: Int, height: Int): Int {
        if (height == 0) {
            return width
        } else {
            return gcd(height, width % height)
        }
    }
}