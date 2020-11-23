package com.kshitijpatil.baseandroid.data.remote

import java.io.IOException

/**
 * Either sealed class returning either Success [S] or Error [E]
 */
sealed class NetworkResponse<out S, out E> {
    /**
     * Success response with body
     */
    data class Success<T>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with body
     */
    data class ApiError<E>(val body: E, val code: Int) : NetworkResponse<Nothing, E>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException) : NetworkResponse<Nothing, Nothing>()

    /**
     * For example, json parsing error
     */
    data class UnknownError(val error: Throwable?) : NetworkResponse<Nothing, Nothing>()

}

data class SuccessResponse(
    val data: String
)

data class ErrorResponse(
    val error: String
)

typealias GenericNetworkResponse = NetworkResponse<SuccessResponse, ErrorResponse>