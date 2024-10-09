package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.viselvis.fooddiarykotlin.utils.NotWorkingDisplayPage

@Composable
fun SettingsRoute() {
    Surface (
        modifier = Modifier.fillMaxSize() 
    ) {
        // NotWorkingDisplayPage(message = "This feature is still being developed!")

        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            SettingsItem("Set reminder")
            SettingsItem("Set dark theme")
        }
    }
}

@Composable
fun SettingsItem (
    text: String,
    itemNumber: Int = 0
) {
    Row (modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )


    }
}