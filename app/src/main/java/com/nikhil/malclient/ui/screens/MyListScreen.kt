package com.nikhil.malclient.ui.screens

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.nikhil.malclient.viewmodel.AniListViewModel
import com.nikhil.malclient.viewmodel.MyListViewModel
import kotlinx.coroutines.launch


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MyListScreen(
    token: String,
    myListViewModel: MyListViewModel,
    navController: androidx.navigation.NavController
) {


    Log.d(
        "MYLIST_TEST",
        "MyListScreen opened"
    )



    val context =
        LocalContext.current



    val lifecycleOwner =
        LocalLifecycleOwner.current



    DisposableEffect(lifecycleOwner) {


        val observer =
            LifecycleEventObserver { _, event ->


                if(event == Lifecycle.Event.ON_RESUME) {


                    myListViewModel.loadMyList(
                        token = token,
                        forceRefresh = true
                    )


                }

            }



        lifecycleOwner.lifecycle.addObserver(observer)



        onDispose {

            lifecycleOwner.lifecycle.removeObserver(observer)

        }


    }





    val aniListViewModel: AniListViewModel = viewModel(

        factory = object : ViewModelProvider.Factory {

            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>
            ): T {


                return AniListViewModel(
                    context
                ) as T


            }

        }

    )





    val allList by myListViewModel.allList.collectAsState()

    val watchingList by myListViewModel.watchingList.collectAsState()

    val completedList by myListViewModel.completedList.collectAsState()

    val planList by myListViewModel.planList.collectAsState()

    val onHoldList by myListViewModel.onHoldList.collectAsState()

    val droppedList by myListViewModel.droppedList.collectAsState()



    val statuses = listOf(

            null,

    "watching",

    "completed",

    "plan_to_watch",

    "on_hold",

    "dropped"

    )
    var selectedTab by remember {

        mutableIntStateOf(0)

    }





    val pagerState =
        rememberPagerState(

            initialPage = 0,

            pageCount = {

                6

            }

        )





    val allScrollState =
        rememberLazyListState()


    val watchingScrollState =
        rememberLazyListState()


    val completedScrollState =
        rememberLazyListState()


    val onHoldScrollState =
        rememberLazyListState()


    val droppedScrollState =
        rememberLazyListState()


    val planScrollState =
        rememberLazyListState()





    val scope =
        rememberCoroutineScope()





    var isRefreshing by remember {

        mutableStateOf(false)

    }






    val onRefresh: () -> Unit = {


        scope.launch {


            isRefreshing = true



            myListViewModel.refreshMyList(

                token,

                statuses[pagerState.currentPage]

            )



            isRefreshing = false


        }


    }






    LaunchedEffect(Unit) {


        myListViewModel.loadMyList(

            token

        )


    }






    LaunchedEffect(pagerState.currentPage) {


        selectedTab =
            pagerState.currentPage



        myListViewModel.loadMyList(

            token,

            statuses[pagerState.currentPage]

        )


    }







    Column(

        modifier = Modifier

            .fillMaxSize()

            .background(
                Color(0xFF0B1628)
            )

    ) {



        Spacer(

            modifier = Modifier.height(40.dp)

        )






        ScrollableTabRow(

            selectedTabIndex = selectedTab,

            containerColor = Color(0xFF111F33),

            edgePadding = 8.dp,

            divider = {}

        ) {


            listOf(

                "All",

                "Watching",

                "Completed",

                "Plan to Watch",

                "On-Hold",

                "Dropped"

            ).forEachIndexed { index, title ->



                Tab(

                    selected = selectedTab == index,


                    onClick = {


                        scope.launch {


                            pagerState.animateScrollToPage(
                                index
                            )


                        }


                    },


                    text = {


                        Text(

                            text = title,

                            fontSize = 13.sp,

                            color =

                                if(selectedTab == index)

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

                0 ->
                    allScrollState

                1 ->
                    watchingScrollState

                2 ->
                    completedScrollState

                3 ->
                    planScrollState

                4 ->
                    onHoldScrollState

                else ->
                    droppedScrollState

            }






            PullToRefreshBox(

                isRefreshing = isRefreshing,

                onRefresh = onRefresh,

                modifier =
                    Modifier.fillMaxSize()

            ) {



                LazyColumn(

                    state = currentScrollState,

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(8.dp),


                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)

                ) {



                    items(



                        items = when(page) {

                            0 ->
                                allList?.data ?: emptyList()

                            1 ->
                                watchingList?.data ?: emptyList()

                            2 ->
                                completedList?.data ?: emptyList()

                            3 ->
                                planList?.data ?: emptyList()

                            4 ->
                                onHoldList?.data ?: emptyList()

                            else ->
                                droppedList?.data ?: emptyList()

                        },



                        key = {

                                item ->

                            item.node.id

                        }


                    ) { item ->





                        LaunchedEffect(item.node.id) {


                            aniListViewModel.loadAiredEpisodes(

                                item.node.id

                            )


                        }





                        val totalEpisodes =
                            item.node.num_episodes ?: 0





                        val airedEpisodes =

                            aniListViewModel
                                .airedEpisodesMap[item.node.id]

                                ?: totalEpisodes






                        val watchedEpisodes =

                            item.list_status
                                .num_episodes_watched
                                .coerceAtMost(

                                    if(airedEpisodes > 0)

                                        airedEpisodes

                                    else

                                        totalEpisodes

                                )






                        Card(

                            modifier = Modifier

                                .fillMaxWidth()

                                .clickable {


                                    AnimeListSession.setAnimeStatus(

                                        animeId = item.node.id,

                                        status = item.list_status.status

                                    )


                                    AnimeListSession.setWatchedEpisodes(

                                        animeId = item.node.id,

                                        episodes = item.list_status.num_episodes_watched

                                    )



                                    navController.navigate(

                                        "details/${item.node.id}"

                                    )


                                },


                            shape =
                                RoundedCornerShape(12.dp),



                            colors =
                                CardDefaults.cardColors(

                                    containerColor =
                                        Color(0xFF14243A)

                                )


                        ) {



                            Row(

                                modifier =
                                    Modifier.padding(10.dp)

                            ) {



                                AsyncImage(

                                    model =
                                        item.node.main_picture?.medium,


                                    contentDescription =
                                        item.node.title,


                                    modifier =
                                        Modifier

                                            .width(100.dp)

                                            .height(150.dp),


                                    contentScale =
                                        ContentScale.Crop


                                )





                                Spacer(

                                    modifier =
                                        Modifier.width(12.dp)

                                )





                                Column(

                                    modifier =
                                        Modifier.weight(1f)

                                ) {



                                    Text(

                                        text =
                                            item.node.title,


                                        color =
                                            Color(0xFF42A5F5),


                                        fontWeight =
                                            FontWeight.Bold,


                                        fontSize =
                                            14.sp


                                    )





                                    Spacer(

                                        modifier =
                                            Modifier.height(6.dp)

                                    )





                                    Text(

                                        text =
                                            item.list_status.status
                                                .replace("_", " ")
                                                .uppercase(),


                                        color =
                                            Color(0xFF8FA8C8),


                                        fontSize =
                                            11.sp


                                    )





                                    Spacer(

                                        modifier =
                                            Modifier.height(6.dp)

                                    )





                                    Row(

                                        verticalAlignment =
                                            Alignment.CenterVertically

                                    ) {



                                        Text(

                                            text =
                                                "⭐ ${item.list_status.score}",


                                            color =
                                                Color(0xFFFFD54F)

                                        )





                                        Spacer(

                                            modifier =
                                                Modifier.width(12.dp)

                                        )





                                        Button(

                                            onClick = {


                                                val newEpisodeCount =
                                                    watchedEpisodes + 1



                                                myListViewModel.updateLocalEpisode(

                                                    item.node.id,

                                                    newEpisodeCount

                                                )



                                                myListViewModel.updateEpisodeProgress(

                                                    token,

                                                    item.node.id.toString(),

                                                    newEpisodeCount

                                                )


                                            },


                                            modifier =
                                                Modifier.height(30.dp)

                                        ) {


                                            Text("+1")


                                        }


                                    }






                                    Text(

                                        text =
                                            "Episodes: $watchedEpisodes / ${
                                                if(airedEpisodes > 0)
                                                    airedEpisodes
                                                else
                                                    "?"
                                            } / ${
                                                if(totalEpisodes > 0)
                                                    totalEpisodes
                                                else
                                                    "?"
                                            }",


                                        color =
                                            Color(0xFFBDBDBD)


                                    )





                                    Spacer(

                                        modifier =
                                            Modifier.height(8.dp)

                                    )





                                    val progress =

                                        if(totalEpisodes > 0)

                                            watchedEpisodes.toFloat() /
                                                    totalEpisodes.toFloat()

                                        else

                                            0f





                                    val animatedProgress by animateFloatAsState(

                                        targetValue = progress,

                                        animationSpec =
                                            tween(500)

                                    )





                                    LinearProgressIndicator(

                                        progress =
                                            animatedProgress,


                                        modifier =
                                            Modifier

                                                .fillMaxWidth()

                                                .height(6.dp)


                                    )


                                }


                            }


                        }


                    }


                }


            }


        }


    }


}