package com.kshitijpatil.simpleflow

sealed class SimpleUiAction {
    object GetFirstData: SimpleUiAction()
    object GetSecondData: SimpleUiAction()
}