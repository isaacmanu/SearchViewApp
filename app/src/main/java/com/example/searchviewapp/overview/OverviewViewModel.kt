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
import com.example.searchviewapp.network.model.RecyclerViewItem
import com.example.searchviewapp.network.model.SummonerData
import kotlinx.coroutines.launch


enum class RiotApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    /*
    Private and public LiveData values for all items used in SearchableActivity.
    LiveData is used so that the UI is automatically updated when the data changes.
    Backing properties are used so that the values can only be changed from the viewmodel.
    */
    private val _userData = MutableLiveData<SummonerData?>()

    val userData: LiveData<SummonerData?> = _userData

    private val _rankedData = MutableLiveData<RankedData?>()

    val rankedData: LiveData<RankedData?> = _rankedData

    private val _matchHistory = MutableLiveData<List<String>>()

    val matchHistory: LiveData<List<String>> = _matchHistory

    private val _matchData = MutableLiveData<List<MatchData>?>()

    val matchData: LiveData<List<MatchData>?> = _matchData

    //Item passed to CardAdapter
    private val _recyclerItem = MutableLiveData<List<RecyclerViewItem>>()

    val recyclerItem: LiveData<List<RecyclerViewItem>> = _recyclerItem

    private val _status = MutableLiveData<RiotApiStatus>()

    val status: LiveData<RiotApiStatus> = _status


/*
    We launch a coroutine within the scope of the viewmodel so that the API calls are not
    done on the main thread. Only one coroutine is launched for the intended purpose of executing
    the api calls sequentially however this may be incorrect or bad practice.
 */
    fun getSummonerData(query: String) {
        viewModelScope.launch {
            _status.value = RiotApiStatus.LOADING


            /*
            Retrieve summoner data via RiotApiService and store it in _userData.
            Returned value is in HTTP Response format, currently however only the message body
            is used.

            Try-Catch blocks are used in case of network errors or unexpected behaviour
            */
            try {
                val listResult = RiotApi.retrofitService.getSummonerData(query)
                val body = listResult.body()
                _userData.value = body

            } catch (e: Exception) {
               _status.value = RiotApiStatus.ERROR
                _userData.value = null
            }

            /*
            Retrieve Ranked Data and store it in _rankedData.
            This will return null if no ranked games have been played.
            This information is not critical so the status is not changed.
            */
            try {
                val summonerId = _userData.value?.id
                if (summonerId != null) {
                    getRankedData(summonerId)
                }
            } catch (e: Exception) {
                _status.value = RiotApiStatus.LOADING
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
            try {
                val listOfMatchIds = _matchHistory.value
                getMatchData(listOfMatchIds)
            } catch (e: Exception) {
                _status.value = RiotApiStatus.ERROR
                _matchData.value = null
                Log.e("OverViewModel", e.toString())

            }

            val summonerData = _userData.value
            val matchDataList = _matchData.value
            if (summonerData != null && matchDataList != null) {
                mergeObjects(summonerData, matchDataList)
            }


            _status.value = RiotApiStatus.DONE
        }
    }

    /*
    Function to combine two different objects for use in the ListAdapter (CardAdapter).
    There is most likely a much better way of doing this.
     */
    private fun mergeObjects(summonerData: SummonerData, matchDataList: List<MatchData>) {
        val temporaryRecyclerViewItemList = mutableListOf<RecyclerViewItem>()
        matchDataList.forEach{
            val temporaryRecyclerViewItem = RecyclerViewItem(summonerData.puuid, it)
            temporaryRecyclerViewItemList.add(temporaryRecyclerViewItem)
        }
        _recyclerItem.value = temporaryRecyclerViewItemList

    }

    /*
    Suspend functions to be called inside the coroutine
     */
    private suspend fun getRankedData(summonerId: String) {
            val listResultRankedData = RiotApi.retrofitService.getRankedData(summonerId)
            val rankedDataBody = listResultRankedData.body()
            _rankedData.value = rankedDataBody!![0]
    }

    private suspend fun getMatchHistory(puuId: String) {
            val listResultMatchHistory = RiotApiEuropeRouting.retrofitService.getMatchHistory(puuId)
            _matchHistory.value = listResultMatchHistory.body()

    }

    private suspend fun getMatchData(matchIdList: List<String>?) {
        val listResult = mutableListOf<MatchData>()
        matchIdList!!.forEach {
            listResult.add(RiotApiEuropeRouting.retrofitService.getMatchData(it).body()!!)
        }
        _matchData.value = listResult

    }




}