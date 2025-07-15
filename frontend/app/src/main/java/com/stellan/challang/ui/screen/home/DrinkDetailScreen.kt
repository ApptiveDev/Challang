package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.R
import com.stellan.challang.ui.component.StarRating
import com.stellan.challang.ui.component.StarRatingDecimal
import com.stellan.challang.ui.theme.PaperlogyFamily
import kotlin.math.roundToInt

@Composable
fun DrinkDetailScreen(
    drinkId: String,
    onViewReviews: () -> Unit
) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            val startColor = Color(0xFF6CD0D8)
            val lightEndColor = lerp(startColor, Color.White, 0.95f)

            Box(
                Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(colors = listOf(startColor, lightEndColor)),
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Image(
                    painter = painterResource(R.drawable.balvenie),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth().padding(top = 50.dp, bottom = 70.dp)
                )
            }
        }

        item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 15.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "발베니",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    "위스키 40도",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = Color(0xFF938F8F)
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFFF7B7B),
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        item {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy((-3).dp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
            ) {
                repeat(11) {
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = { Text(
                            "바닐라향",
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) },
                        border = BorderStroke(0.dp, Color.Transparent),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFFCEEFF2)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.width(80.dp)
                    )
                }
            }
            Spacer(Modifier.height(30.dp))
        }

        item {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .background(Color(0xFFCEEFF2), shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    "짐빔(Jim Beam)은 미국 켄터키주에서 생산되는 대표적인 버번 위스키에요." +
                        "1795년부터 이어져 온 전통과 독자적인 제조법으로 깊고 부드러운 풍미를 자랑하죠." +
                        "오크통에서 숙성된 특유의 풍미가 세계적으로 사랑받고 있으며," +
                        "다양한 칵테일 베이스로도 널리 활용된답니다.",
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    lineHeight = 30.sp,
                    color = Color(0xFF5B5858),
                    modifier = Modifier.padding(15.dp)
                )
            }
            Spacer(Modifier.height(15.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .background(Color(0xFFCEEFF2), shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    "테이스팅 노트",
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp)
                )
                Text(
                    "짐빔은 은은한 바닐라와 캐러멜 향을 중심으로, 고소한 옥수수 곡물 향과 오크 나무 숙성에서 비롯된" +
                            " 부드럽고 깊이 있는 풍미가 느껴지는 버번 위스키입니다." +
                        "적당한 단맛과 함께 살짝 스파이시한 느낌이 균형을 이루어, 전반적으로 깔끔하고 산뜻한 인상을 줍니다." +
                        "맛이 무겁거나 강하지 않아 초심자들도 부담 없이 즐기기 좋으며," +
                        "스트레이트, 온더록, 하이볼, 칵테일 등 다양한 방식으로 활용할 수 있는 활용도 높은 위스키입니다.",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF5B5858),
                    modifier = Modifier.padding(15.dp)
                )
            }
            Spacer(Modifier.height(30.dp))
        }

        item {
            val degree = 40f
            val valueRange = 0f..60f

            var sliderWidthPx by remember { mutableIntStateOf(0) }
            val thumbRadiusPx = with(LocalDensity.current) { 10.dp.toPx() }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .onGloballyPositioned { coords ->
                        sliderWidthPx = coords.size.width
                    }
            ) {
                Slider(
                    value = degree,
                    onValueChange = {},
                    valueRange = valueRange,
                    enabled = false,
                    colors = SliderDefaults.colors(
                        disabledActiveTrackColor = Color(0xFF6CD0D8),
                        disabledInactiveTrackColor = Color(0xFFCEEFF2),
                        disabledThumbColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                )

                val horizontalPaddingPx = with(LocalDensity.current) { 30.dp.toPx() }
                val trackWidthPx = sliderWidthPx - horizontalPaddingPx * 2f
                val fraction = (degree - valueRange.start) /
                        (valueRange.endInclusive - valueRange.start)
                val xOffsetPx = horizontalPaddingPx + (trackWidthPx - thumbRadiusPx * 2) * fraction

                Text(
                    "40도",
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .offset { IntOffset(xOffsetPx.roundToInt(),
                            50.dp.toPx().roundToInt()) }
                )
            }
            HorizontalDivider(Modifier.padding(30.dp),
                thickness = 3.dp, color = Color(0xFFCEEFF2))
        }

        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "사용자 리뷰",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                    Text(
                        "전체보기",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color(0xFF6D6B6B),
                        modifier = Modifier.clickable{ onViewReviews() }
                    )
                }
                Spacer(Modifier.height(10.dp))
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFCEEFF2), shape = RoundedCornerShape(10.dp))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp, start = 25.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            "귀여운 위스키 123"
                        )
                        Text(
                            "2025.06.15",
                            fontWeight = FontWeight.Normal,
                            fontSize = 10.sp
                        )
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp, vertical = 5.dp)
                    ) {
                        StarRatingDecimal(rating = 3.8f)
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }

        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .background(Color(0xFFCEEFF2), shape = RoundedCornerShape(10.dp))
            ) {
                Text(
                    "이 술을 좋아한 다른 유저들의 픽!",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF5B3535),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                repeat(4) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(140.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    ) {

                    }
                }
            }
        }
    }
}