package com.kshitijpatil.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kshitijpatil.baseandroid.ui.MVIComposeTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach

@OptIn(ExperimentalCoroutinesApi::class)
class SimpleEventFragment: Fragment() {
    private val pendingActions = Channel<SimpleUiAction>(Channel.BUFFERED)
    private val viewModel: SimpleEventViewModel by viewModels(factoryProducer = { SimpleEventsViewModelFactory() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            // Intercept action here if need to
            pendingActions.consumeEach { viewModel.submitAction(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = ComposeView(requireContext()).apply {
        layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        setContent {
            MVIComposeTheme {
                // A surface container using the 'background' color from the theme
                val viewState by viewModel.state.collectAsState()
                Surface(color = MaterialTheme.colors.background) {
                    SimpleEventUi(
                        viewState = viewState,
                        actioner = { pendingActions.offer(it) },
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
    }
}