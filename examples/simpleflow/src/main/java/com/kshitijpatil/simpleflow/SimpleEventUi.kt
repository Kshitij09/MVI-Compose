package com.kshitijpatil.simpleflow

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.zIndex
import androidx.ui.tooling.preview.Preview
import com.kshitijpatil.mvicompose.ui.MVIComposeTheme

private const val untouchedMessage = "UI is Untouched"

@Composable
fun SimpleEventUi(
    viewState: SimpleEventViewState,
    firstStateContent: @Composable (String) -> Unit,
    secondStateContent: @Composable (String) -> Unit,
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
                    viewState.firstData ?: untouchedMessage
                )
            }
            Box(modifier = boxModifier) {
                secondStateContent(
                    viewState.secondData ?: untouchedMessage
                )
            }
        }
        Row(
            modifier = Modifier.weight(0.2f).fillMaxWidth().wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            Button(onClick = {}) {
                Text("Load First Data")
            }
            Spacer(modifier = Modifier.width(32.dp))
            Button(onClick = {}) {
                Text("Load Second Data")
            }
        }
    }
}


@Composable
fun DataScreen(data: String) {
    Text(
        data,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.fillMaxSize().wrapContentSize(),
    )
}

@Preview
@Composable
fun DataScreenPreview() {
    val viewState = SimpleEventViewState(untouchedMessage, untouchedMessage)

    MVIComposeTheme {
        Surface(color = MaterialTheme.colors.background) {
            SimpleEventUi(
                viewState = viewState,
                firstStateContent = { DataScreen(it) },
                secondStateContent = { DataScreen(it) }
            )
        }
    }
}