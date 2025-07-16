package com.stellan.challang.ui.screen.mypage

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.stellan.challang.ui.theme.PaperlogyFamily
import androidx. compose. runtime. rememberCoroutineScope
import com. stellan. challang. data. repository. AuthRepository
import androidx. compose. ui. platform. LocalContext
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import android. util. Log
import android. R. id. message
import com. stellan. challang. ui. viewmodel. AuthViewModel
import com.stellan.challang.ui.viewmodel.AuthViewModelFactory
import com.stellan.challang.data.api.ApiClient
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx. navigation. compose. rememberNavController
import kotlinx.coroutines.delay
import com. stellan.challang.ui.viewmodel.UserViewModel
import com.stellan.challang.data.repository.UserRepository
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx. compose. ui. graphics. RectangleShape
import androidx. compose. foundation. interaction. MutableInteractionSource



@Composable
fun WithdrawScreen(onDone: () -> Unit) {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(ApiClient.api)
    )
    // Composable 내부에서 생성 (단일 화면용)
    val navController = rememberNavController()

    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    val reasonsMap = mapOf(
        "서비스 불만족" to 0,
        "앱 오류" to 1,
        "정보의 부족" to 2,
        "다른 계정으로 재가입" to 3,
        "기타" to 4
    )

    val userViewModel = remember { UserViewModel(UserRepository(ApiClient.userApi)) }

    LaunchedEffect(Unit) {
        userViewModel.fetchActivityCounts()
    }

    val activityCount by userViewModel.activityCount.collectAsState()


    var step by rememberSaveable { mutableIntStateOf(1) }
    var selectedReason by rememberSaveable { mutableStateOf<String?>(null) }
    var agree by rememberSaveable { mutableStateOf(false) }
    var showConfirmDialog by rememberSaveable { mutableStateOf(false) }
    var showDoneDialog by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()



    if (step == 1) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Text(
                    text = "회원 탈퇴",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp, bottom = 6.dp),
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(40.dp))

                Text(
                    text = "탈퇴하시려는 이유를\n알려주세요.",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color.Black
                )
                Spacer(Modifier.height(40.dp))

                val reasons = listOf(
                    "서비스 불만족", "앱 오류", "정보의 부족", "다른 계정으로 재가입", "기타"
                )


                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    reasons.forEach { reason ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    selectedReason = reason
                                }

                                .padding(vertical = 1.dp)
                        ) {
                            RadioButton(
                                selected = selectedReason == reason,
                                onClick = { selectedReason = reason },
                                modifier = Modifier.scale(1.2f),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFFB2DADA),
                                    unselectedColor = Color(0xFFB2DADA),
                                    disabledSelectedColor = Color(0xFFB2DADA),
                                    disabledUnselectedColor = Color(0xFFB2DADA).copy(alpha = 0.4f)
                                )
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = reason,
                                fontFamily = PaperlogyFamily,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { step = 2 },
                    enabled = selectedReason != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB2DADA)
                    ),
                    shape = RoundedCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = "다음",
                        fontFamily = PaperlogyFamily,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }

    else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(
                    text = "회원탈퇴",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 25.dp, bottom = 6.dp),
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(40.dp))
                Text(
                    text = "잠시만요!",
                    fontFamily = PaperlogyFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(40.dp))

                Text(
                    text = "탈퇴 시 지금까지 작성하셨던 리뷰와\n 맞춤 큐레이팅이 사라져 복구가 불가해요.",
                    fontFamily = PaperlogyFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black
                )
                Spacer(Modifier.height(40.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF0F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            text = "작성한 내 리뷰: ${activityCount?.writtenReviewCount ?: 0}건",
                            fontFamily = PaperlogyFamily,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF0F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text(
                            text = "찜한 큐레이팅: ${activityCount?.likedCurationCount ?: 0}건",
                            fontFamily = PaperlogyFamily,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF0F0))
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Column(modifier = Modifier.padding(15.dp)) {
                    Text(
                        text = "탈퇴 시 계정 및 개인정보와 이용 기록이 모두 삭제되며\n" +
                                "삭제된 데이터는 복구가 불가능합니다.\n" +
                                "또한 연속적인 탈퇴 후 재가입 시 이용에\n 제한을 받을 수 있습니다.",
                        fontFamily = PaperlogyFamily,
                        fontSize = 14.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = agree, onCheckedChange = { agree = it })
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "주의사항 확인 후 탈퇴 동의",
                            fontFamily = PaperlogyFamily,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { showConfirmDialog = true },
                        enabled = agree,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2DADA)),
                        shape = RoundedCornerShape(30.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = "탈퇴할래요",
                            fontFamily = PaperlogyFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
            if (showConfirmDialog) {
                WithdrawConfirmDialog(
                    onConfirm = {
                        showConfirmDialog = false

                        coroutineScope.launch {
                            val reasonId = reasonsMap[selectedReason] ?: 1  // 기본값 1
                            authViewModel.withdraw(
                                reason = reasonId,
                                onSuccess = {
                                    showDoneDialog = true
                                },
                                onError = {
                                    Log.e("WithdrawScreen", "회원탈퇴 실패: $message")
                                },
                                onUnauthorized = {
                                    coroutineScope.launch {
                                        delay(100) // 한 프레임 대기
                                        navController.navigate("auth") {
                                            popUpTo("main") { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }
                    },
                    onDismiss = { showConfirmDialog = false }
                )
            }

            if (showDoneDialog) {
                WithdrawDoneDialog(
                    onAcknowledge = {
//                        authRepository.clearTokens() // 토큰 삭제
                        onDone() // 로그인 화면으로 이동
                        showDoneDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun WithdrawConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "회원탈퇴",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "모든 주의사항을 숙지하고\n정말로 탈퇴하시겠습니까?",
                    fontFamily = PaperlogyFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFE0F2F1))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onDismiss
                            )
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("취소",
                            fontFamily = PaperlogyFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFB2DADA))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onConfirm
                            )
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("확인",
                            fontFamily = PaperlogyFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun WithdrawDoneDialog(
    onAcknowledge: () -> Unit
) {
    Dialog(onDismissRequest = onAcknowledge) {
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "회원탈퇴",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "탈퇴가 완료되었습니다.",
                    fontFamily = PaperlogyFamily,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color(0xFFB2DADA))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onAcknowledge()
                            }

                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("확인",
                            fontFamily = PaperlogyFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black)
                    }
                }
            }
        }
    }
}