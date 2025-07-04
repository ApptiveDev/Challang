package com.stellan.challang.ui.screen.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stellan.challang.rememberRecentSearches
import com.stellan.challang.saveSearchQuery
import com.stellan.challang.ui.theme.PaperlogyFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuratingScreen() {
    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    val recentSearches by rememberRecentSearches()

    Box(Modifier
        .fillMaxSize()
        .semantics { isTraversalGroup = true }) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = { expanded = false },
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
                    items(recentSearches) { q ->
                        SuggestionChip(
                            onClick = {
                                text = q
                                expanded = false
                                LaunchedEffect(q) {
                                    saveSearchQuery(LocalContext.current, q)
                                }
                            },
                            label = { Text(
                                text = q,
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

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 72.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.semantics { traversalIndex = 1f },
        ) {
            val list = List(100) { "Text $it" }
            items(count = list.size) {
                Text(
                    text = list[it],
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}