package com.kshitijpatil.simpleflow

import androidx.compose.runtime.Immutable

@Immutable
data class SimpleEventViewState(
    val firstData: String? = null,
    val secondData: String? = null
)