package com.kshitijpatil.simple

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.viewModel
import androidx.fragment.app.viewModels
import androidx.navigation.compose.*
import com.kshitijpatil.simple.Screen.FirstPage
import com.kshitijpatil.simple.Screen.SecondPage
import kotlinx.coroutines.ExperimentalCoroutinesApi

private sealed class Screen(val route: String) {
    object FirstPage: Screen("first")
    object SecondPage: Screen("second")
}

@Immutable
data class SecondPageState(
    val setPageUnlocked: (Boolean) -> Unit,
    val retryAction: emptyLambda
)

interface NavClickListener {
    fun onNavClicked()
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun SecondNavHost(setPageUnlocked: (Boolean) -> Unit,
                  retryAction: emptyLambda) {
    val navController = rememberNavController()
    val viewModel: SimpleEventViewModel = viewModel(factory = SimpleEventsViewModelFactory())
    val viewState by viewModel.state.collectAsState()
    NavHost(navController, startDestination = FirstPage.route) {
        composable(FirstPage.route) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.arguments?.getString(KEY_ROUTE)
            setPageUnlocked(false)

            FirstPageContent(listener = object: NavClickListener {
                override fun onNavClicked() {
                    navController.popBackStack(navController.graph.startDestination,false)

                    if (currentRoute != SecondPage.route) {
                        navController.navigate(SecondPage.route)
                    }
                }
            })
        }
        composable(SecondPage.route) {
            setPageUnlocked(true)
            DataScreen(state = viewState.secondData, retryAction = { retryAction() })
        }
    }
}


@Composable
fun FirstPageContent(listener: NavClickListener) {

    Box(modifier = Modifier.centerContent()) {
        Button(onClick = { listener.onNavClicked() }) {
            Text("Unlock Content")
        }
    }
}