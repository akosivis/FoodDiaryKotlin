package com.viselvis.fooddiarykotlin.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholderText: String,
    givenModifier: Modifier = Modifier.fillMaxWidth(),
    isSingleLine: Boolean = false
) {
    TextField(
        modifier = givenModifier,
        value = text,
        onValueChange = onTextChanged,
        textStyle = TextStyle(fontSize = 16.sp),
        shape = RoundedCornerShape(16.dp),
        singleLine = isSingleLine,
        placeholder = { Text(placeholderText) },
        colors = TextFieldDefaults.textFieldColors(
            // backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
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
                text = text,
                fontSize = 16.sp
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

@Composable
fun BaseLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun BaseClickableCard (
    clickable: () -> Unit,
    name: String,
    imageVector: ImageVector? = null,
    contentDesc: String = "",
    isFixedHeight: Boolean = false,
) {
    Card (
        modifier = Modifier.clickable(onClick = clickable ),
    ) {
        Column (
            modifier = if (isFixedHeight) {
                Modifier
                    .padding(15.dp)
                    .height(130.dp)
            } else {
                Modifier.padding(15.dp)
                   },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDesc
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(name)
        }
    }
}

@Composable
fun BaseColumnItem (
    clickable: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
    itemType: Int = 0
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { clickable() },
        shape = when (itemType) {
            1 -> RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
            2 -> RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp, bottomStart = 15.dp, bottomEnd = 15.dp)
            3 -> RoundedCornerShape(0.dp)
            else -> RoundedCornerShape(15.dp)
        },
        content = content
    )
}

@Composable
fun NotWorkingDisplayPage(
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier.align (Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image (
                modifier = Modifier.height(100.dp).width(100.dp),
                imageVector = Icons.Default.Warning,
                contentDescription = "Not yet working icon"
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text (
                fontSize = 18.sp,
                text = message
            )
        }
    }
}