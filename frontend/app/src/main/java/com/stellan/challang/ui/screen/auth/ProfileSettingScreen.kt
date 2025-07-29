package com.stellan.challang.ui.screen.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import kotlin.math.roundToInt
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.HorizontalDivider

import com.stellan.challang.ui.theme.PaperlogyFamily
import kotlinx.coroutines.delay

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableFloatStateOf

import androidx.compose.animation.core.*
import kotlin.math.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.stellan.challang.ui.viewmodel.TagViewModel
import com.stellan.challang.data.api.ApiClient
import  com.stellan.challang.data.repository.TagRepository
import com.stellan.challang.data.model.auth.TokenProvider
import com.stellan.challang.data.model.preference.PreferenceRequest
import com.stellan.challang.data.repository.PreferenceRepository
import android.util.Log
import com.stellan.challang.ui.viewmodel.PreferenceViewModel

//@Composable
//fun ProfileSettingScreen(
//    onProfileComplete: () -> Unit
//) {
//    // ApiClient 사용하도록 변경
//    val tagRepository = remember { TagRepository(ApiClient.tagApi) }
//    val viewModel = remember { TagViewModel(tagRepository) }
//
//    var step by remember { mutableIntStateOf(1) }
//
//    when (step) {
//        1 -> ProfileStepOne(onNext = { step = 2 })
//        2 -> ProfileStepTwo(
//            onValueSelected = {},
//            onNext = { step = 3 }
//        )
//        3 -> ProfileStepThree(
//            onNext = { step = 4 },
//            viewModel = viewModel
//        )
//        4 -> ProfileStepFour(onNext = onProfileComplete)
//    }
//}
//@Composable
//fun ProfileSettingScreen(
//    onProfileComplete: () -> Unit
//) {
//    val tagRepository = remember { TagRepository(ApiClient.tagApi) }
//    val tagViewModel = remember { TagViewModel(tagRepository) }
//    val preferenceRepository = remember { PreferenceRepository(ApiClient.preferenceApi) }
//    val preferenceViewModel = remember { PreferenceViewModel(preferenceRepository) }
//
//
//    // 🟡 상태 모아두기
//    var selectedTypeIds by remember { mutableStateOf<List<Int>>(emptyList()) }
//    var selectedLevelId by remember { mutableStateOf<Int?>(null) }
//    var selectedTagIds by remember { mutableStateOf<List<Int>>(emptyList()) }
//
//    var step by remember { mutableIntStateOf(1) }
//
//    when (step) {
//        1 -> ProfileStepOne(
//            onNext = { step = 2 },
//            onTypeSelected = { selectedTypeIds = it } // 🔼 선택된 주종 ID 저장
//        )
//        2 -> ProfileStepTwo(
//            onValueSelected = { selectedLevelId = it }, // 🔼 경험 수준 저장
//            onNext = { step = 3 }
//        )
//        3 -> ProfileStepThree(
//            viewModel = tagViewModel,
//            onNext = { step = 4 },
//            onTagSelected = { selectedTagIds = it } // 🔼 스타일 태그 ID 저장
//        )
//        4 -> ProfileStepFour(
//            onNext = {
//                val token = TokenProvider.getAccessToken() ?: return@ProfileStepFour
//                val request = PreferenceRequest(
//                    typeIds = selectedTypeIds,
//                    levelId = selectedLevelId ?: 0,
//                    tagIds = selectedTagIds
//                )
//                preferenceViewModel.submitPreference("Bearer $token", request)
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    val token = TokenProvider.getRefreshToken()  // ⏪ 여기 access token 사용해야 함
//                    try {
//                        if (!token.isNullOrBlank()) {
//                            preferenceRepository.submitPreference("Bearer $token", request)
//                            withContext(Dispatchers.Main) {
//                                onProfileComplete()
//                            }
//                        }
//                    } catch (e: Exception) {
//                        Log.e("PreferenceSubmit", "전송 실패", e)
//                    }
//                }
//
//            }
//        )
//    }
//}
@SuppressLint("RememberReturnType")
@Composable
fun ProfileSettingScreen(
    onProfileComplete: () -> Unit
) {
    val tagRepository = remember { TagRepository(ApiClient.tagApi) }
    val tagViewModel = remember { TagViewModel(tagRepository) }
    val preferenceRepository = remember { PreferenceRepository(ApiClient.preferenceApi) }
    val preferenceViewModel = remember { PreferenceViewModel(preferenceRepository) }

    val submitSuccess by preferenceViewModel.submitSuccess.collectAsState()
    val errorMessage by preferenceViewModel.errorMessage.collectAsState()

    LaunchedEffect(submitSuccess) {
        if (submitSuccess == true) {
            onProfileComplete()
        }
    }

    var selectedTypeIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var selectedLevelId by remember { mutableStateOf<Int?>(null) }
    var selectedTagIds by remember { mutableStateOf<List<Int>>(emptyList()) }
    var step by remember { mutableIntStateOf(1) }

    when (step) {
        1 -> ProfileStepOne(
            onNext = { step = 2 },
            onTypeSelected = { selectedTypeIds = it }
        )

        2 -> ProfileStepTwo(
            onValueSelected = { selectedLevelId = it },
            onNext = { step = 3 }
        )

        3 -> ProfileStepThree(
            viewModel = tagViewModel,
            onNext = { step = 4 },
            onTagSelected = { selectedTagIds = it }
        )

        4 -> ProfileStepFour(
            onNext = {
                val token = TokenProvider.getRefreshToken() ?: return@ProfileStepFour
                val request = PreferenceRequest(
                    typeIds = selectedTypeIds,
                    levelId = selectedLevelId ?: 0,
                    tagIds = selectedTagIds
                )
                preferenceViewModel.submitPreference("Bearer $token", request)
            }
        )
    }

    errorMessage?.let {
        Log.e("PreferenceSubmit", "에러: $it")
    }
}


