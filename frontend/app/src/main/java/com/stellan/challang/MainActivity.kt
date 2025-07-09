package com.stellan.challang

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.navigation.compose.rememberNavController
import com.stellan.challang.ui.navigation.AppNavHost
import com.stellan.challang.ui.theme.ChallangTheme
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "user_prefs")
private val RECENT_SEARCHES_KEY = stringSetPreferencesKey("recent_searches")
private val GUIDE_SHOWN_KEY = booleanPreferencesKey("guide_shown")


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallangTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)

            }
        }
    }
}

suspend fun saveSearchQuery(context: Context, query: String) {
    context.dataStore.edit { prefs ->
        val current = prefs[RECENT_SEARCHES_KEY] ?: emptySet()
        val updated = (listOf(query) + current.filter { it != query })
            .take(3)
            .toSet()
        prefs[RECENT_SEARCHES_KEY] = updated
    }
}

@Composable
fun rememberRecentSearches(): State<List<String>> {
    val context = LocalContext.current
    val flow = context.dataStore.data
        .map { prefs ->
            prefs[RECENT_SEARCHES_KEY]?.toList().orEmpty()
        }
    return flow.collectAsState(initial = emptyList())
}

@Composable
fun rememberGuideShown(): State<Boolean> {
    val context = LocalContext.current
    val prefsFlow = context.dataStore.data
    val shownFlow = prefsFlow.map { prefs ->
        prefs[GUIDE_SHOWN_KEY] ?: false
    }
    return shownFlow.collectAsState(initial = false)
}

