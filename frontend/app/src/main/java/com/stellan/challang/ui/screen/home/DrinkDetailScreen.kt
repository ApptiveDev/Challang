package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stellan.challang.R
import com.stellan.challang.data.model.drink.Drink
import com.stellan.challang.ui.component.StarRatingDecimal
import com.stellan.challang.ui.theme.PaperlogyFamily
import com.stellan.challang.ui.util.formatAbv

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkDetailScreen(
    drink: Drink,
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
                AsyncImage(
                    model = drink.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(600.dp)
                        .padding(top = 50.dp, bottom = 70.dp)
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
                    drink.name,
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 26.sp
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    if (drink.minAbv == drink.maxAbv) {
                        "${drink.typeName} ${formatAbv(drink.minAbv)}도"
                    } else {
                        "${drink.typeName} ${formatAbv(drink.minAbv)}~${formatAbv(drink.maxAbv)}도"
                    },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                drink.liquorTags.forEach { tag ->
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = {
                            Text(
                                tag.tagName,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
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
                    drink.origin,
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
                    drink.tastingNote,
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
            val degree = drink.maxAbv.toFloat()
            val label = "${formatAbv(drink.maxAbv)}도"

            val sliderState = remember {
                SliderState(
                    value = degree,
                    valueRange = 0f..60f
                )
            }

            val interaction = remember { MutableInteractionSource() }
            val sliderColors = SliderDefaults.colors(
                disabledActiveTrackColor = Color(0xFF6CD0D8),
                disabledInactiveTrackColor = Color(0xFFCEEFF2),
                disabledThumbColor = Color.Black
            )

            Slider(
                state = sliderState,
                enabled = false,
                interactionSource = interaction,
                colors = sliderColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                thumb = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(35.dp)
                        )
                        Spacer(Modifier.height(20.dp))
                        Text(
                            label,
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                },
                track = {
                    SliderDefaults.Track(
                        sliderState = sliderState,
                        enabled = false,
                        colors = sliderColors,
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 8.dp
                    )
                }
            )
            HorizontalDivider(
                Modifier.padding(start = 30.dp, top = 10.dp, end = 30.dp, bottom = 20.dp),
                thickness = 3.dp, color = Color(0xFFCEEFF2)
            )
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
                        modifier = Modifier.clickable { onViewReviews() }
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
                        StarRatingDecimal(rating = 3.6f)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            repeat(3) {
                                SuggestionChip(
                                    onClick = {},
                                    label = {
                                        Text(
                                            "바닐라향",
                                            fontSize = 8.sp,
                                            color = Color.Black,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    },
                                    border = BorderStroke(0.dp, Color.Transparent),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(9.dp),
                                    modifier = Modifier
                                        .height(18.dp)
                                        .width(60.dp)
                                )
                            }
                        }
                    }
                    Text(
                        "근본은 근본. 제일 만만한 입문자용 위스키",
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp, vertical = 5.dp)
                    )
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
                        Box(Modifier.padding(vertical = 15.dp)) {
                            Image(
                                painter = painterResource(R.drawable.ballantines_30_years_old),
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight
                            )
                        }
                        Column(Modifier.padding(top = 15.dp, bottom = 15.dp)) {
                            Text(
                                "발렌타인 30년",
                                fontSize = 18.sp
                            )
                            StarRatingDecimal(4.5f)
                            Spacer(Modifier.height(12.dp))
                            FlowRow(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalArrangement = Arrangement.spacedBy(7.dp)
                            ) {
                                repeat(6) {
                                    SuggestionChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                "바닐라향",
                                                fontSize = 9.sp,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        },
                                        border = BorderStroke(0.dp, Color.Transparent),
                                        colors = SuggestionChipDefaults.suggestionChipColors(
                                            containerColor = Color(0xFFCEEFF2)
                                        ),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .height(24.dp)
                                            .width(63.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}