package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.ui.component.StarRating

@Composable
fun WriteReviewScreen(
    drinkId: String,
    onSubmit: () -> Unit
) {
    LazyColumn(Modifier.fillMaxWidth()) {
        item {
            Text(
                "리뷰",
                fontWeight = FontWeight.Normal,
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 30.dp, start = 30.dp, bottom = 15.dp)
            )
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 14.dp,
                color = Color(0xFFDEEEEE)
            )
        }

        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 15.dp)
            ) {
                Text(
                    "별점",
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(10.dp))
                StarRating(4, Modifier.align(Alignment.CenterHorizontally))
            }
            Spacer(Modifier.height(10.dp))
        }

        item {
            var review by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(""))
            }
            val minLen = 5
            val maxLen = 300
            val scroll = rememberScrollState()

            LaunchedEffect(review.text, review.selection) {
                val caretAtEnd = review.selection.collapsed &&
                        review.selection.end == review.text.length
                if (caretAtEnd) {
                    scroll.animateScrollTo(scroll.maxValue)
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                Text(
                    "리뷰 작성",
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(Color(0xFFDEEEEE))
                ) {
                    BasicTextField(
                        value = review,
                        onValueChange = { value ->
                            review = if (value.text.length <= maxLen) value
                            else value.copy(
                                text = value.text.take(maxLen),
                                selection = TextRange(maxLen)
                            ) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 38.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        decorationBox = { innerTextField ->
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scroll)
                            ) {
                                if (review.text.isEmpty()) {
                                    Text(
                                        "리뷰를 입력하세요",
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF868686),
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    Text(
                        "${review.text.length}/$maxLen",
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    )
                }
                Text(
                    "최소 ${minLen}자 최대 ${maxLen}자",
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = if (review.text.length < minLen) Color(0xFFD9534F)
                    else Color(0xFF868686),
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}