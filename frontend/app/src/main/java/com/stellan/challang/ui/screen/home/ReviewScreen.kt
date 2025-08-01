package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.ui.component.RectangularProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    drinkId: String,
    onWriteReview: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Text(
                "리뷰",
                fontWeight = FontWeight.Normal,
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 30.dp, start = 30.dp, bottom = 15.dp)
            )
        }

        item {
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 14.dp,
                color = Color(0xFFDEEEEE)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, start = 30.dp, end = 30.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier.wrapContentHeight().wrapContentWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "9.6",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(Modifier.width(20.dp))
                    Column(
                        Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        listOf("단맛" to 0.95f, "산미" to 0.88f, "과실향" to 0.81f).forEach {
                            (label, frac) ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .width(60.dp)
                                        .wrapContentHeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        label,
                                        fontWeight = FontWeight.Normal,
                                        textAlign = TextAlign.Center
                                    )
                                }
                                RectangularProgress(fraction = frac)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(18.dp))
                Button(
                    onClick = { onWriteReview() },
                    modifier = Modifier.width(250.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2DADA))
                ) {
                    Text(
                        "후기작성",
                        color = Color.Black
                    )
                }
            }
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 14.dp,
                color = Color(0xFFDEEEEE)
            )
        }

        item {
            FlowRow(

            ) {

            }
        }
    }
}