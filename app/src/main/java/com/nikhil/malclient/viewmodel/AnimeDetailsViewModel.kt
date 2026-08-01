package com.nikhil.malclient.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikhil.malclient.model.Anime
import com.nikhil.malclient.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.nikhil.malclient.user.AnimeListManager


class AnimeDetailsViewModel : ViewModel() {


    private val repository =
        AnimeRepository()



    private val _anime =
        MutableStateFlow<Anime?>(null)


    val anime =
        _anime.asStateFlow()



    private val _loading =
        MutableStateFlow(false)


    val loading =
        _loading.asStateFlow()

    fun removeFromList(
        token: String,
        animeId: String,
        onResult: (Boolean) -> Unit
    ) {


        // instant UI update
        AnimeListManager.removeAnime(

            animeId.toInt()

        )


        viewModelScope.launch {


            try {


                val response =
                    repository.removeAnimeFromList(

                        token = token,

                        animeId = animeId

                    )


                Log.d(
                    "REMOVE_LIST",
                    "success=${response.isSuccessful}"
                )


                onResult(
                    response.isSuccessful
                )


            }
            catch(e: Exception) {


                Log.e(
                    "REMOVE_LIST_ERROR",
                    e.message ?: "error"
                )


                onResult(false)

            }


        }


    }

    fun updateAnimeStatus(
        token: String,
        animeId: String,
        status: String
    ) {


        viewModelScope.launch {


            try {


                val response =
                    repository.addAnimeToList(

                        token = token,

                        animeId = animeId,

                        status = status

                    )



                Log.d(
                    "STATUS_UPDATE",
                    "status=$status success=${response.isSuccessful}"
                )


                if(response.isSuccessful) {


                    Log.d(
                        "STATUS_UPDATE",
                        "Updated successfully"
                    )


                }



            }
            catch(e: Exception) {


                Log.e(
                    "STATUS_UPDATE",
                    e.message ?: "error"
                )


            }


        }


    }

    fun loadAnimeDetails(
        token: String,
        animeId: String
    ) {

        Log.d(
            "DETAIL_VM",
            "loading id=$animeId"
        )


        if (_loading.value) {
            return
        }



        viewModelScope.launch {


            try {


                _loading.value = true



                val response =
                    repository.getAnimeDetails(
                        token,
                        animeId
                    )



                Log.d(
                    "ANIME_DETAILS",
                    "code=${response.code()} body=${response.body()}"
                )



                if (
                    response.isSuccessful &&
                    response.body() != null
                ) {


                    _anime.value =
                        response.body()


                }
                else {


                    Log.e(
                        "ANIME_DETAILS",
                        "API failed ${response.code()}"
                    )

                }




            }
            catch (e: Exception) {


                Log.e(
                    "ANIME_DETAILS",
                    e.message ?: "Unknown error"
                )


            }
            finally {


                _loading.value = false


            }


        }


    }





    fun addToList(
        token: String,
        animeId: String,
        status: String,
        onResult: (Boolean) -> Unit
    ) {

        viewModelScope.launch {

            try {


                Log.d(
                    "ADD_LIST",
                    "sending status=$status animeId=$animeId"
                )


                val response =
                    repository.addAnimeToList(
                        token = token,
                        animeId = animeId,
                        status = status
                    )


                Log.d(
                    "ADD_LIST",
                    "code=${response.code()} success=${response.isSuccessful}"
                )


                onResult(
                    response.isSuccessful
                )


            } catch (e: Exception) {


                Log.e(
                    "ADD_LIST_ERROR",
                    e.message ?: "Unknown error"
                )


                onResult(false)

            }

        }

    }


}