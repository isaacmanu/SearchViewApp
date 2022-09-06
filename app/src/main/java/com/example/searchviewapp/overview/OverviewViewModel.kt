package com.example.searchviewapp.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchviewapp.network.*
import com.example.searchviewapp.network.model.MatchData
import com.example.searchviewapp.network.model.MatchHistory
import com.example.searchviewapp.network.model.RankedData
import com.example.searchviewapp.network.model.SummonerData
import kotlinx.coroutines.launch


//Note 05/09: To get the api calls to execute sequentially they need to be executed
// in the same coroutine.

//Binding adapter is being passed a null reference to matchData

enum class RiotApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _userData = MutableLiveData<SummonerData?>()

    // The external immutable LiveData for the request status
    val userData: LiveData<SummonerData?> = _userData

    private val _rankedData = MutableLiveData<RankedData?>()

    val rankedData: LiveData<RankedData?> = _rankedData


    //This doesn't need to be liveData as it isn't used by the UI
    private lateinit var matchHistory: List<MatchHistory>

    private val _matchData = MutableLiveData<List<MatchData?>>()

    val matchData: LiveData<List<MatchData?>> = _matchData

    //This will hold the match data until all the api calls are complete
    private var temporaryMatchDataHolder = mutableListOf<MatchData?>()

    private val _status = MutableLiveData<RiotApiStatus>()

    val status: LiveData<RiotApiStatus> = _status



    fun getSummonerData(query: String) {
        viewModelScope.launch {
            _status.value = RiotApiStatus.LOADING


            // Retrieve summoner data and store it in _userData
            try {
                val listResult = RiotApi.retrofitService.getSummonerData(query)
                val body = listResult.body()
                _userData.value = body

            } catch (e: Exception) {
                _status.value = RiotApiStatus.ERROR
                _userData.value = null
            }

            // Retrieve Ranked Data and store it in _rankedData
            try {
                val summonerId = _userData.value?.id
                if (summonerId != null) {
                    getRankedData(summonerId)
                }
            } catch (e: Exception) {
                _status.value = RiotApiStatus.ERROR
                _rankedData.value = null

            }

            // Retrieve match history from Riot Api via our retrofit instance
            // and store said match history in _matchHistory
            //This will return a list of 10 unique match IDs
            try {
                val userPuuId = _userData.value?.puuid
                if (userPuuId != null) {
                    getMatchHistory(userPuuId)
                }
            } catch (e: Exception) {
                _status.value = RiotApiStatus.ERROR
                matchHistory = emptyList()
            }

            //Retrieve match data for each unique match ID
            try {
                for (i in 0..9) {
                    val matchIdObject = matchHistory[i]
                    getMatchData(matchIdObject.matchId, i)
                }
                _matchData.value = temporaryMatchDataHolder



            } catch (e: Exception) {
                _status.value = RiotApiStatus.ERROR
                _matchData.value = emptyList()

            }

            _status.value = RiotApiStatus.DONE
        }
    }

    private suspend fun getRankedData(summonerId: String) {
            val listResultRankedData = RiotApi.retrofitService.getRankedData(summonerId)
            val rankedDataBody = listResultRankedData.body()
            _rankedData.value = rankedDataBody!![0]
    }

    private suspend fun getMatchHistory(puuId: String) {
            val listResultMatchHistory = RiotApi.retrofitService.getMatchHistory(puuId)
            matchHistory = listResultMatchHistory.body()!!

    }

    private suspend fun getMatchData(id: String, counter: Int) {
            val listResultMatchData = RiotApi.retrofitService.getMatchData(id)
            val matchDataBody = listResultMatchData.body()
            temporaryMatchDataHolder[counter] = matchDataBody!!
    }




}