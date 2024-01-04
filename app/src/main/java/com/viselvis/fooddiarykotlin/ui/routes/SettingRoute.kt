package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.viselvis.fooddiarykotlin.utils.NotWorkingDisplayPage

@Composable
fun SettingsRoute() {
    Surface (
        modifier = Modifier.fillMaxSize() 
    ) {
        NotWorkingDisplayPage(message = "This feature is still being developed!")
    }
}