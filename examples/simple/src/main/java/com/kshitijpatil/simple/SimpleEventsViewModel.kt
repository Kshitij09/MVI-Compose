package com.kshitijpatil.simple

import androidx.compose.runtime.emit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.kshitijpatil.baseandroid.ReduxViewModel
import com.kshitijpatil.baseandroid.data.remote.GenericNetworkResponse
import com.kshitijpatil.baseandroid.data.remote.MockApi
import com.kshitijpatil.baseandroid.data.remote.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimpleEventViewModel(
    private val api: MockApi,
) : ReduxViewModel<SimpleEventViewState, SimpleUiAction>(SimpleEventViewState()) {

    init {
        viewModelScope.launch {
            pendingActions.consumeAsFlow().collect { reduce(it) }
        }
    }

    private suspend fun reduce(action: SimpleUiAction) = when (action) {
        SimpleUiAction.GetFirstData -> fetchFirstData()
        SimpleUiAction.GetSecondData -> fetchSecondData()
    }

    private suspend fun fetchSecondData() = withContext(Dispatchers.IO) {
        setState {
            copy(secondData = Async.Loading("Loading second data"))
        }
        api.getSecondData().collect { data ->
            setState { copy(secondData = mapResponse(data)) }
        }
    }

    private suspend fun fetchFirstData() = withContext(Dispatchers.IO) {
        setState {
            copy(firstData = Async.Loading("Loading first data"))
        }
        api.getFirstData().collect { data ->
            setState { copy(firstData = mapResponse(data)) }
        }
    }

    private fun mapResponse(networkResponse: GenericNetworkResponse): Async<String> {
        return when(networkResponse) {
            is NetworkResponse.Success -> Async.Content(networkResponse.body.data)
            is NetworkResponse.ApiError -> Async.Error(networkResponse.body.error)
            is NetworkResponse.NetworkError -> Async.Error("No network!")
            is NetworkResponse.UnknownError -> Async.Error("Something went wrong")
        }
    }
}

class SimpleEventsViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SimpleEventViewModel::class.java)) {
            val api = MockApi.create()
            @Suppress("UNCHECKED_CAST")
            return SimpleEventViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed class Async<out S> {
    data class Loading(val progressMessage: String) : Async<Nothing>()
    data class Content<S>(val data: S) : Async<S>()
    data class Error(val message: String) : Async<Nothing>()
    object Undefined: Async<Nothing>()
}
