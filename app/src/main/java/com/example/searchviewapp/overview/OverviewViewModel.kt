package com.example.searchviewapp.overview

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchviewapp.network.*
import com.example.searchviewapp.network.model.MatchData
import com.example.searchviewapp.network.model.RankedData
import com.example.searchviewapp.network.model.SummonerData
import kotlinx.coroutines.launch


//Note 05/09: To get the api calls to execute sequentially they need to be executed
// in the same coroutine.

//Match Data list is empty when passed to recyclerview

enum class RiotApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _userData = MutableLiveData<SummonerData?>()

    // The external immutable LiveData for the request status
    val userData: LiveData<SummonerData?> = _userData

    private val _rankedData = MutableLiveData<RankedData?>()

    val rankedData: LiveData<RankedData?> = _rankedData


    //This doesn't need to be liveData as it isn't used by the UI
    //private lateinit var matchHistory: List<MatchHistory>

    private val _matchHistory = MutableLiveData<List<String>>()

    val matchHistory: LiveData<List<String>> = _matchHistory

    private val _matchData = MutableLiveData<List<MatchData?>>()

    val matchData: LiveData<List<MatchData?>> = _matchData

    private lateinit var temporaryMatchDataHolder: MutableList<MatchData?>

    private val _matchDataTest = MutableLiveData<MatchData>()

    val matchDataTest: LiveData<MatchData> = _matchDataTest


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
                _matchHistory.value = emptyList()
            }

            //Retrieve match data for each unique match ID
            // CAUSING ERROR
            try {
                val listOfMatchIds = _matchHistory.value
                getMatchData(listOfMatchIds)
                //Log.d("OverViewModel", _matchDataTest.value!!.info.tft_game_type)
            } catch (e: Exception) {
                _status.value = RiotApiStatus.ERROR
                _matchData.value = null
                Log.e("OverViewModel", e.toString())

            }

            // TEST TEST TEST
            try {
                val testMatchData = _matchHistory.value!![0]
                getMatchDataTest(testMatchData)
            } catch (e: Exception) {
                _matchDataTest.value = null
                _status.value = RiotApiStatus.ERROR
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
            val listResultMatchHistory = RiotApiEuropeRouting.retrofitService.getMatchHistory(puuId)
            _matchHistory.value = listResultMatchHistory.body()

    }


    // Test function: Logic for acquiring single MatchData instances works
    // Need to extend to multiple instances
    private suspend fun getMatchDataTest(matchId: String) {
        val listResult = RiotApiEuropeRouting.retrofitService.getMatchData(matchId)
        _matchDataTest.value = listResult.body()

    }

    private suspend fun getMatchData(matchIdList: List<String>?) {
        val listResult = mutableListOf<MatchData>()
        var counter: Int = 0
        matchIdList!!.forEach {
            listResult.add(RiotApiEuropeRouting.retrofitService.getMatchData(it).body()!!)
            //listResult[counter] = RiotApiEuropeRouting.retrofitService.getMatchData(it).body()!!
            counter += 1

        }
        _matchData.value = listResult

    }




}