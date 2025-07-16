package com.stellan.challang.ui.screen.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.ui.res.painterResource
import androidx. compose. ui. layout. ContentScale
import com.stellan.challang.ui.theme.PaperlogyFamily
import androidx.compose.runtime.*
import androidx.compose.material3.Card
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.window.Dialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import com.stellan.challang.data.repository.AuthRepository
import androidx. navigation. NavHostController
import com. stellan. challang.R
import androidx. compose. foundation. Image
import android.widget.Toast
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stellan.challang.ui.viewmodel.AuthViewModel
import com.stellan.challang.ui.viewmodel.AuthViewModelFactory
import com. stellan. challang. data. api. ApiClient
import com. stellan.challang.ui.viewmodel.UserViewModel
import com.stellan.challang.data.repository.UserRepository
import androidx. compose. ui. graphics. RectangleShape
import androidx. compose. foundation. interaction. MutableInteractionSource





@Composable
fun MyPageScreen(
    rootNavController: NavHostController,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
//    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val authRepository = remember { AuthRepository(context) }

    var showLogoutDialog by remember { mutableStateOf(false) }
    var showLoggedOutDialog by remember { mutableStateOf(false) }

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(ApiClient.api)
    )

    val userViewModel = remember { UserViewModel(UserRepository(ApiClient.userApi)) }

    val userInfo by userViewModel.userInfo.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchUserInfo()
        userViewModel.fetchActivityCounts()
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "마이페이지",
                fontFamily = PaperlogyFamily,
                fontSize = 24.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth() ,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = userInfo?.nickname ?: "닉네임 없음",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 15.dp, bottom = 4.dp)
                    )

                    Text(
                        text = userInfo?.birthDate ?: "생년월일 없음",
                        fontFamily = PaperlogyFamily,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 15.dp, bottom = 4.dp)
                    )

                    Text(
                        text = when (userInfo?.gender) {
                            0 -> "여성"
                            1 -> "남성"
                            else -> "성별 없음"
                        },
                        fontFamily = PaperlogyFamily,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(start = 15.dp, bottom = 4.dp)
                    )

                }
                Spacer(modifier = Modifier.width(60.dp))
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE8F1F1),
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_image_basic),
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 4.dp,
            color = Color(0xFFE3F0F0)
        )
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp)
            ) {
            MyPageItem(text = "도움말", onClick = onHelpClick)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFE3F0F0)
            )
            MyPageItem(text = "로그아웃", onClick = { showLogoutDialog = true })
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFE3F0F0)
            )
            MyPageItem(text = "회원탈퇴", onClick = onWithdrawClick )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFE3F0F0)
            )
        }
    }
    if (showLogoutDialog) {
        LogoutDialog(
            onConfirm = {
                coroutineScope.launch {
                    val success = authViewModel.logout()
                    if (success) {
                        showLogoutDialog = false
                        showLoggedOutDialog = true
                    } else {
                        showLogoutDialog = false
                        Toast.makeText(context, "로그아웃에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onDismiss = {
                showLogoutDialog = false
            }
        )
    }


    if (showLoggedOutDialog) {
        ShowLoggedOutDialog(
            onDismiss = { showLoggedOutDialog = false },
            onConfirm = {
                showLoggedOutDialog = false
                rootNavController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
    }
}


@Composable
private fun MyPageItem(text: String, onClick: () -> Unit) {
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

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "로그아웃",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "로그아웃 하시겠습니까?",
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
                                indication = null
                            ) { onDismiss() }
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
                                indication = null
                            ) { onConfirm() }
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
fun ShowLoggedOutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "로그아웃",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "로그아웃 되었습니다.",
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
                            ) {onConfirm() }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("확인", fontFamily = PaperlogyFamily, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }
        }
    }
}


