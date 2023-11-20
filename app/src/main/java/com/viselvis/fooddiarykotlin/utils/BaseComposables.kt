package com.viselvis.fooddiarykotlin.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

@Composable
fun BaseTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholderText: String,
    givenModifier: Modifier =  Modifier.fillMaxWidth()
) {
    TextField(
        modifier = givenModifier,
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

@Composable
fun FlowRow(
    horizontalGap: Dp = 0.dp,
    verticalGap: Dp = 0.dp,
    alignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable () -> Unit,
) = Layout(content = content) { measurables, constraints ->
    val horizontalGapPx = horizontalGap.toPx().roundToInt()
    val verticalGapPx = verticalGap.toPx().roundToInt()

    val rows = mutableListOf<Row>()
    var rowConstraints = constraints
    var rowPlaceables = mutableListOf<Placeable>()

    measurables.forEach { measurable ->
        val placeable = measurable.measure(Constraints())
        if (placeable.measuredWidth !in rowConstraints.minWidth..rowConstraints.maxWidth) {
            rows += Row(rowPlaceables, horizontalGapPx)
            rowConstraints = constraints
            rowPlaceables = mutableListOf()
        }
        val consumedWidth = placeable.measuredWidth + horizontalGapPx
        rowConstraints = rowConstraints.offset(horizontal = -consumedWidth)
        rowPlaceables.add(placeable)
    }
    rows += Row(rowPlaceables, horizontalGapPx)

    val width = constraints.maxWidth
    val height = (rows.sumOf { row -> row.height } + (rows.size - 1) * verticalGapPx)
        .coerceAtMost(constraints.maxHeight)

    layout(width, height) {
        var y = 0
        rows.forEach { row ->
            val offset = alignment.align(row.width, width, layoutDirection)
            var x = offset
            row.placeables.forEach { placeable ->
                placeable.placeRelative(x, y)
                x += placeable.width + horizontalGapPx
            }
            y += row.height + verticalGapPx
        }
    }
}

private class Row(
    val placeables: List<Placeable>,
    val horizontalGapPx: Int,
) {
    val width by lazy(mode = LazyThreadSafetyMode.NONE) {
        placeables.sumBy { it.width } + (placeables.size - 1) * horizontalGapPx
    }

    val height by lazy(mode = LazyThreadSafetyMode.NONE) {
        placeables.maxOfOrNull { it.height } ?: 0
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseChip (
    text: String,
    clickable: () -> Unit,
//    contentColor: Color = Color.,
//    contentColor: Color = Color.White
) {
    InputChip (
        onClick = clickable,
        selected = true,
        label = {
            Text (
                text = text
            )
        },
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Close this chip",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        },
    )

}