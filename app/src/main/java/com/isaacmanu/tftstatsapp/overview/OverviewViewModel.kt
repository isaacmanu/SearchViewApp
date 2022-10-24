package com.isaacmanu.tftstatsapp.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaacmanu.tftstatsapp.network.*
import com.isaacmanu.tftstatsapp.network.model.MatchData
import com.isaacmanu.tftstatsapp.network.model.RankedData
import com.isaacmanu.tftstatsapp.network.model.RecyclerViewItem
import com.isaacmanu.tftstatsapp.network.model.SummonerData
import com.isaacmanu.tftstatsapp.repository.SearchResultRepository
import kotlinx.coroutines.launch


enum class RiotApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    private val searchResultRepository = SearchResultRepository()

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
    fun getSummonerData(query: String, server: String) {

    //Selects which api object to use based on passed string
    var serverRegion: ApiInstance = when (server) {
        "EUW" -> EuropeApi
        "NA" -> NaApi
        "KR" -> KrApi
        else -> OceApi

    }


        viewModelScope.launch {
            _status.value = RiotApiStatus.LOADING


            /*
            Retrieve summoner data via RiotApiService and store it in _userData.
            Returned value is in HTTP Response format, currently however only the message body
            is used.

            Try-Catch blocks are used in case of network errors or unexpected behaviour
            */
            try {
                _userData.value = searchResultRepository.getUserData(query, serverRegion)

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
                    _rankedData.value = searchResultRepository.getRankedData(summonerId, serverRegion)
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
                    _matchHistory.value = searchResultRepository.getMatchHistory(userPuuId, serverRegion)
                }
            } catch (e: Exception) {
               _status.value = RiotApiStatus.ERROR
                _matchHistory.value = emptyList()
            }

            //Retrieve match data for each unique match ID
            try {
                val listOfMatchIds = _matchHistory.value
                _matchData.value = searchResultRepository.getMatchData(listOfMatchIds, serverRegion)
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

    //Function called to clear data when navigating away from fragment
    fun clearData() {
        _userData.value = null
        _rankedData.value = null
        _matchHistory.value = listOf()
        _matchData.value = null
        _recyclerItem.value = listOf()
    }




}