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

import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.volvocars.mediasample.R
import com.volvocars.mediasample.domain.Song

class MediaItemFactory(private val context: Context) {
    fun createBrowsableRootMediaItem(rootId: String): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setTitle(context.getString(R.string.browsable_tab_label))
            .setIsBrowsable(true)
            .setIsPlayable(false)
            .build()
        return MediaItem.Builder()
            .setMediaId(rootId)
            .setMediaMetadata(metadata)
            .build()
    }

    fun createPlayableMediaItem(song: Song, playlistSize: Int, songNumber: Int): MediaItem {
        val metadata = MediaMetadata.Builder()
            .setTitle(song.title)
            .setArtist(song.artist)
            .setArtworkUri(song.mediaArtUri.toUri())
            .setIsBrowsable(false)
            .setIsPlayable(true)
            .setTrackNumber(songNumber)
            .setTotalTrackCount(playlistSize)
            .setDisplayTitle(song.title)
            .setSubtitle(song.subtitle)
            .setDescription(song.description)
            .build()
        return MediaItem.Builder()
            .setMediaId(song.id)
            .setUri(song.mediaUri)
            .setMediaMetadata(metadata)
            .build()
    }
}
