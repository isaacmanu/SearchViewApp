package com.example.searchviewapp.overview

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.searchviewapp.network.RankedData
import com.example.searchviewapp.network.SummonerData
import com.example.searchviewapp.network.RiotApi
import kotlinx.coroutines.launch
import org.w3c.dom.Text

enum class RiotApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _userData = MutableLiveData<SummonerData>()

    // The external immutable LiveData for the request status
    val userData: LiveData<SummonerData> = _userData


    private val _rankedData = MutableLiveData<RankedData>()

    val rankedData: LiveData<RankedData> = _rankedData


    private val _status = MutableLiveData<RiotApiStatus>()

    val status: LiveData<RiotApiStatus> = _status



    fun getSummonerData(query: String) {
        viewModelScope.launch {
            _status.value = RiotApiStatus.LOADING

            try {
                val listResult = RiotApi.retrofitService.getSummonerData(query)
                val body = listResult.body()
                _userData.value = body
                val summonerId = body!!.id

                try {
                    getRankedData(summonerId)
                } catch (e: Exception) {
                    _status.value = RiotApiStatus.ERROR
                }

                _status.value = RiotApiStatus.DONE


            } catch (e: Exception) {
                _status.value = RiotApiStatus.ERROR
            }
        }
    }

    private fun getRankedData(summonerId: String) {
        viewModelScope.launch{
            val listResultRankedData = RiotApi.retrofitService.getRankedData(summonerId)
            val rankedDataBody = listResultRankedData.body()
            _rankedData.value = rankedDataBody!![0]
        }

    }


}