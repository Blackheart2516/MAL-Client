package com.nikhil.malclient.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.nikhil.malclient.viewmodel.MyListViewModel

import kotlinx.coroutines.launch
import androidx.compose.material.ExperimentalMaterialApi



@Composable
@OptIn(ExperimentalMaterialApi::class)
fun MyListScreen(
    token: String
) {


    val viewModel: MyListViewModel = viewModel()



    val allList by viewModel.allList.collectAsState()

    val watchingList by viewModel.watchingList.collectAsState()

    val completedList by viewModel.completedList.collectAsState()

    val planList by viewModel.planList.collectAsState()



    val statuses = listOf(
        null,
        "watching",
        "completed",
        "plan_to_watch"
    )



    var selectedTab by remember {

        mutableIntStateOf(0)

    }



    val pagerState = rememberPagerState(

        initialPage = 0,

        pageCount = {

            4

        }

    )



    val allScrollState = rememberLazyListState()

    val watchingScrollState = rememberLazyListState()

    val completedScrollState = rememberLazyListState()

    val planScrollState = rememberLazyListState()



    val scope = rememberCoroutineScope()


    var isRefreshing by remember {
        mutableStateOf(false)
    }


    val refreshState = rememberPullRefreshState(

        refreshing = isRefreshing,

        onRefresh = {

            scope.launch {

                isRefreshing = true


                viewModel.refreshMyList(

                    token = token,

                    status = statuses[pagerState.currentPage]

                )


                isRefreshing = false

            }

        }
    )



    LaunchedEffect(Unit) {

        viewModel.loadMyList(token)

    }



    LaunchedEffect(pagerState.currentPage) {


        selectedTab = pagerState.currentPage


        viewModel.loadMyList(

            token,

            statuses[pagerState.currentPage]

        )

    }
    Column(

        modifier = Modifier

            .fillMaxSize()

            .background(Color(0xFF0B1628))

    ) {



        Spacer(

            modifier = Modifier.height(40.dp)

        )



        TabRow(

            selectedTabIndex = selectedTab,

            containerColor = Color(0xFF111F33),

            divider = {}

        ) {



            listOf(

                "All",

                "Watching",

                "Completed",

                "Plan"

            ).forEachIndexed { index, title ->



                Tab(

                    selected = selectedTab == index,


                    onClick = {


                        scope.launch {

                            pagerState.animateScrollToPage(index)

                        }

                    },


                    modifier = Modifier.weight(1f),


                    text = {


                        Text(

                            text = title,

                            fontSize = 12.sp,

                            maxLines = 1,


                            color = if(selectedTab == index)

                                Color.White

                            else

                                Color(0xFF8A8A8A)

                        )

                    }

                )

            }

        }




        HorizontalPager(

            state = pagerState,

            modifier = Modifier.fillMaxSize()

        ) { page ->



            val currentScrollState = when(page) {


                0 -> allScrollState


                1 -> watchingScrollState


                2 -> completedScrollState


                else -> planScrollState


            }




            Box(

                modifier = Modifier

                    .fillMaxSize()

                    .pullRefresh(refreshState)

            ) {


                LazyColumn(

                    state = currentScrollState,

                    modifier = Modifier

                        .fillMaxSize()

                        .padding(8.dp),

                    verticalArrangement = Arrangement.spacedBy(8.dp)

                )  {



                items(

                    items = when(page) {


                        0 -> allList?.data ?: emptyList()


                        1 -> watchingList?.data ?: emptyList()


                        2 -> completedList?.data ?: emptyList()


                        else -> planList?.data ?: emptyList()


                    },


                    key = { item ->

                        item.node.id

                    }

                ) { item ->



                    LaunchedEffect(item.node.id) {


                        viewModel.loadAiredEpisodes(

                            animeId = item.node.id

                        )

                    }




                    val totalEpisodes =

                        item.node.num_episodes ?: 0




                    val airedEpisodes =

                        viewModel.airedEpisodesMap[item.node.id] ?: 0




                    val watchedEpisodes =

                        (

                                viewModel.episodeUpdates[item.node.id]

                                    ?: item.list_status.num_episodes_watched

                                )

                            .coerceAtMost(

                                if(airedEpisodes > 0)

                                    airedEpisodes

                                else

                                    totalEpisodes

                            )




                    Card(

                        modifier = Modifier

                            .fillMaxWidth(),


                        shape = RoundedCornerShape(12.dp),


                        colors = CardDefaults.cardColors(

                            containerColor = Color(0xFF14243A)

                        )

                    ) {



                        Row(

                            modifier = Modifier

                                .padding(10.dp)

                        ) {



                            AsyncImage(

                                model = item.node.main_picture?.medium,

                                contentDescription = item.node.title,


                                modifier = Modifier

                                    .width(100.dp)

                                    .height(150.dp),


                                contentScale = ContentScale.Crop

                            )

                            Spacer(

                                modifier = Modifier.width(12.dp)

                            )



                            Column(

                                modifier = Modifier.weight(1f)

                            ) {



                                Text(

                                    text = item.node.title,

                                    color = Color(0xFF42A5F5),

                                    fontWeight = FontWeight.Bold,

                                    fontSize = 14.sp,

                                    maxLines = 1

                                )



                                Spacer(

                                    modifier = Modifier.height(6.dp)

                                )



                                Text(

                                    text = item.list_status.status.uppercase(),

                                    color = Color(0xFF8FA8C8),

                                    fontSize = 11.sp

                                )



                                Spacer(

                                    modifier = Modifier.height(6.dp)

                                )



                                Row(

                                    verticalAlignment = Alignment.CenterVertically

                                ) {



                                    Text(

                                        text = "⭐ ${item.list_status.score}",

                                        color = Color(0xFFFFD54F)

                                    )



                                    Spacer(

                                        modifier = Modifier.width(12.dp)

                                    )



                                    Button(

                                        onClick = {


                                            if (

                                                watchedEpisodes < airedEpisodes ||

                                                airedEpisodes == 0

                                            ) {


                                                val newEpisodeCount =

                                                    watchedEpisodes + 1



                                                viewModel.updateLocalEpisode(

                                                    animeId = item.node.id,

                                                    episodes = newEpisodeCount

                                                )



                                                viewModel.updateEpisodeProgress(

                                                    token = token,

                                                    animeId = item.node.id.toString(),

                                                    episodes = newEpisodeCount

                                                )

                                            }


                                        },


                                        modifier = Modifier.height(30.dp),


                                        contentPadding = PaddingValues(

                                            horizontal = 10.dp,

                                            vertical = 2.dp

                                        )

                                    ) {


                                        Text(

                                            text = "+1",

                                            fontSize = 12.sp

                                        )

                                    }

                                }




                                Text(

                                    text = "Episodes: $watchedEpisodes / ${
                                        if (airedEpisodes > 0)

                                            airedEpisodes

                                        else

                                            "?"

                                    } / ${
                                        if (totalEpisodes > 0)

                                            totalEpisodes

                                        else

                                            "?"
                                    }",


                                    color = Color(0xFFBDBDBD)

                                )



                                Spacer(

                                    modifier = Modifier.height(8.dp)

                                )



                                val progress =

                                    if (totalEpisodes > 0)

                                        watchedEpisodes.toFloat() /

                                                totalEpisodes.toFloat()

                                    else

                                        0f




                                val animatedProgress by animateFloatAsState(

                                    targetValue = progress,

                                    animationSpec = tween(500)

                                )



                                LinearProgressIndicator(

                                    progress = animatedProgress,


                                    modifier = Modifier

                                        .fillMaxWidth()

                                        .height(6.dp),


                                    color = Color(0xFF42A5F5),


                                    trackColor = Color(0xFF24364D)

                                )



                            }

                        }

                    }

                }

            }
                PullRefreshIndicator(

                    refreshing = isRefreshing,

                    state = refreshState,

                    modifier = Modifier.align(
                        Alignment.TopCenter
                    )

                )

            }

        }

    }

}

