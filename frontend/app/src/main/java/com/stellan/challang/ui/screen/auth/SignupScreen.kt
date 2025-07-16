package com.stellan.challang.ui.screen.auth

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stellan.challang.R
import com.stellan.challang.ui.component.BirthdayPicker
import java.util.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import java.time.LocalDate
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import com.stellan.challang.ui.theme.PaperlogyFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import com. stellan. challang. ui. viewmodel. AuthViewModel
import com.stellan.challang.ui.viewmodel.AuthViewModelFactory
import com.stellan.challang.data.api.ApiClient
import androidx. compose. ui. platform. LocalContext
import android.widget.Toast


private val adjectives = listOf(
    "달콤한", "쌉쌀한", "상큼한", "씁쓸한", "톡 쏘는", "부드러운", "거친", "묵직한", "가벼운", "향긋한",
    "구수한", "신선한", "오래된", "젊은", "드라이한", "스위트한", "라이트한", "헤비한", "깔끔한", "지저분한",
    "탁한", "맑은", "황금빛의", "투명한", "붉은", "푸른", "녹색의", "은은한", "강렬한", "풍부한",
    "섬세한", "복합적인", "단순한", "숙성된", "미숙한", "깊은", "얕은", "따뜻한", "시원한", "차가운",
    "뜨거운", "쨍한", "눅진한", "상쾌한", "진한", "연한", "짜릿한", "자극적인", "독한", "순한",
    "쌉쌀한", "비릿한", "쿰쿰한", "고소한", "매콤한", "끈적한", "씁쓸한", "달콤씁쓸한", "개운한", "흐릿한",
    "밝은", "어두운", "반짝이는", "은빛의", "얼얼한", "알싸한", "톡톡 튀는", "아린", "텁텁한", "고풍스러운",
    "현대적인", "클래식한", "이국적인", "친숙한", "대중적인", "고급스러운", "저렴한", "귀한", "희귀한", "흔한",
    "독창적인", "전통적인", "혁신적인", "안정적인", "변화무쌍한", "균형 잡힌", "불균형한", "매력적인", "유혹적인", "중독적인",
    "상징적인", "역사적인", "전설적인", "기념비적인", "평범한", "특별한", "활기찬", "차분한", "진지한", "경쾌한"
)
private val alcoholNouns = listOf(
    "술", "와인", "맥주", "소주", "위스키", "럼", "진", "보드카", "브랜디", "리큐어",
    "샴페인", "막걸리", "사케", "칵테일", "에일", "라거", "포트", "셰리", "당밀", "포도",
    "보리", "쌀", "효모", "증류", "발효", "숙성", "오크통", "배럴", "증류소", "양조장",
    "와이너리", "바", "펍", "선술집", "잔", "글라스", "병", "코르크", "라벨", "빈티지",
    "아로마", "부케", "바디", "피니시", "타닌", "산도", "당도", "알코올", "도수", "원액",
    "블렌딩", "싱글몰트", "그레인", "몰트", "증류기", "냉각수", "필터", "캐스크", "증류주", "양주",
    "혼성주", "식전주", "식후주", "스피릿", "샷", "온더락", "하이볼", "칵테일잔", "오프너", "디캔터",
    "코스터", "냅킨", "얼음", "탄산수", "토닉워터", "콜라", "주스", "시럽", "가니시", "올리브",
    "레몬", "라임", "체리", "민트", "칵테일바", "바텐더", "소믈리에", "테이스팅", "시음", "페어링",
    "안주", "숙취", "알코올중독", "간", "기분", "분위기", "파티", "축배", "유흥", "문화"
)


private fun generateRandomNickname(): String {
    val adj  = adjectives.random()
    val noun = alcoholNouns.random()
    val num  = (1000..9999).random().toString().padStart(3, '0')
    return "$adj $noun $num"
}

