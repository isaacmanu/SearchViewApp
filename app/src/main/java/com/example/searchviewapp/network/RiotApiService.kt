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


//Routing for EUW
private const val BASE_URL =
    "https://euw1.api.riotgames.com"

//Development API key, needs to be regenerated every 24 hours and is not to be used in a live product
private const val API_KEY = "RGAPI-f8d864a2-cae1-4e55-bf12-34fb70aed51a"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RiotApiService {
    @GET("/lol/summoner/v4/summoners/by-name/{query}?api_key=$API_KEY")
    suspend fun getSummonerData(@Path("query") query: String): Response<SummonerData>

    @GET("/tft/league/v1/entries/by-summoner/{summonerId}?api_key=$API_KEY")
    suspend fun getRankedData(@Path("summonerId") summonerId: String): Response<List<RankedData>>

    @GET("/tft/match/v1/matches/by-puuid/{puuid}/ids?api_key=$API_KEY")
    suspend fun getMatchHistory(@Path("puuid") puuId: String) :Response<List<MatchHistory>>

    @GET("/tft/match/v1/matches/{matchId}?api_key=$API_KEY")
    suspend fun getMatchData(@Path("matchId") matchId: String): Response<MatchData>
}

object RiotApi {
    val retrofitService: RiotApiService by lazy {
        retrofit.create(RiotApiService::class.java)
    }
}
