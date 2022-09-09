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

private const val BASE_URL_EUROPE = "https://europe.api.riotgames.com"

//Development API key, needs to be regenerated every 24 hours and is not to be used in a live product
private const val API_KEY = "RGAPI-96f47c83-205f-4692-b6e3-17056f3bb690"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Retrofit instance for euw1 endpoint
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

//Retrofit instance for europe endpoint
private val retrofitEurope = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_EUROPE)
    .build()


interface RiotApiService {
    @GET("/lol/summoner/v4/summoners/by-name/{query}?api_key=$API_KEY")
    suspend fun getSummonerData(@Path("query") query: String): Response<SummonerData>

    @GET("/tft/league/v1/entries/by-summoner/{summonerId}?api_key=$API_KEY")
    suspend fun getRankedData(@Path("summonerId") summonerId: String): Response<List<RankedData>>

    @GET("/tft/match/v1/matches/by-puuid/{puuid}/ids?api_key=$API_KEY")
    suspend fun getMatchHistory(@Path("puuid") puuId: String) :Response<List<String>>

    @GET("/tft/match/v1/matches/{matchId}?api_key=$API_KEY")
    suspend fun getMatchData(@Path("matchId") matchId: String): Response<MatchData>
}


object RiotApi {
    val retrofitService: RiotApiService by lazy {
        retrofit.create(RiotApiService::class.java)
    }
}

object RiotApiEuropeRouting {
    val retrofitService: RiotApiService by lazy {
        retrofitEurope.create(RiotApiService::class.java)
    }
}