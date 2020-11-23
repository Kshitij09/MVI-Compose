package com.kshitijpatil.mvicompose.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExampleList(examples: List<String>) {
    LazyColumnFor(
        items = examples,
        contentPadding = PaddingValues(start = 8.dp, bottom = 12.dp),
        modifier = Modifier.padding(8.dp)
    ) { example ->
        ExampleRow(title = example) {

        }
    }
}

@Composable
fun ExampleRow(title: String, onClick: (String) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { onClick(title) })
    ) {
        Text(title, style = MaterialTheme.typography.h5)
        Divider()
    }
}