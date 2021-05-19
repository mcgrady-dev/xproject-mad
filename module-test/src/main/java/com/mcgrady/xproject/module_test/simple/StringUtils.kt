@file: JvmName("StringUtilsFormJvmName")

package com.mcgrady.xproject.module_test.simple

/**
 * Created by mcgrady on 5/1/21.
 */

fun isEmpty(str: String): Boolean {
    return str == ""
}

object StringUtilsFormINSTANCE {
    fun isEmpty(str: String): Boolean {
        return str == ""
    }
}

object StringUtilsFormJvmStatic {
    @JvmStatic
    fun isEmpty(str: String): Boolean {
        return str == ""
    }
}


class StringUtilsFormCompObj {
    companion object {
        fun isEmpty(str: String): Boolean {
            return "" == str
        }
    }
}