package com.mcgrady.xproject.testing.kotlin

/**
 * Created by mcgrady on 2022/1/4.
 */

// 方式一
object WorkSingleton1

class WorkSingleton private constructor() {

    companion object {

        // 方式二
        @JvmStatic
        val INSTANCE1 by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { WorkSingleton() }

        // 方式三 默认就是 LazyThreadSafetyMode.SYNCHRONIZED，可以省略不写，如下所示
        @JvmStatic
        val INSTANCE2 by lazy { WorkSingleton() }
    }
}