@Composable
fun ProfileStepOne(
    onNext: () -> Unit,
    onTypeSelected: (List<Int>) -> Unit
) {
    val alcoholOptions = listOf(
        1 to "\uD83C\uDF7E소주",
        2 to "\uD83C\uDF78칵테일",
        3 to "\uD83E\uDD43위스키",
        4 to "\uD83C\uDF77와인·샴페인"
    )

    val selectedOptions = remember { mutableStateListOf<String>() }
    val selectedIds = remember { mutableStateListOf<Int>() }
    val isSelectionEnough = selectedOptions.size >= 3

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
                text = "프로필 설정",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, bottom = 6.dp),
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFD9D9D9)
            )
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "선호하는 주종을\n세가지 알려주세요!",
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black,
                lineHeight = 28.sp,
                modifier = Modifier.padding(bottom = 24.dp),
            )
            Spacer(modifier = Modifier.height(40.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                alcoholOptions.forEach { (id, option) ->
                    val isSelected = selectedIds.contains(id)
                    Surface(
                        shape = CircleShape,
                        color = if (isSelected) Color(0xFFB2DADA)
                        else Color(0xFFDDF0F0),
                        tonalElevation = if (isSelected) 4.dp else 0.dp,
                        modifier = Modifier
                            .size(width = 105.dp, height = 57.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                if (isSelected) {
                                    selectedIds.remove(id)
                                    selectedOptions.remove(option)
                                } else if (selectedOptions.size < 3) {
                                    selectedIds.add(id)
                                    selectedOptions.add(option)
                                }
                            }
                            .padding(horizontal = 1.dp, vertical = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontFamily = PaperlogyFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Button(
                    onClick = {
                        if (isSelectionEnough) {
                            onTypeSelected(selectedIds.toList())
                            onNext()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelectionEnough) Color(0xFFB2DADA)
                        else Color(0xFFDDF0F0)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        "다음",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


@Composable
fun ProfileStepTwo(
    onValueSelected: (Int) -> Unit,
    onNext: () -> Unit
) {
    val levels = listOf("고도수", "중도수", "저도수")
    val descriptions = listOf(
        "최저 도수 기준 25% 초과",
        "최저 도수 기준 15% 초과~25% 이하", "최저 도수 기준 15% 이하"
    )
    var sliderValue by remember { mutableFloatStateOf(1f) }
    val selectedIndex = sliderValue.roundToInt()
    val isSelectionMade = sliderValue in 0f..2f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Text(
            text = "프로필 설정",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, bottom = 6.dp),
            fontFamily = PaperlogyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = Color(0xFFD9D9D9)
        )
        Spacer(modifier = Modifier.height(56.dp))
        Text(
            text = "선호하는 도수를\n알려주세요!",
            fontFamily = PaperlogyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            color = Color.Black,
            lineHeight = 28.sp,
            modifier = Modifier.padding(bottom = 24.dp),
        )
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(230.dp)
                    .align(Alignment.Center)
                    .offset(x = (-168).dp)
                    .background(Color(0xFFDDF0F0), shape = RoundedCornerShape(4.dp))
            )
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                onValueChangeFinished = { onValueSelected(selectedIndex) },
                valueRange = 0f..2f,
                steps = 1,
                modifier = Modifier
                    .height(2.dp)
                    .width(240.dp)
                    .rotate(90f)
                    .align(Alignment.CenterStart)
                    .offset(y = 110.dp),
                colors = SliderDefaults.colors(
                    activeTrackColor = Color.Transparent,
                    inactiveTrackColor = Color.Transparent,
                    thumbColor = Color(0xFFB2DADA)
                )
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .offset(x = (-46).dp)
            ) {
                levels.forEachIndexed { index, label ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(40.dp))
                        Box(
                            modifier = Modifier
                                .size(25.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selectedIndex == index)
                                        Color(0xFFB2DADA)
                                    else
                                        Color(0xFFDDF0F0)
                                )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = label,
                                fontFamily = PaperlogyFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                color = Color.Black,
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = descriptions.getOrNull(index) ?: "",
                                fontFamily = PaperlogyFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp,
                                color = Color(0xFF838383),
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Button(
                onClick = {
                    if (isSelectionMade) {
                        onValueSelected(selectedIndex + 1)
                        onNext()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB2DADA)
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    "다음",
                    fontFamily = PaperlogyFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp,
                    color = Color.Black
                )
            }
        }
    }
}


@Composable
fun ProfileStepThree(
    onTagSelected: (List<Int>) -> Unit,
    onNext: () -> Unit,
    viewModel: TagViewModel
) {
    val tagList by viewModel.tagList.collectAsState()
    val alcoholOptions = tagList.map { it.name }
    val selectedOptions = remember { mutableStateListOf<String>() }
    val isSelectionEnough = selectedOptions.size >= 5

    LaunchedEffect(Unit) {
        val token = TokenProvider.getRefreshToken()
        if (!token.isNullOrBlank()) {
            viewModel.fetchTags("Bearer $token")
        }
    }


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
                text = "프로필 설정",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, bottom = 6.dp),
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFD9D9D9)
            )
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "선호하는 스타일을\n5가지 이상 알려주세요!",
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black,
                lineHeight = 28.sp,
                modifier = Modifier.padding(bottom = 24.dp),
            )
            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .height(460.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy((4).dp, Alignment.Start),
                    verticalArrangement = Arrangement.spacedBy((2).dp)
                ) {
                    alcoholOptions.forEach { option ->
                        val isSelected = selectedOptions.contains(option)
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) Color(0xFFB2DADA)
                            else Color(0xFFDDF0F0),
                            tonalElevation = if (isSelected) 4.dp else 0.dp,
                            modifier = Modifier
                                .height(57.dp)
                                .defaultMinSize(minWidth = 100.dp)
                                .wrapContentWidth(unbounded = true)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    if (isSelected) {
                                        selectedOptions.remove(option)
                                    } else {
                                        selectedOptions.add(option)
                                    }
                                }
                                .padding(horizontal = 2.dp, vertical = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 15.dp, vertical = 5.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option,
                                    fontFamily = PaperlogyFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            ) {
                Button(
                    onClick = {
                        if (isSelectionEnough) {
                            val selectedTagIds = selectedOptions.mapNotNull { name ->
                                tagList.find { it.name == name }?.id
                            }
                            onTagSelected(selectedTagIds)
                            onNext()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelectionEnough) Color(0xFFB2DADA)
                        else Color(0xFFDDF0F0)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        "확인",
                        fontFamily = PaperlogyFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 24.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileStepFour(
    onNext: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "wiggle")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    LaunchedEffect(Unit) {
        delay(3000L)
        onNext()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 370.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "당신만을 위한\n큐레이팅이 생성되고 있어요.\n잠시만 기다려주세요.",
            fontFamily = PaperlogyFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            lineHeight = 28.sp
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "프로필 설정",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp, bottom = 6.dp),
                fontFamily = PaperlogyFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color(0xFFD9D9D9)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(80.dp),
                contentAlignment = Alignment.Center
            ) {
                val wiggleX = 5.dp * cos(phase)
                val wiggleY = 5.dp * sin(phase)

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .offset(x = (-130).dp + wiggleX, y = 20.dp + wiggleY)
                        .background(Color(0xFFDDF0F0), shape = CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .offset(x = (-80).dp - wiggleX, y = (-65).dp + wiggleY)
                        .background(Color(0xFFB2DADA), shape = CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .offset(x = 100.dp + wiggleX, y = 350.dp - wiggleY)
                        .background(Color(0xFFB2DADA), shape = CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .offset(x = 20.dp - wiggleX, y = 425.dp + wiggleY)
                        .background(Color(0xFFB2DADA), shape = CircleShape)
                )
            }
        }
    }
}
