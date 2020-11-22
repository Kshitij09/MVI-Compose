package com.kshitijpatil.mvicompose.data.remote

import androidx.compose.runtime.emit
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockApi {
    suspend fun getFirstData(): Flow<String> = flow {
        delay(1000)
        emit("Here is your first Data")
    }

    suspend fun getSecondData(): Flow<String> = flow {
        delay(1000)
        emit("Here is THE SECOND Data")
    }
}