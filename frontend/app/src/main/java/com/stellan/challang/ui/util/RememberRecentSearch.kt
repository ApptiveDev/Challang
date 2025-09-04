package com.stellan.challang.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.stellan.challang.data.prefs.recentSearchesFlow

@Composable
fun rememberRecentSearches(): State<List<String>> {
    val context = LocalContext.current.applicationContext
    return (context as android.content.Context)
        .recentSearchesFlow()
        .collectAsState(initial = emptyList())
}