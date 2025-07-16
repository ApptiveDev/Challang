package com.stellan.challang.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.ui.theme.PaperlogyFamily

@Composable
fun SpeechBubble(
    text: String,
    bubbleColor: Color = Color(0xFFFFD940)
) {
    val density = LocalDensity.current
    val bubbleShape = GenericShape { size, _ ->
        val tailW = with(density) { 10.dp.toPx() }
        val tailH = with(density) { 8.dp.toPx() }
        val tailX = with(density) { 16.dp.toPx() }
        val radius = (size.height - tailH) / 2f
        val bodyHeight = size.height - tailH

        addRoundRect(
            RoundRect(
                rect = Rect(0f, 0f, size.width, bodyHeight),
                topLeft = CornerRadius(radius, radius),
                topRight = CornerRadius(radius, radius),
                bottomRight = CornerRadius(radius, radius),
                bottomLeft = CornerRadius(radius, radius)
            )
        )
        val baseY = bodyHeight
        val leftX = tailX
        val rightX = tailX + tailW
        val apexX = tailX + tailW * 1.2f
        val apexY = size.height

        moveTo(leftX, baseY)
        lineTo(apexX, apexY)
        lineTo(rightX, baseY)
        close()
    }

    Surface(
        shape = bubbleShape,
        color = bubbleColor,
        shadowElevation = 1.5.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 6.dp, bottom = 12.dp),
            fontFamily = PaperlogyFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}