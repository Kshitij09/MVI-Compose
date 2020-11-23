package com.kshitijpatil.simple

import androidx.compose.runtime.Immutable

@Immutable
data class SimpleEventViewState(
    val firstData: Async<String> = Async.Undefined,
    val secondData: Async<String> = Async.Undefined,
    val secondPageUnlocked: Boolean = false
)