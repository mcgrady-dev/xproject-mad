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
package com.mcgrady.xproject.retromusic.appwidgets

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mcgrady.xproject.retromusic.service.MusicService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val widgetManager = AppWidgetManager.getInstance(context)

        // Start music service if there are any existing widgets
        if (widgetManager.getAppWidgetIds(
                ComponentName(
                        context, AppWidgetBig::class.java
                    )
            ).isNotEmpty() || widgetManager.getAppWidgetIds(
                    ComponentName(
                            context, AppWidgetClassic::class.java
                        )
                ).isNotEmpty() || widgetManager.getAppWidgetIds(
                    ComponentName(
                            context, AppWidgetSmall::class.java
                        )
                ).isNotEmpty() || widgetManager.getAppWidgetIds(
                    ComponentName(
                            context, AppWidgetCard::class.java
                        )
                ).isNotEmpty()
        ) {
            val serviceIntent = Intent(context, MusicService::class.java)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // not allowed on Oreo
                context.startService(serviceIntent)
            }
        }
    }
}
