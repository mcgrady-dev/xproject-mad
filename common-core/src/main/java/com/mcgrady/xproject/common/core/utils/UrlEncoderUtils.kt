/*
 * Copyright 2022 mcgrady
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mcgrady.xproject.common.core.utils

/**
 * Created by mcgrady on 2019/4/26.
 */
object UrlEncoderUtils {

    /**
     * 判断 str 是否已经 URLEncoder.encode() 过
     * 经常遇到这样的情况, 拿到一个 URL, 但是搞不清楚到底要不要 URLEncoder.encode()
     * 不做 URLEncoder.encode() 吧, 担心出错, 做 URLEncoder.encode() 吧, 又怕重复了
     *
     * @param str 需要判断的内容
     * @return 返回 `true` 为被 URLEncoder.encode() 过
     */
    fun hasUrlEncoded(str: String): Boolean {
        var encode = false
        for (i in str.indices) {
            val c = str[i]
            if (c == '%' && i + 2 < str.length) {
                // 判断是否符合urlEncode规范
                val c1 = str[i + 1]
                val c2 = str[i + 2]
                if (isValidHexChar(c1) && isValidHexChar(c2)) {
                    encode = true
                    break
                } else {
                    break
                }
            }
        }
        return encode
    }

    /**
     * 判断 c 是否是 16 进制的字符
     *
     * @param c 需要判断的字符
     * @return 返回 `true` 为 16 进制的字符
     */
    private fun isValidHexChar(c: Char): Boolean {
        return c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'
    }
}
