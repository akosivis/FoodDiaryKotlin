package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.viselvis.fooddiarykotlin.R
import com.viselvis.fooddiarykotlin.utils.BaseTextField
import com.viselvis.fooddiarykotlin.viewmodels.EnterUsernameViewModel

@Composable
fun EnterNameRoute(
    viewModel: EnterUsernameViewModel,
    navigateToMainRoute : () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isThereUserName) {
        navigateToMainRoute()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text (
            text = stringResource(id = R.string.what_is_your_name)
        )
        BaseTextField(
            text = uiState.userName,
            onTextChanged = { viewModel.updateUserName(it) },
            placeholderText = "",
            isSingleLine = true
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(45.dp),
            onClick = {
                viewModel.saveUserName()
            }
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }
}