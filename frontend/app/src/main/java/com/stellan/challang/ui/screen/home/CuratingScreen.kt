package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.R
import com.stellan.challang.hasSeenGuideFlow
import com.stellan.challang.rememberRecentSearches
import com.stellan.challang.saveSearchQuery
import com.stellan.challang.setGuideShown
import com.stellan.challang.ui.theme.PaperlogyFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuratingScreen(onDetail: (drinkID: String) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val recentSearches by rememberRecentSearches()

    val hasSeenGuide by context.hasSeenGuideFlow().collectAsState(initial = false)
    var showGuide by remember { mutableStateOf(!hasSeenGuide) }

    Box(Modifier.fillMaxSize()) {
        Column (Modifier.fillMaxSize()) {
            SearchBar(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = {
                            scope.launch { saveSearchQuery(context, text) }
                            expanded = false },
                        expanded = expanded,
                        onExpandedChange = { expanded = it },
                        trailingIcon = { Icon(Icons.Default.Search,
                            contentDescription = null) },
                    )
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                colors = SearchBarDefaults.colors(
                    containerColor = if (expanded) Color.White else Color(0xFFCEEFF2)
                ),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(25.dp)) {
                    Text("최근 검색어",
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
                                label = { Text(
                                    text = past,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp),
                                    textAlign = TextAlign.Center
                                ) },
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
                    Text("추천 키워드",
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
                                label = { Text(
                                    "사랑해",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp),
                                    textAlign = TextAlign.Center
                                ) },
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
                    Text("실시간 인기 주류 순위",
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
                                label = { Text(
                                    "사랑해",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp),
                                    textAlign = TextAlign.Center
                                ) },
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

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(27.dp)
                    .background(brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF6CD0D8), Color.White)
                    ), shape = RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
            ) {
                Box(Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(R.drawable.balvenie),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(top = 40.dp, start = 24.dp)
                    ) {
                        Text(
                            text = "발베니",
                            fontFamily = PaperlogyFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            color = Color.White
                        )
                        Text(
                            text = "40%",
                            fontFamily = PaperlogyFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            color = Color.White
                        )
                        Text(
                            text = "고도수",
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
                            .padding(bottom = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Box(
                            modifier = Modifier.weight(1.3f),
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(
                                onClick = { onDetail("1") },
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
                                Icon(Icons.Outlined.Share,
                                    contentDescription = "공유하기",
                                    tint = Color(0xFF6CD0D8),
                                    modifier = Modifier
                                        .size(35.dp))
                            }
                            IconButton(onClick = { /* 다운로드 */ }) {
                                Icon(Icons.Default.Download,
                                    contentDescription = "다운로드",
                                    tint = Color(0xFF6CD0D8),
                                    modifier = Modifier
                                        .size(35.dp))
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
                            showGuide = false
                            // scope.launch { context.setGuideShown() }
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
                    contentScale = ContentScale.FillBounds,
                    alpha = 0.6f
                )
            }
        }
    }
}