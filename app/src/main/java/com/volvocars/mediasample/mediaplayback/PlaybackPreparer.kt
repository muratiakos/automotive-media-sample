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

import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media.utils.MediaConstants
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.LibraryResult
import androidx.media3.session.MediaLibraryService.LibraryParams
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import androidx.media3.session.MediaSession
import com.google.common.collect.ImmutableList
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.volvocars.mediasample.common.logging.logd
import com.volvocars.mediasample.common.logging.loge

class PlaybackPreparer(
    private val mediaLibrary: MediaLibrary
) : MediaLibrarySession.Callback {

    @OptIn(UnstableApi::class)
    override fun onGetLibraryRoot(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        params: LibraryParams?
    ): ListenableFuture<LibraryResult<MediaItem>> {
        val rootExtras = params?.extras ?: Bundle()
        rootExtras.apply {
            putInt(
                MediaConstants.DESCRIPTION_EXTRAS_KEY_CONTENT_STYLE_BROWSABLE,
                MediaConstants.DESCRIPTION_EXTRAS_VALUE_CONTENT_STYLE_GRID_ITEM
            )
            putInt(
                MediaConstants.DESCRIPTION_EXTRAS_KEY_CONTENT_STYLE_PLAYABLE,
                MediaConstants.DESCRIPTION_EXTRAS_VALUE_CONTENT_STYLE_GRID_ITEM
            )
        }
        val libraryParams = LibraryParams.Builder().setExtras(rootExtras).build()
        val rootMetadata = MediaMetadata.Builder()
            .setIsBrowsable(true)
            .setIsPlayable(false)
            .build()
        val rootItem = MediaItem.Builder()
            .setMediaId(BROWSABLE_ROOT_ID)
            .setMediaMetadata(rootMetadata)
            .build()
        return Futures.immediateFuture(LibraryResult.ofItem(rootItem, libraryParams))
    }

    override fun onGetChildren(
        session: MediaLibrarySession,
        browser: MediaSession.ControllerInfo,
        parentId: String,
        page: Int,
        pageSize: Int,
        params: LibraryParams?
    ): ListenableFuture<LibraryResult<ImmutableList<MediaItem>>> {
        val children = mediaLibrary.getPlaylistById(parentId) ?: emptyList()
        return Futures.immediateFuture(LibraryResult.ofItemList(ImmutableList.copyOf(children), params))
    }

    override fun onAddMediaItems(
        mediaSession: MediaSession,
        controller: MediaSession.ControllerInfo,
        mediaItems: MutableList<MediaItem>
    ): ListenableFuture<MutableList<MediaItem>> {
        val updatedMediaItems = mediaItems.map { mediaItem ->
            if (mediaItem.mediaId != MediaItem.DEFAULT_MEDIA_ID && mediaItem.requestMetadata.mediaUri == null) {
                mediaLibrary.getSongById(mediaItem.mediaId) ?: mediaItem
            } else {
                mediaItem
            }
        }.toMutableList()
        return Futures.immediateFuture(updatedMediaItems)
    }
}
