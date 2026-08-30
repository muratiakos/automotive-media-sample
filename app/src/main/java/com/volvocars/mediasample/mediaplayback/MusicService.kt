/*
 * Copyright 2022 Volvo Car Corporation
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
package com.volvocars.mediasample.mediaplayback

import android.app.PendingIntent
import androidx.media3.common.Player
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaSession
import com.volvocars.mediasample.common.logging.logd
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MusicService : MediaLibraryService() {
    private val mediaLibrary: MediaLibrary by inject()
    private val mediaPlayer: Player by inject { parametersOf(this) }
    private lateinit var mediaLibrarySession: MediaLibrarySession

    override fun onCreate() {
        super.onCreate()

        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
                packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                    PendingIntent.getActivity(this, 0, sessionIntent, PendingIntent.FLAG_IMMUTABLE)
                }

        val callback = PlaybackPreparer(mediaLibrary)

        val builder = MediaLibrarySession.Builder(this, mediaPlayer, callback)
        sessionActivityPendingIntent?.let {
            builder.setSessionActivity(it)
        }
        mediaLibrarySession = builder.build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaLibrarySession =
        mediaLibrarySession

    override fun onDestroy() {
        logd("service destroyed!")
        mediaLibrarySession.run {
            player.release()
            release()
        }
        super.onDestroy()
    }
}
