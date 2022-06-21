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
package com.mcgrady.xproject.retromusic.helper

/**
 * Simple thread safe stop watch.
 *
 * @author Karim Abou Zeid (kabouzeid)
 */
class StopWatch {

    /**
     * The time the stop watch was last started.
     */
    private var startTime: Long = 0

    /**
     * The time elapsed before the current [.startTime].
     */
    private var previousElapsedTime: Long = 0

    /**
     * Whether the stop watch is currently running or not.
     */
    private var isRunning: Boolean = false

    /**
     * @return the total elapsed time in milliseconds
     */
    val elapsedTime: Long
        get() = synchronized(this) {
            var currentElapsedTime: Long = 0
            if (isRunning) {
                currentElapsedTime = System.currentTimeMillis() - startTime
            }
            return previousElapsedTime + currentElapsedTime
        }

    /**
     * Starts or continues the stop watch.
     *
     * @see .pause
     * @see .reset
     */
    fun start() {
        synchronized(this) {
            startTime = System.currentTimeMillis()
            isRunning = true
        }
    }

    /**
     * Pauses the stop watch. It can be continued later from [.start].
     *
     * @see .start
     * @see .reset
     */
    fun pause() {
        synchronized(this) {
            previousElapsedTime += System.currentTimeMillis() - startTime
            isRunning = false
        }
    }

    /**
     * Stops and resets the stop watch to zero milliseconds.
     *
     * @see .start
     * @see .pause
     */
    fun reset() {
        synchronized(this) {
            startTime = 0
            previousElapsedTime = 0
            isRunning = false
        }
    }

    override fun toString(): String {
        return String.format("%d millis", elapsedTime)
    }
}
