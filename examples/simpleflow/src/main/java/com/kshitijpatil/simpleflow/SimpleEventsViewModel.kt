package com.kshitijpatil.simpleflow

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.kshitijpatil.mvicompose.base.ReduxViewModel
import com.kshitijpatil.mvicompose.data.remote.MockApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimpleEventViewModel(
    private val api: MockApi
) : ReduxViewModel<SimpleEventViewState>(SimpleEventViewState()) {
    private val pendingActions = Channel<SimpleUiAction>(Channel.BUFFERED)

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
        api.getSecondData().collect { nextState ->
            setState { copy(secondData = nextState) }
        }
    }

    private suspend fun fetchFirstData() = withContext(Dispatchers.IO) {
        api.getFirstData().collect { nextState ->
            setState { copy(firstData = nextState) }
        }
    }
}