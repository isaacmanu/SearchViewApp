package com.isaacmanu.tftstatsapp.network

import com.isaacmanu.tftstatsapp.network.model.MatchData
import com.isaacmanu.tftstatsapp.network.model.RankedData
import com.isaacmanu.tftstatsapp.network.model.SummonerData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

//Backend server hosted on deta (https://www.deta.sh/)
private const val BASE_URL_EUROPE = "https://4u2rwv.deta.dev/euw/"
private const val BASE_URL_KR = "https://4u2rwv.deta.dev/kr/"
private const val BASE_URL_NA = "https://4u2rwv.deta.dev/na/"
private const val BASE_URL_OCE = "https://4u2rwv.deta.dev/oce/"

//Moshi object to handle JSON response
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//Retrofit instance for EUW endpoint
private val retrofitEurope = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_EUROPE)
    .build()

//Retrofit instance for NA endpoint
private val retrofitNorthAmerica = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_NA)
    .build()

//Retrofit instance for OCE endpoint
private val retrofitOce = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_OCE)
    .build()

//Retrofit instance for KR endpoint
private val retrofitKr = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL_KR)
    .build()


/*
Using the username given by the user we use Retrofit to send GET requests to different endpoints.
getSummonerData is the only request that requires user data, the rest use values taken from
previous requests
*/
interface RiotApiService {
    @GET("get-user-data/{query}")
    suspend fun getSummonerData(@Path("query") query: String): Response<SummonerData>

    @GET("get-ranked-data/{summonerId}")
    suspend fun getRankedData(@Path("summonerId") summonerId: String): Response<List<RankedData>>

    @GET("get-match-history/{puuid}")
    suspend fun getMatchHistory(@Path("puuid") puuId: String) :Response<List<String>>

    @GET("get-match-data/{matchId}")
    suspend fun getMatchData(@Path("matchId") matchId: String): Response<MatchData>
}


abstract class ApiInstance {
    abstract val retrofitService: RiotApiService
}

// Public objects which extend the api service to the rest of the app (namely the viewmodel)
object EuropeApi: ApiInstance() {
    override val retrofitService: RiotApiService by lazy {
        retrofitEurope.create(RiotApiService::class.java)
    }
}

object KrApi: ApiInstance() {
    override val retrofitService: RiotApiService by lazy {
        retrofitKr.create(RiotApiService::class.java)
    }
}

object NaApi: ApiInstance() {
    override val retrofitService: RiotApiService by lazy {
        retrofitNorthAmerica.create(RiotApiService::class.java)
    }
}

object OceApi: ApiInstance() {
    override val retrofitService: RiotApiService by lazy {
        retrofitOce.create(RiotApiService::class.java)
    }
}
