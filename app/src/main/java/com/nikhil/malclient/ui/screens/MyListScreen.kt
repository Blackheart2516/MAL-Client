package com.nikhil.malclient.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale

import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

import com.nikhil.malclient.viewmodel.MyListViewModel

import kotlinx.coroutines.launch


@Composable
fun MyListScreen(
    token: String
) {

    val viewModel: MyListViewModel = viewModel()

    val myList by viewModel.myList.collectAsState()


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


    val scope = rememberCoroutineScope()



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

                            color = if (selectedTab == index)

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

        ) {


            LazyColumn(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),


                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {


                items(

                    myList?.data ?: emptyList()

                ) { item ->


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



                                Text(

                                    text = "⭐ ${item.list_status.score}",

                                    color = Color(0xFFFFD54F)

                                )



                                Text(

                                    text = "Episodes: ${item.list_status.num_episodes_watched}/${item.node.num_episodes ?: "?"}",

                                    color = Color(0xFFBDBDBD)

                                )



                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )



                                LinearProgressIndicator(

                                    progress = {

                                        if ((item.node.num_episodes ?: 0) > 0)

                                            item.list_status.num_episodes_watched.toFloat() /
                                                    item.node.num_episodes!!.toFloat()
                                        else

                                            0f

                                    },


                                    modifier = Modifier
                                        .fillMaxWidth(),


                                    color = Color(0xFF42A5F5)

                                )

                            }

                        }

                    }

                }

            }

        }

    }

}
