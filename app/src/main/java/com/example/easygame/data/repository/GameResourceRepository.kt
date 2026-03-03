package com.example.easygame.data.repository

import android.content.Context
import coil3.ImageLoader
import coil3.request.ErrorResult
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import com.example.easygame.domain.model.GameObject
import java.io.File

class GameResourceRepository(private val context: Context, private val imageLoader: ImageLoader) {

    suspend fun saveGameObject(gameObject: GameObject): String {
        val downloadUrl = gameObject.source ?: throw Throwable(EMPTY_URL_ERROR)
        val request = ImageRequest.Builder(context).data(downloadUrl).build()
        when (val result = imageLoader.execute(request)) {
            is SuccessResult ->
                return saveFileToStorage(result, gameObject.name)
            is ErrorResult -> {
                throw result.throwable
            }
        }
    }

    private fun saveFileToStorage(result: SuccessResult, name: String): String {
        val key = result.diskCacheKey ?: throw Throwable(EMPTY_DISK_CACHE_KEY_ERROR)
        val diskCacheFile = imageLoader.diskCache
            ?.openSnapshot(key)
            ?.data?.toFile()
            ?: throw Throwable(CAN_NOT_GET_DOWNLOADED_FILE_ERROR)
        val localFile = File(context.filesDir, "$name.svg")
        diskCacheFile.copyTo(localFile, true)
        return localFile.path
    }

    private companion object {
        const val EMPTY_URL_ERROR = "Empty url error"
        const val EMPTY_DISK_CACHE_KEY_ERROR = "Empty disk cache key error"
        const val CAN_NOT_GET_DOWNLOADED_FILE_ERROR = "Can not get downloaded file error"
    }
}
