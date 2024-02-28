package com.viselvis.fooddiarykotlin.ui.routes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroductionRoute(
    navigateToMainRoute: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = {4})
    val coroutineScope = rememberCoroutineScope()
    val introPageList = listOf(
        IntroPage(
            0, "", "Welcome to NoteEat app!, xxxx",
            "", true
        ) { coroutineScope.launch { pagerState.scrollToPage(1)} },
        IntroPage(
            1, "", "You can list your recent food intakes here",
            "", true
        ) { coroutineScope.launch { pagerState.scrollToPage(2)} },
        IntroPage(2, "", "You can also print your food intake on a given date, xxxx",
            "The printed file can be presented to a health professional when needed", true
        ) { coroutineScope.launch { pagerState.scrollToPage(3)} },
        IntroPage(3, "", "Hope you can use this app well, xxxx!",
            "", false, navigateToMainRoute
        )
    )

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState) { page ->
            IntroPage(introPageList[page])
        }
    }
}

data class IntroPage(
    val id: Int,
    val imageUrl: String,
    val mainText: String,
    val secondText: String,
    val isNext: Boolean,
    val navigateAction: () -> Unit
)

@Composable
fun IntroPage(page: IntroPage) {
    Column(modifier = Modifier.fillMaxSize()) {
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
            onClick = page.navigateAction
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

@Preview(widthDp = 412, heightDp = 892)
@Composable
fun IntroRoutePreview() {
    IntroPage(3, "",
        "Hope you can use this app well, xxxx!",
        "", false
    ) {}
}