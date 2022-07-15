package com.example.searchviewapp.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private const val BASE_URL =
    "https://euw1.api.riotgames.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface RiotApiService {
    @GET("/lol/summoner/v4/summoners/by-name/{query}?api_key=RGAPI-da1d3f2f-8768-4392-a526-8dfbbfedaa0a")
    suspend fun getSummonerData(@Path("query") query: String): Response<SummonerData>

    @GET("/tft/league/v1/entries/by-summoner/{summonerId}?api_key=RGAPI-da1d3f2f-8768-4392-a526-8dfbbfedaa0a")
    suspend fun getRankedData(@Path("summonerId") summonerId: String): Response<List<RankedData>>
}

object RiotApi {
    val retrofitService: RiotApiService by lazy {
        retrofit.create(RiotApiService::class.java)
    }
}
