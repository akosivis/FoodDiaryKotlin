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
            Row (modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(
                    text = "Set reminder",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Row (modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                Text(
                    text = "Set dark theme",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}