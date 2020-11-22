package com.kshitijpatil.simpleflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import com.kshitijpatil.mvicompose.ui.MVIComposeTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class SimpleEventActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SimpleEventViewModel = viewModel()
            val viewState by viewModel.state.collectAsState()
            MVIComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SimpleEventUi(
                        viewState = viewState,
                        firstStateContent = { DataScreen(it) },
                        secondStateContent = { DataScreen(it) }
                    )
                }
            }
        }
    }
}
