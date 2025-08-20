package com.stellan.challang.ui.screen.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.stellan.challang.ui.component.StarRating

@Composable
fun WriteReviewScreen(
    drinkId: String,
    onSubmit: () -> Unit
) {
    var rating by rememberSaveable { mutableIntStateOf(0) }

    var review by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val minLen = 5
    val maxLen = 300

    var images by rememberSaveable(
        stateSaver = listSaver(
            save = { list -> list.map { it.toString() } },
            restore = { list -> list.map(Uri::parse) }
        )
    ) { mutableStateOf(emptyList<Uri>()) }

    val selectedTag = rememberSaveable(
        saver = listSaver(
            save = { it.toList() },
            restore = { it.toMutableStateList() }
        )
    ) { mutableStateListOf<String>() }

    LazyColumn(Modifier.fillMaxWidth()) {
        item {
            Text(
                "리뷰",
                fontWeight = FontWeight.Normal,
                fontSize = 25.sp,
                modifier = Modifier.padding(top = 30.dp, start = 30.dp, bottom = 15.dp)
            )
            HorizontalDivider(
                Modifier.fillMaxWidth(),
                thickness = 14.dp,
                color = Color(0xFFDEEEEE)
            )
        }

        item {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 15.dp)
            ) {
                Text(
                    "별점",
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(10.dp))
                StarRating(
                    rating = rating,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    onRatingChanged = { new -> rating = if (new == rating) 0 else new }
                )
            }
            Spacer(Modifier.height(10.dp))
        }

        item {
            val scroll = rememberScrollState()
            LaunchedEffect(review.text, review.selection) {
                val caretAtEnd = review.selection.collapsed &&
                        review.selection.end == review.text.length
                if (caretAtEnd) {
                    scroll.animateScrollTo(scroll.maxValue)
                }
            }

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                Text(
                    "리뷰 작성",
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(10.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(Color(0xFFDEEEEE))
                ) {
                    BasicTextField(
                        value = review,
                        onValueChange = { value ->
                            review = if (value.text.length <= maxLen) value
                            else value.copy(
                                text = value.text.take(maxLen),
                                selection = TextRange(maxLen)
                            ) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 38.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        decorationBox = { innerTextField ->
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .verticalScroll(scroll)
                            ) {
                                if (review.text.isEmpty()) {
                                    Text(
                                        "리뷰를 입력하세요",
                                        fontWeight = FontWeight.Normal,
                                        color = Color(0xFF868686),
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    Text(
                        "${review.text.length}/$maxLen",
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp)
                    )
                }
                Text(
                    "최소 ${minLen}자 최대 ${maxLen}자",
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = if (review.text.length < minLen) Color(0xFFD9534F)
                    else Color(0xFF868686),
                    modifier = Modifier.align(Alignment.End)
                )
            }
            Spacer(Modifier.height(20.dp))
        }

        item {
            var previewIndex by rememberSaveable { mutableStateOf<Int?>(null) }
            val maxCount = 3
            val picker = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxCount),
                onResult = { uris ->
                    if (uris.isEmpty()) return@rememberLauncherForActivityResult
                    val remaining = maxCount - images.size
                    if (remaining > 0) {
                        images = images + uris.take(remaining)
                    }
                }
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(
                            "사진 첨부",
                            fontWeight = FontWeight.Normal,
                            fontSize = 20.sp
                        )
                    }
                    Text(
                        "${images.size}장 / 최대 ${maxCount}장",
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color(0xFF868686)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val cornerRadius = 8.dp
                    val strokeWidth = 1.5.dp
                    val dash = 6.dp
                    val gap = 4.dp
                    val color = Color(0xFF868686)

                    if (images.size < maxCount) {
                        Box(
                            Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(cornerRadius))
                                .clickable{
                                    picker.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .drawBehind {
                                    val stroke = Stroke(
                                        width = strokeWidth.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(dash.toPx(), gap.toPx())
                                        )
                                    )
                                    drawRoundRect(
                                        color = color,
                                        size = size,
                                        style = stroke,
                                        cornerRadius = CornerRadius(
                                            cornerRadius.toPx(), cornerRadius.toPx()
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                    images.forEachIndexed { idx, uri ->
                        Box(
                            Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(cornerRadius))
                                .clickable { previewIndex = idx }
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "첨부된 사진 ${idx + 1}",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            val showPreview = previewIndex != null
            if (showPreview) {
                val index = previewIndex!!
                Dialog(
                    onDismissRequest = { previewIndex = null },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                    ) {
                        AsyncImage(
                            model = images[index],
                            contentDescription = "미리보기",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(0.dp)
                        )
                        IconButton(
                            onClick = { previewIndex = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "닫기",
                                tint = Color.White
                            )
                        }
                        FilledTonalButton(
                            onClick = {
                                images = images.toMutableList().also { it.removeAt(index) }
                                previewIndex = null
                            },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                            Spacer(Modifier.width(6.dp))
                            Text("이 사진 삭제", color = Color.Black)
                        }
                    }
                }
            }
        }

        item {
            val allTags = listOf("단맛", "산미", "과실향", "허브", "스모키", "스파이시")
            val maxSelect = 3

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 20.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        "해시태그",
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                    Text(
                        "최소 1개, 최대 3개 선택해주세요.",
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = if (selectedTag.isEmpty()) Color(0xFFD9534F)
                        else Color(0xFF868686)
                    )
                }
                Spacer(Modifier.height(10.dp))
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    allTags.forEach { tag ->
                        val isSelected = tag in selectedTag
                        val canSelectMore = selectedTag.size < maxSelect

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                if (isSelected) { selectedTag.remove(tag) }
                                else if (canSelectMore) { selectedTag.add(tag) }
                            },
                            enabled = isSelected || canSelectMore,
                            label = { Text(
                                "#$tag",
                                fontSize = 20.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            ) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFDEEEEE),
                                selectedContainerColor = Color(0xFFB2DADA),
                                disabledContainerColor = Color(0xFFDEEEEE)
                            ),
                            border = BorderStroke(0.dp, Color.Transparent),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier.height(45.dp).width(105.dp)
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = { onSubmit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .padding(horizontal = 30.dp),
                enabled = review.text.length in minLen..maxLen && selectedTag.isNotEmpty(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB2DADA),
                    disabledContainerColor = Color(0xFFDEEEEE)
                )
            ) {
                Text(
                    "등록",
                    fontWeight = FontWeight.Normal,
                    fontSize = 25.sp,
                    color = Color.Black
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}