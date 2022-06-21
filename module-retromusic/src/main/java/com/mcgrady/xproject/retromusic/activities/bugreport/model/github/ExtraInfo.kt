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
package com.mcgrady.xproject.retromusic.activities.bugreport.model.github

class ExtraInfo {
    private val extraInfo: MutableMap<String, String> = LinkedHashMap()
    fun put(key: String, value: String) {
        extraInfo[key] = value
    }

    fun put(key: String, value: Boolean) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Double) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Float) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Long) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Int) {
        extraInfo[key] = value.toString()
    }

    fun put(key: String, value: Any) {
        extraInfo[key] = value.toString()
    }

    fun remove(key: String) {
        extraInfo.remove(key)
    }

    fun toMarkdown(): String {
        if (extraInfo.isEmpty()) {
            return ""
        }
        val output = StringBuilder()
        output.append(
            """
    Extra info:
    ---
    <table>

            """.trimIndent()
        )
        for (key in extraInfo.keys) {
            output
                .append("<tr><td>")
                .append(key)
                .append("</td><td>")
                .append(extraInfo[key])
                .append("</td></tr>\n")
        }
        output.append("</table>\n")
        return output.toString()
    }
}
