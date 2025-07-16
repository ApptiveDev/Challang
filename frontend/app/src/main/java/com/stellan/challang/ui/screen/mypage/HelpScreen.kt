package com.stellan.challang.ui.screen.mypage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.ui.theme.PaperlogyFamily
import androidx. compose. foundation. interaction. MutableInteractionSource
import androidx. compose. runtime. remember

@Composable
fun HelpScreen(
    onPrivacy: () -> Unit,
    onTerms: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "도움말",
            fontFamily = PaperlogyFamily,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 24.dp, bottom = 200.dp)
        )

        HelpItem("개인정보처리방침", onPrivacy)
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFFE3F0F0)
        )
        HelpItem("서비스 이용약관", onTerms)
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color(0xFFE3F0F0)
        )
    }

}

@Composable
fun HelpItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )

            .padding(horizontal = 24.dp, vertical = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontFamily = PaperlogyFamily,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = ">",
            fontFamily = PaperlogyFamily,
            fontSize = 18.sp,
            color = Color(0xFFADADAD)
        )
    }
}