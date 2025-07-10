package com.stellan.challang.ui.screen.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.ui.theme.PaperlogyFamily

@Composable
fun CuratingGuideScreen(
    overlayAlpha: Float = 0.6f,
    lineLength: Dp = 200.dp,
    bigCircleRadius: Dp = 30.dp,
    smallCircleRadius: Dp = 8.dp,
    onComplete: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = overlayAlpha))
            .clickable{
                if (step == 1) {
                    step = 2
                } else {
                    onComplete()
                }
            }
    ) {
        when (step) {
            1 -> {
                VerticalGuide(
                    lineLength = lineLength,
                    bigRadius = bigCircleRadius,
                    smallRadius = smallCircleRadius
                )
            }
            2 -> {
                HorizontalGuide(
                    lineLength = lineLength,
                    bigRadius = bigCircleRadius,
                    smallRadius = smallCircleRadius
                )
            }
        }
    }
}

@Composable
private fun VerticalGuide(
    lineLength: Dp,
    bigRadius: Dp,
    smallRadius: Dp
) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Rewind", fontFamily = PaperlogyFamily, fontSize = 20.sp,
            fontWeight = FontWeight.Normal, color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Canvas(modifier = Modifier.size(lineLength, lineLength)) {
            val cx = size.width / 2
            val cy = size.height / 2
            drawLine(
                color = Color.White,
                start = Offset(cx - lineLength.toPx() / 2, cy),
                end = Offset(cx + lineLength.toPx() / 2, cy),
                strokeWidth = 4f
            )
            drawCircle(
                color = Color.Transparent,
                center = Offset(cx, cy),
                radius = bigRadius.toPx(),
                style = Stroke(width = 4f)
            )
            drawCircle(
                color = Color.White,
                center = Offset(cx, cy + lineLength.toPx() / 2),
                radius = smallRadius.toPx()
            )
        }

        Text(
            "Pass", fontFamily = PaperlogyFamily, fontSize = 20.sp,
            fontWeight = FontWeight.Normal, color = Color.White,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun HorizontalGuide(
    lineLength: Dp,
    bigRadius: Dp,
    smallRadius: Dp
) {
    Row(
        Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "Bad", fontFamily = PaperlogyFamily, fontSize = 20.sp,
            fontWeight = FontWeight.Normal, color = Color.White,
            modifier = Modifier.padding(end = 16.dp)
        )

        Canvas(modifier = Modifier.size(lineLength, lineLength)) {
            val cx = size.width / 2
            val cy = size.height / 2
            drawLine(
                color = Color.White,
                start = Offset(cx, cy - lineLength.toPx() / 2),
                end = Offset(cx, cy + lineLength.toPx() / 2),
                strokeWidth = 4f
            )
            drawCircle(
                color = Color.Transparent,
                center = Offset(cx, cy),
                radius = bigRadius.toPx(),
                style = Stroke(width = 4f)
            )
            drawCircle(
                color = Color.White,
                center = Offset(cx + lineLength.toPx() / 2, cy),
                radius = smallRadius.toPx()
            )
        }
        Text(
            "Good", fontFamily = PaperlogyFamily, fontSize = 20.sp,
            fontWeight = FontWeight.Normal, color = Color.White,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}