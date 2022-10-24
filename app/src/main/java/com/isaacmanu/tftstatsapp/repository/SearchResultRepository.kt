package com.isaacmanu.tftstatsapp.repository

import com.isaacmanu.tftstatsapp.network.ApiInstance
import com.isaacmanu.tftstatsapp.network.model.MatchData
import com.isaacmanu.tftstatsapp.network.model.MatchHistory
import com.isaacmanu.tftstatsapp.network.model.RankedData
import com.isaacmanu.tftstatsapp.network.model.SummonerData

class SearchResultRepository {

    suspend fun getUserData(query: String, serverRegion: ApiInstance): SummonerData? {
        val listResult = serverRegion.retrofitService.getSummonerData(query)
        return listResult.body()
    }

    suspend fun getRankedData(summonerId: String, serverRegion: ApiInstance): RankedData? {
        val listResult = serverRegion.retrofitService.getRankedData(summonerId)
        return listResult.body()?.get(0)
    }

    suspend fun getMatchHistory(userPuuId: String, serverRegion: ApiInstance): List<String>? {
        val listResult = serverRegion.retrofitService.getMatchHistory(userPuuId)
        return listResult.body()
    }

    suspend fun getMatchData(matchIds: List<String>?, serverRegion: ApiInstance): List<MatchData> {
        val listResult = mutableListOf<MatchData>()
        matchIds?.forEach {
            listResult.add(serverRegion.retrofitService.getMatchData(it).body()!!)
        }
        return listResult
    }
}