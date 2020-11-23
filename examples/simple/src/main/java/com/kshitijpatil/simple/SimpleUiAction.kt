package com.kshitijpatil.simple

sealed class SimpleUiAction {
    object GetFirstData: SimpleUiAction()
    object GetSecondData: SimpleUiAction()
}