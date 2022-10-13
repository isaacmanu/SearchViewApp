package com.example.searchviewapp.network

import com.example.searchviewapp.network.model.MatchData
import com.example.searchviewapp.network.model.MatchHistory
import com.example.searchviewapp.network.model.RankedData
import com.example.searchviewapp.network.model.SummonerData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

//Backend server hosted on deta (https://www.deta.sh/)
private const val BASE_URL = "https://4u2rwv.deta.dev/"

//Moshi object to handle JSON response
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Retrofit instance for euw1 endpoint
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

/*
Using the username given by the user we use Retrofit to send GET requests to different endpoints.
getSummonerData is the only request that requires user data, the rest use values taken from
previous requests
*/
interface RiotApiService {
    @GET("/euw/get-user-data/{query}")
    suspend fun getSummonerData(@Path("query") query: String): Response<SummonerData>

    @GET("/euw/get-ranked-data/{summonerId}")
    suspend fun getRankedData(@Path("summonerId") summonerId: String): Response<List<RankedData>>

    @GET("/euw/get-match-history/{puuid}")
    suspend fun getMatchHistory(@Path("puuid") puuId: String) :Response<List<String>>

    @GET("/euw/get-match-data/{matchId}")
    suspend fun getMatchData(@Path("matchId") matchId: String): Response<MatchData>
}

//Public objects which extend the api service to the rest of the app (namely the viewmodel)
object RiotApi {
    val retrofitService: RiotApiService by lazy {
        retrofit.create(RiotApiService::class.java)
    }
}
