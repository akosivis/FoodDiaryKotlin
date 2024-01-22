package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroductionRoute() {
    val pagerState = rememberPagerState(pageCount = {4})
    val introPageList = listOf(
        IntroPage(0, "", "Welcome to NoteEat app!, xxxx", "", true),
        IntroPage(1, "", "You can list your recent food intakes here", "", true),
        IntroPage(2, "", "You can also print your food intake on a given date, xxxx", "The printed file can be presented to a health professional when needed", true),
        IntroPage(3, "", "Hope you can use this app well, xxxx!", "", false)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            IntroPage(introPageList[page - 1])
        }
    }
}

data class IntroPage(
    val id: Int,
    val imageUrl: String,
    val mainText: String,
    val secondText: String,
    val isNext: Boolean
)

@Composable
fun IntroPage(page: IntroPage) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Image

        Text(
            text = page.mainText,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = page.secondText,
            fontSize = 16.sp
        )
        Button(
            modifier = Modifier.align(alignment = Alignment.End),
            onClick = {}
        ) {
            Text(
                text =
                if (page.isNext) {
                    "Next"
                } else {
                    "Done"
                }
            )
        }
    }
}