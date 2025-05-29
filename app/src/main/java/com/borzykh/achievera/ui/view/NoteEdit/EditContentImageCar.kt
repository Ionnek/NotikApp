/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteEdit

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.compose.ui.unit.dp
import com.borzykh.achievera.R
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.viewModel.NoteEditViewModel
import kotlin.math.abs

@Composable
fun imageCarousel(id:Long,
                  viewModel: NoteEditViewModel,
                  Ptpkr: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {
    val imageList by viewModel.images.collectAsState(initial = emptyList())
    fun intentDel(ListId: Int) {
        viewModel.DeletePhoto(
            imageList[ListId].id,
            imageList[ListId].noteId,
            imageList[ListId].imageUri
        )
    }

    val finalList = remember(imageList) {
        imageList + listOf(null)
    }
    var intentLastPhotoDelId by remember { mutableStateOf(0) }
    var intentLastPhotoDel by remember { mutableStateOf(false) }

    var scale by remember { mutableStateOf(100.dp) }
    var isPagerActive by remember { mutableStateOf(false) }
    val animatedSize by animateDpAsState(
        targetValue = scale,
        animationSpec = tween(
            durationMillis = 500,
            easing = FastOutSlowInEasing
        )
    )
    val configuration = LocalConfiguration.current
    var firstVisibleIndex by remember { mutableIntStateOf(0) }
    var allowInfiniteScroll by remember { mutableStateOf(false) }
    val screenWidth = configuration.screenWidthDp.dp
    val THRESHOLD = 0.5f

    LaunchedEffect(animatedSize) {

        val closeTo100dp = abs(animatedSize.value - 100.dp.value) < THRESHOLD
        if (closeTo100dp) {
            if (intentLastPhotoDel) {
                intentDel(intentLastPhotoDelId)
                intentLastPhotoDel = false
            }
            allowInfiniteScroll = false
        } else if (animatedSize.value > 100.dp.value + THRESHOLD) {
            allowInfiniteScroll = true
        }


        val closeToScreenWidth =
            abs(animatedSize.value - (screenWidth.value - 32.dp.value)) < THRESHOLD
        if (closeToScreenWidth) {
            isPagerActive = true
        } else if (animatedSize.value < (screenWidth.value - 32.dp.value) - THRESHOLD && isPagerActive) {
            isPagerActive = false
            allowInfiniteScroll = true
        }
    }

    if(imageList.isNotEmpty()){
    if (!isPagerActive) {
        val listState = rememberLazyListState(
            initialFirstVisibleItemIndex = firstVisibleIndex,
        )
        Box(
            Modifier.height(10.dp).fillMaxWidth().background(color = BackgroundColor)
        ) {}
        Box(modifier = Modifier.fillMaxWidth()) {
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, end = 18.dp)
                    .background(color = Color.Transparent),
                flingBehavior = rememberSnapFlingBehavior(
                    lazyListState = listState,
                    snapPosition = SnapPosition.Start
                ),
            ) {
                itemsIndexed(finalList) { index, item ->
                    if (item != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageList[index].imageUri)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Image $index",
                            modifier = Modifier
                                .size(
                                    if (index == firstVisibleIndex) {
                                        animatedSize
                                    } else {
                                        200.dp - animatedSize
                                    }
                                )
                                .clickable {
                                    scale = (screenWidth - 32.dp)
                                    firstVisibleIndex = index
                                },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Image(
                                painter = painterResource(R.drawable.group_471),
                                contentDescription = "Image $index",
                                modifier = Modifier
                                    .size(
                                        if (index != firstVisibleIndex) {
                                            200.dp - animatedSize
                                        } else {
                                            animatedSize
                                        }
                                    )
                                    .clickable {
                                        val request =
                                            PickVisualMediaRequest(PickVisualMedia.ImageOnly)
                                        Ptpkr.launch(request)
                                    },
                                contentScale = ContentScale.Crop
                            )
                            if (allowInfiniteScroll) {
                                Box(
                                    modifier = Modifier.fillMaxHeight()
                                        .width(animatedSize * 2 - 200.dp)
                                        .background(color = Color.Transparent)
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        val pagerState = rememberPagerState(
            initialPage = firstVisibleIndex,
            pageCount = { imageList.size }
        )
        Box(
            Modifier.height(10.dp).fillMaxWidth().background(color = BackgroundColor)
        ) {}
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .size(animatedSize),
            ) { page ->
                Box(

                    modifier = Modifier
                        .size(animatedSize)
                        .padding(0.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageList[page].imageUri)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Image $page",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(animatedSize)
                            .clickable {
                                scale = 100.dp
                                firstVisibleIndex = page
                            }
                    )
                    Image(
                        painter = painterResource(id = R.drawable.group_472_2),
                        contentDescription = "Icon desc",
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clickable {
                                if (imageList.size == 1) {
                                    intentLastPhotoDel = true
                                    intentLastPhotoDelId = page
                                    scale = 100.dp
                                } else {
                                    viewModel.DeletePhoto(
                                        imageList[page].id,
                                        imageList[page].noteId,
                                        imageList[page].imageUri
                                    )
                                }
                            },
                    )
                }
            }
        }
    }
}
}
