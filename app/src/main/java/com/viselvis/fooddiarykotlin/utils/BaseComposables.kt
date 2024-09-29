package com.viselvis.fooddiarykotlin.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.viselvis.fooddiarykotlin.R
import kotlin.math.roundToInt

val TAG = "NoteEat"

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
    clickable: (() -> Unit)? = null,
) {
    if (clickable != null) {
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
    } else {
        InputChip (
            onClick = {},
            selected = true,
            label = {
                Text (
                    text = text,
                    fontSize = 16.sp
                )
            }
        )
    }
}

@Composable
fun BaseLoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5f))
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun BaseClickableCard (
    modifierAddtl: Modifier = Modifier,
    clickable: () -> Unit,
    name: String,
    imageVector: ImageVector? = null,
    contentDesc: String = "",
    isFixedHeight: Boolean = false,
) {
    Card (
        modifier = Modifier.clickable(onClick = clickable).then(modifierAddtl),
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
fun FoodTypeItemClickable (
    clickable: () -> Unit,
    name: String,
    iconId: Int?,
    contentDesc: String = "",
    isFixedHeight: Boolean = false,
) {
    Card (
        modifier = Modifier.clickable(onClick = clickable),
    ) {
        Column (
            modifier = if (isFixedHeight) {
                Modifier
                    .padding(15.dp)
                    .height(130.dp)
                    .fillMaxWidth()
            } else {
                Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
            },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (iconId != null) {
                Image(
                    modifier = Modifier.size(40.dp, 40.dp),
                    painter = painterResource(id = iconId),
                    contentDescription = contentDesc
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(name)
        }
    }
}

@Composable
fun BaseItemClickable(
    clickable: () -> Unit,
    name: String,
    iconId: Int?,
    contentDesc: String = "",
    isFixedHeight: Boolean = false,
) {
    Column (
        modifier = if (isFixedHeight) {
            Modifier
                .padding(15.dp)
                .height(130.dp)
                .fillMaxWidth()
                .clickable(onClick = clickable)
        } else {
            Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .clickable(onClick = clickable)
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (iconId != null) {
            Image(
                modifier = Modifier.size(40.dp, 40.dp),
                painter = painterResource(id = iconId),
                contentDescription = contentDesc
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text(name)
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
                modifier = Modifier
                    .height(100.dp)
                    .width(100.dp),
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

@Composable
fun BaseDialog(
    onDismiss: () -> Unit,
    message: String = "",   // stringResource(R.string.item_added_successfully)
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(id = R.string.close))
            }
        }
    )
}

@Composable
fun BaseListItemCard(
    modifierAddtl: Modifier = Modifier,
    clickable: () -> Unit,
    text: String,
    imageVector: ImageVector? = null,
    contentDesc: String = "",
) {
    Card (
        modifier = Modifier.clickable(onClick = clickable).then(modifierAddtl),
    ) {
        Column (
            Modifier.padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDesc
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text (
                text = text,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTextFieldWithoutBg(
    text: String,
    onTextChanged: (String) -> Unit,
    placeholderText: String,
    givenModifier: Modifier = Modifier.fillMaxWidth(),
    inputTextStyle: TextStyle = LocalTextStyle.current,
    isSingleLine: Boolean = false,
    isTextCenter: Boolean = false,
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val visualTransformation = VisualTransformation.None

    BasicTextField(
        modifier = givenModifier,
        value = text,
        onValueChange = onTextChanged,
        // MaterialTheme.typography.headlineSmall
        textStyle = inputTextStyle
            .copy(textAlign = if (isTextCenter) {
                TextAlign.Center
            } else {
                TextAlign.Start
            }),
        singleLine = isSingleLine,
        interactionSource = interactionSource,
        visualTransformation = visualTransformation,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = text,
                placeholder = {
                    Text(
                        text = placeholderText,
                        textAlign = if (isTextCenter)
                            TextAlign.Center
                        else
                            TextAlign.Start
                    )},
                enabled = true,
                singleLine = isSingleLine,
                interactionSource = interactionSource,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
            )
        }
    )
}