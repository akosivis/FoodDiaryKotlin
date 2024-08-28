package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme
import com.viselvis.fooddiarykotlin.ui.theme.md_theme_light_primary
import com.viselvis.fooddiarykotlin.utils.BaseChip
import com.viselvis.fooddiarykotlin.utils.FlowRow

@Preview(widthDp = 412, heightDp = 892)
@Composable
fun ItemDetailRoute(
    modifier: Modifier = Modifier
) {
    NoteEatTheme {
        Surface (modifier = Modifier.fillMaxSize()) {
            Column (
                modifier = modifier.padding(15.dp).fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .drawWithCache {
                            val roundedPolygon = RoundedPolygon(
                                numVertices = 6,
                                radius = size.minDimension / 2,
                                centerX = size.width / 2,
                                centerY = size.height / 2
                            )
                            val roundedPolygonPath = roundedPolygon.toPath().asComposePath()
                            onDrawBehind {
                                drawPath(roundedPolygonPath, color = md_theme_light_primary)
                            }
                        }
                        .size(50.dp, 50.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text (
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Sample title",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Contains: ",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(5.dp))
                FlowRow(
                    horizontalGap = 8.dp,
                    verticalGap = 8.dp,
                ) {
                    for (ingredient in listOf("Sample", "Sample2")) {
                        BaseChip(
                            text = ingredient,
                            clickable = {}
                        )
                    }
                }
            }
        }
    }
}