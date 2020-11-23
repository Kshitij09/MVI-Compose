package com.kshitijpatil.simple

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.kshitijpatil.baseandroid.ui.MVIComposeTheme
import com.kshitijpatil.simple.SimpleUiAction.GetFirstData
import com.kshitijpatil.simple.SimpleUiAction.GetSecondData

private const val untouchedMessage = "UI is Untouched"

private typealias emptyLambda = () -> Unit

fun Modifier.centerContent() = this.fillMaxSize().wrapContentSize()

@Composable
fun SimpleEventUi(
    viewState: SimpleEventViewState,
    actioner: (SimpleUiAction) -> Unit,
    firstStateContent: @Composable (Async<String>, emptyLambda) -> Unit,
    secondStateContent: @Composable (Async<String>, emptyLambda) -> Unit,
) {
    Column {
        Column(modifier = Modifier.weight(0.8f)) {
            val boxModifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .weight(1f)
                .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            Box(modifier = boxModifier) {
                firstStateContent(
                    viewState.firstData
                ) { actioner(GetFirstData) }
            }
            Box(modifier = boxModifier) {
                secondStateContent(
                    viewState.secondData
                ) { actioner(GetSecondData) }
            }
        }
        Row(
            modifier = Modifier.weight(0.2f).centerContent(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            Button(onClick = { actioner(GetFirstData) }) {
                Text("Load First Data")
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(onClick = { actioner(GetSecondData) }) {
                Text("Load Second Data")
            }
        }
    }
}


@Composable
fun DataScreen(state: Async<String>, retryAction: () -> Unit) {
    val data = when (state) {
        is Async.Undefined -> untouchedMessage
        is Async.Content -> state.data
        else -> untouchedMessage
    }
    when (state) {
        is Async.Content, is Async.Undefined -> {
            Text(
                data,
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center,
                modifier = Modifier.centerContent(),
            )
        }
        is Async.Error -> {
            ErrorView(state.message) { retryAction() }
        }
        is Async.Loading -> {
            LoadingView(state.progressMessage)
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.centerContent()
    ) {
        Text(
            message,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.error
        )
        Button(onClick = { onRetry() }) {
            Text("Try Again")
        }
    }
}

@Composable
fun LoadingView(progressMessage: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.centerContent()
    ) {
        CircularProgressIndicator()
        Text(
            progressMessage,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Preview
@Composable
fun DataScreenPreview() {
    val viewState =
        SimpleEventViewState(Async.Content(untouchedMessage), Async.Content(untouchedMessage))

    MVIComposeTheme {
        Surface(color = MaterialTheme.colors.background) {
            SimpleEventUi(
                viewState = viewState,
                actioner = {},
                firstStateContent = { uiState, retryAction ->
                    DataScreen(uiState, retryAction)
                },
                secondStateContent = { uiState, retryAction ->
                    DataScreen(uiState, retryAction)
                }
            )
        }
    }
}