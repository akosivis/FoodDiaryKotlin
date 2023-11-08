package com.viselvis.fooddiarykotlin.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun BaseTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholderText: String
) {

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        onValueChange = onTextChanged,
        shape = RoundedCornerShape(16.dp),
        placeholder = { Text(placeholderText) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
    )
}