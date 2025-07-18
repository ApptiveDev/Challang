package com.stellan.challang.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.stellan.challang.R

val PaperlogyFamily = FontFamily(
    Font(R.font.paperlogy_regular, FontWeight.Normal),
    Font(R.font.paperlogy_semibold, FontWeight.SemiBold),
    Font(R.font.paperlogy_bold, FontWeight.Bold),
    Font(R.font.paperlogy_light, FontWeight.Light),
    Font(R.font.paperlogy_thin, FontWeight.Thin),
    Font(R.font.paperlogy_black, FontWeight.Black),
    Font(R.font.paperlogy_extrabold, FontWeight.ExtraBold),
    Font(R.font.paperlogy_extralight, FontWeight.ExtraLight),
    Font(R.font.paperlogy_medium, FontWeight.Medium),
)

val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = PaperlogyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PaperlogyFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = PaperlogyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = Color(0xFF7E7E7E)
    )
)