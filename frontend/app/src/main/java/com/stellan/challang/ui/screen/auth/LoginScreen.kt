package com.stellan.challang.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kakao.sdk.user.UserApiClient
import com.stellan.challang.R
import com.stellan.challang.ui.component.SpeechBubble
import com.stellan.challang.ui.theme.PaperlogyFamily
import com.stellan.challang.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.stellan.challang.data.api.ApiClient
import androidx.compose.runtime.remember
import com.stellan.challang.ui.viewmodel.AuthViewModelFactory

@Composable
fun LoginScreen(
    navController: NavHostController,
    onLoginSuccess: (Boolean, String, Boolean) -> Unit,
    onNeedSignup: (String) -> Unit
) {
    val context = LocalContext.current
    val apiService = remember { ApiClient.api }
    val coroutineScope = rememberCoroutineScope()
    val viewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(apiService)
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.login_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.3f), blendMode = BlendMode.Darken)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 230.dp)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "당신이 술을 필요로 할때, 찰랑",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 30.sp,
                        color = Color.White
                    )
                    Text(
                        text = "나만을 위한 주류 큐레이팅 서비스",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SpeechBubble(text = "3초만에 회원가입")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
                                if (error != null || token == null) {
                                    Toast.makeText(context, "카카오 로그인 실패", Toast.LENGTH_SHORT).show()
                                } else {
                                    val kakaoAccessToken = token.accessToken
                                    coroutineScope.launch {
                                        viewModel.kakaoLogin(
                                            kakaoAccessToken = kakaoAccessToken,
                                            onSuccess = { isNewUser, isPreferenceSet ->
                                                onLoginSuccess(isNewUser, kakaoAccessToken, isPreferenceSet)  // ✅ 토큰도 같이 넘김
                                            },
                                            onNeedSignup = {
                                                onNeedSignup(kakaoAccessToken)
                                            },
                                            onError = {
                                                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                            }
                                        )
                                    }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD940)),
                        modifier = Modifier.size(width = 300.dp, height = 50.dp)
                    ) {
                        Text(
                            text = "카카오톡으로 계속하기",
                            fontFamily = PaperlogyFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}
