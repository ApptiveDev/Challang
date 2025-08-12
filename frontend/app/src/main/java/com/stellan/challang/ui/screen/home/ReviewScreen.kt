package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.ui.component.RectangularProgress
import com.stellan.challang.ui.component.StarRatingDecimal

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
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Medium,
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
            val filters = listOf("전체", "맛", "안주", "곡물", "산미", "위스키")
            val selectedFilters = remember { mutableStateListOf("전체") }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy((-7).dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 10.dp)
            ) {
                filters.forEach { filter ->
                    val isSelected = filter in selectedFilters
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            when (filter) {
                                "전체" -> {
                                    selectedFilters.clear()
                                    selectedFilters.add("전체")
                                }
                                else -> {
                                    if ("전체" in selectedFilters) {
                                        selectedFilters.remove("전체")
                                    }
                                    if (isSelected) {
                                        selectedFilters.remove(filter)
                                        if (selectedFilters.isEmpty()) {
                                            selectedFilters.add("전체")
                                        }
                                    } else {
                                        selectedFilters.add(filter)
                                    }
                                }
                            }
                        },
                        label = { Text(
                            text = filter,
                            fontSize = 12.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFDEEEEE),
                            selectedContainerColor = Color(0xFFB2DADA)
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
        }

        item {
            Column(Modifier.fillMaxWidth()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "총 72건",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                    Text(
                        "최신순/추천순",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                }
                repeat(3) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .background(Color(0xFFDEEEEE))
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, start = 20.dp, bottom = 12.dp, end = 5.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "귀여운 위스키 1234",
                                    fontSize = 13.sp
                                )
                                Spacer(Modifier.height(5.dp))
                                Row(verticalAlignment = Alignment.Bottom) {
                                    StarRatingDecimal(4f, starSize = 16.dp)
                                    Spacer(Modifier.width(5.dp))
                                    Text(
                                        "2025.04.30",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 8.sp
                                    )
                                }
                            }
                            Box(
                                Modifier.clickable(role = Role.Button, onClick = {})
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ReportProblem,
                                    contentDescription = null,
                                    tint = Color(0xFFFFA288),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, end = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                "아쉬운 점은 있었으나 가볍게 마시기 적당한 술이라고 생각. 여기에 텍스트 여기에",
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                modifier = Modifier.width(260.dp)
                            )
                            Row(
                                Modifier.width(70.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    Modifier
                                        .clickable(role = Role.Button, onClick = {})
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ThumbUp,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        "30",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                                Row(
                                    Modifier
                                        .clickable(role = Role.Button, onClick = {})
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.ThumbDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        "5",
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(15.dp))
                        Row(
                            Modifier.fillMaxWidth().padding(start = 20.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                Modifier
                                    .size(90.dp)
                                    .background(
                                        color = Color(0xFFD9D9D9),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            ) {

                            }
                            Box(
                                Modifier
                                    .size(90.dp)
                                    .background(
                                        color = Color(0xFFD9D9D9),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                            ) {

                            }
                        }
                        Spacer(Modifier.height(5.dp))
                        Row(
                            Modifier.fillMaxWidth().padding(start = 20.dp, bottom = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            repeat(3) {
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text(
                                        "위스키",
                                        fontSize = 11.sp,
                                        color = Color.Black
                                    ) },
                                    enabled = false,
                                    border = BorderStroke(width = 0.dp, Color.Transparent),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        disabledContainerColor = Color(0xFFB2DADA)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}