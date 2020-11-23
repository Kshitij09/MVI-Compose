package com.kshitijpatil.baseandroid.data.remote

import androidx.compose.runtime.emit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import kotlin.random.Random

class MockApi {
    suspend fun getFirstData(): Flow<GenericNetworkResponse> = flow {
        delay(1000)
        val status = Random.nextInt(4)
        val response = emulateResponse(status) { SuccessResponse("Here is your First Data") }
        emit(response)
    }

    suspend fun getSecondData(): Flow<GenericNetworkResponse> = flow {
        delay(1000)
        val status = Random.nextInt(4)
        val response = emulateResponse(status) { SuccessResponse("Here is THE SECOND Data") }
        emit(response)
    }

    private fun emulateResponse(response: Int, onSuccess:() -> SuccessResponse): GenericNetworkResponse {
        return when (response) {
            0 -> NetworkResponse.Success(onSuccess())
            1 -> NetworkResponse.ApiError(ErrorResponse("Some API error"), 400)
            2 -> NetworkResponse.NetworkError(IOException("No Network"))
            3 -> NetworkResponse.UnknownError(Throwable("Unknown Error"))
            else -> NetworkResponse.UnknownError(Throwable("Unknown Error"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: MockApi? = null

        fun create(): MockApi =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: MockApi().also { INSTANCE = it }
            }
    }
}