@RequiresApi(Build.VERSION_CODES.O)
fun getKoreanAge(birthYear: Int, birthMonth: Int, birthDay: Int): Int {
    val today = LocalDate.now()
    val birthday = LocalDate.of(birthYear, birthMonth + 1, birthDay)
    var age = today.year - birthday.year
    if (today < birthday.withYear(today.year)) {
        age -= 1
    }
    return age
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    kakaoAccessToken: String,
    onSignupComplete: () -> Unit
) {
    val context = LocalContext.current
    val apiService = remember { ApiClient.api }
    val factory = remember { AuthViewModelFactory(apiService) }
    val viewModel: AuthViewModel = viewModel(factory = factory)

    var randomName by remember { mutableStateOf(generateRandomNickname()) }
    var gender by remember { mutableStateOf("남성") }
    val calendar = Calendar.getInstance().apply {
        add(Calendar.YEAR, -19)
    }
    var year by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var month by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var day by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    val age = getKoreanAge(year, month, day)

    var selectedProfileRes by remember { mutableIntStateOf(R.drawable.profile_image_basic) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val containerSize = LocalWindowInfo.current.containerSize
    val screenWidth = containerSize.width.dp

    val profileImages = listOf(
        R.drawable.profile_image_basic,
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text(
                text = "회원가입",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp),
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterHorizontally)
                    .background(Color(0xFFDDF0F0), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(selectedProfileRes),
                    contentDescription = "선택된 프로필 이미지",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(35.dp))
            Text("닉네임",
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            OutlinedTextField(
                value         = randomName,
                onValueChange = {},
                readOnly      = true,
                modifier      = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF717171)
                ),
                trailingIcon  = {
                    IconButton(onClick = {
                        randomName = generateRandomNickname()
                    }) {
                        Icon(
                            imageVector     = Icons.Default.Refresh,
                            contentDescription = "닉네임 재생성"
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFDDF0F0),
                    unfocusedBorderColor    = Color.Transparent,
                    disabledContainerColor  = Color(0xFFDDF0F0),
                    disabledBorderColor     = Color.Transparent,
                    focusedContainerColor   = Color(0xFFDDF0F0),
                    focusedBorderColor      = Color.Transparent,
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("성별",
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf("남성", "여성").forEach { option ->
                    val isSelected = gender == option
                    Surface(
                        color = if (isSelected) Color(0xFFB2DADA) else Color(0xFFDDF0F0),
                        tonalElevation = if (gender == option) 4.dp else 0.dp,
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                            .weight(1f)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null // Ripple 효과 제거
                            ) { gender = option }
                            .height(56.dp)
                    ) {
                        Text(
                            option,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(Alignment.CenterVertically),
                            fontFamily = PaperlogyFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Text(
                "성별은 최초 등록 이후 수정이 불가능합니다.",
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = Color(0xFF838383),
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text("생년월일",
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (age < 19) Color.Red else Color.Transparent,
                        shape = RoundedCornerShape(6.dp)
                    )
            ) {
                BirthdayPicker(
                    year = year,
                    month = month,
                    day = day,
                    onYearChange = { year = it },
                    onMonthChange = { month = it },
                    onDayChange = { day = it }
                )
            }
            if (age < 19) {
                Text(
                    "찰랑은 만19세 이상부터 가입할 수 있어요!",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.Red
                )
            } else {
                Text(
                    "찰랑은 만19세 이상부터 가입할 수 있어요!",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color(0xFF838383)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.kakaoSignup(
                            kakaoAccessToken = kakaoAccessToken,
                            nickname = randomName,
                            gender = if (gender == "남성") 1 else 0,
                            birthDate = String.format("%04d-%02d-%02d", year, month + 1, day),
                            onSuccess = {
                                onSignupComplete()
                            },
                            onError = { errorMsg ->
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                            },
                            onRetry = {
                                randomName = generateRandomNickname()
                                Toast.makeText(context, "닉네임이 중복되어 새로 생성했어요!", Toast.LENGTH_SHORT).show()
                            }


                        )
                    },
                    enabled = age >= 19,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB2DADA),
                        disabledContainerColor = Color(0xFFDDF0F0)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("확인",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}