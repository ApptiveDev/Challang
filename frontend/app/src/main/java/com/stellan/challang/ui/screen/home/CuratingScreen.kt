package com.stellan.challang.ui.screen.home

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stellan.challang.R
import com.stellan.challang.data.api.ApiClient
import com.stellan.challang.data.model.drink.Drink
import com.stellan.challang.data.repository.DrinkRepository
import com.stellan.challang.hasSeenGuideFlow
import com.stellan.challang.rememberRecentSearches
import com.stellan.challang.saveSearchQuery
import com.stellan.challang.setGuideShown
import com.stellan.challang.ui.theme.PaperlogyFamily
import com.stellan.challang.ui.util.formatAbv
import com.stellan.challang.ui.viewmodel.DrinkViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuratingScreen(onDetail: (drink: Drink) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val recentSearches by rememberRecentSearches()

    val hasSeenGuide by context.hasSeenGuideFlow().collectAsState(initial = false)
    val showGuide = !hasSeenGuide

    val drinkViewModel = remember { DrinkViewModel(
        DrinkRepository(ApiClient.drinkApi)) }
    val drinks by drinkViewModel.drinks.collectAsState()

    LaunchedEffect(Unit) {
        drinkViewModel.fetchDrinks()
    }


    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            SearchBar(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = {
                            scope.launch { saveSearchQuery(context, text) }
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                colors = SearchBarDefaults.colors(
                    containerColor = if (expanded) Color.White else Color(0xFFCEEFF2)
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp)
                ) {
                    Text(
                        "최근 검색어",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(recentSearches) { past ->
                            SuggestionChip(
                                onClick = {
                                    text = past
                                    expanded = false
                                },
                                label = {
                                    Text(
                                        text = past,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 5.dp),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                border = SuggestionChipDefaults.suggestionChipBorder(
                                    enabled = true,
                                    borderColor = Color.Transparent
                                ),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0xFFCEEFF2)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        "추천 키워드",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(6) { iter ->
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        "사랑해",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 5.dp),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                border = SuggestionChipDefaults.suggestionChipBorder(
                                    enabled = true,
                                    borderColor = Color.Transparent
                                ),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0xFFCEEFF2)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                    Text(
                        "실시간 인기 주류 순위",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(8) { iter ->
                            SuggestionChip(
                                onClick = {},
                                label = {
                                    Text(
                                        "사랑해",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 5.dp),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                border = SuggestionChipDefaults.suggestionChipBorder(
                                    enabled = true,
                                    borderColor = Color.Transparent
                                ),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = Color(0xFFCEEFF2)
                                )
                            )
                        }
                    }
                }
            }

            var currentIndex by remember { mutableIntStateOf(0) }
            var history by remember { mutableStateOf(listOf<Int>()) }

            Box(Modifier.fillMaxSize()) {
                drinks.forEachIndexed { idx, drink ->
                    if (idx < currentIndex || idx > currentIndex + 1) return@forEachIndexed

                    val offsetX = remember { Animatable(0f) }
                    val offsetY = remember { Animatable(0f) }

                    Card(
                        Modifier
                            .fillMaxSize()
                            .padding(27.dp)
                            .offset {
                                IntOffset(
                                    offsetX.value.roundToInt(),
                                    offsetY.value.roundToInt()
                                )
                            }
                            .pointerInput(currentIndex) {
                                detectDragGestures(
                                    onDragEnd = {
                                        val threshold = size.height * 0.15f
                                        when {
                                            offsetY.value > threshold -> {
                                                history = history + currentIndex
                                                currentIndex = (currentIndex + 1)
                                                    .coerceAtMost(drinks.lastIndex)
                                                scope.launch {
                                                    offsetX.snapTo(0f)
                                                    offsetY.snapTo(0f)
                                                }
                                            }

                                            offsetY.value < -threshold -> {
                                                if (history.isNotEmpty()) {
                                                    currentIndex = history.last()
                                                    history = history.dropLast(1)
                                                }
                                                scope.launch {
                                                    offsetX.snapTo(0f)
                                                    offsetY.snapTo(0f)
                                                }
                                            }

//                                            offsetX.value > threshold -> {
//                                                history = history + currentIndex
//                                                currentIndex = (currentIndex + 1)
//                                                    .coerceAtMost(drinks.lastIndex)
//                                                scope.launch {
//                                                    offsetX.snapTo(0f)
//                                                    offsetY.snapTo(0f)
//                                                }
//                                            }
//
//                                            offsetX.value < -threshold -> {
//                                                history = history + currentIndex
//                                                currentIndex = (currentIndex + 1)
//                                                    .coerceAtMost(drinks.lastIndex)
//                                                scope.launch {
//                                                    offsetX.snapTo(0f)
//                                                    offsetY.snapTo(0f)
//                                                }
//                                            }

                                            else -> {
                                                scope.launch {
                                                    offsetX.animateTo(0f,
                                                        tween(300))
                                                    offsetY.animateTo(0f,
                                                        tween(300))
                                                }
                                            }
                                        }
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        scope.launch {
                                            offsetX.snapTo(offsetX.value + dragAmount.x)
                                            offsetY.snapTo(offsetY.value + dragAmount.y)
                                        }
                                    }
                                )
                            }
                            .shadow(
                                elevation = 1.dp,
                                shape = RoundedCornerShape(24.dp),
                                clip = false
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF6CD0D8), Color.White)
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ),
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        var boxSize by remember { mutableStateOf(IntSize.Zero) }

                        Box(Modifier.fillMaxSize().onSizeChanged { boxSize = it }) {
                            AsyncImage(
                                model = drink.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(450.dp)
                                    .offset(y = 80.dp),
                                alignment = Alignment.Center
                            )

                            Column(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(top = 42.dp, start = 24.dp)
                            ) {
                                Text(
                                    text = drink.name,
                                    fontFamily = PaperlogyFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 34.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = if (drink.minAbv == drink.maxAbv) {
                                        "${formatAbv(drink.minAbv)}%"
                                    } else {
                                        "${formatAbv(drink.minAbv)}~${formatAbv(drink.maxAbv)}%"
                                    },
                                    fontFamily = PaperlogyFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 34.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = drink.typeName,
                                    fontFamily = PaperlogyFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 34.sp,
                                    color = Color.White
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier.weight(1.3f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    TextButton(
                                        onClick = { onDetail(drink) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF98DFE5)
                                        ),
                                        modifier = Modifier.size(width = 180.dp, height = 40.dp)
                                    ) {
                                        Text(
                                            "자세히 보기",
                                            fontFamily = PaperlogyFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = Color.White
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.weight(1f),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    IconButton(onClick = { /* 공유 */ }) {
                                        Icon(
                                            Icons.Outlined.Share,
                                            contentDescription = "공유하기",
                                            tint = Color(0xFF6CD0D8),
                                            modifier = Modifier
                                                .size(35.dp)
                                        )
                                    }
                                    IconButton(onClick = { /* 다운로드 */ }) {
                                        Icon(
                                            Icons.Default.Download,
                                            contentDescription = "다운로드",
                                            tint = Color(0xFF6CD0D8),
                                            modifier = Modifier
                                                .size(35.dp)
                                        )
                                    }
                                }
                            }
                            if (boxSize.width > 0 && boxSize.height > 0) {
                                val density = LocalDensity.current

                                val imageTopPx    = with(density){ 150.dp.toPx() }
                                val imageHeightPx = with(density){ 320.dp.toPx() }
                                val boxWidthPx    = boxSize.width.toFloat()

                                val labels = drink.liquorTags.map { it.tagName }.take(8)
                                val leftLabels  = labels.take(4)
                                val rightLabels = labels.takeLast(4)

                                val stripHeightPx = imageHeightPx / 4f

                                val xLeftMinPx  = boxWidthPx * 0.00f
                                val xLeftMaxPx  = boxWidthPx * 0.10f
                                val xRightMinPx = boxWidthPx * 0.65f
                                val xRightMaxPx = boxWidthPx * 0.75f

                                (0 until 4).forEach { idx ->
                                    val yPx = imageTopPx + stripHeightPx * idx + stripHeightPx / 2f

                                    val xLeftPx = Random.nextFloat() *
                                            (xLeftMaxPx - xLeftMinPx) + xLeftMinPx
                                    Box(
                                        Modifier.offset(
                                            x = with(density) { xLeftPx.toDp() },
                                            y = with(density) { yPx.toDp() }
                                        )
                                    ) {
                                        SuggestionChip(
                                            onClick = { },
                                            enabled = false,
                                            label = { Text(
                                                leftLabels[idx],
                                                fontSize = 12.sp,
                                                color = Color.Black
                                            ) },
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                disabledContainerColor = if (Random.nextBoolean())
                                                    Color(0xFFEFFAFB)
                                                else Color(0xFFFFEDDC)
                                            ),
                                            border = BorderStroke(0.dp, Color.Transparent),
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                    }

                                    val xRightPx = Random.nextFloat() *
                                            (xRightMaxPx - xRightMinPx) + xRightMinPx
                                    Box(
                                        Modifier.offset(
                                            x = with(density) { xRightPx.toDp() },
                                            y = with(density) { yPx.toDp() }
                                        )
                                    ) {
                                        SuggestionChip(
                                            onClick = { },
                                            enabled = false,
                                            label = { Text(
                                                rightLabels[idx],
                                                fontSize = 12.sp,
                                                color = Color.Black
                                            ) },
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                disabledContainerColor = if (Random.nextBoolean())
                                                    Color(0xFFEFFAFB)
                                                else Color(0xFFFFEDDC)
                                            ),
                                            border = BorderStroke(0.dp, Color.Transparent),
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

        if (showGuide) {
            var step by remember { mutableIntStateOf(1) }

            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable {
                        if (step == 1) step = 2
                        else {
                            scope.launch { context.setGuideShown() }
                        }
                    }
            ) {
                val painter = when (step) {
                    1 -> painterResource(R.drawable.curating_guide_1)
                    else -> painterResource(R.drawable.curating_guide_2)
                }

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                )
            }
        }
    }
}