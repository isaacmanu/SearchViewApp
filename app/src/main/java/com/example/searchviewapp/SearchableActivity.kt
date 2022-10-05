package com.example.searchviewapp

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchviewapp.adapter.CardAdapter
import com.example.searchviewapp.databinding.ActivityMainBinding
import com.example.searchviewapp.databinding.CardViewBinding
import com.example.searchviewapp.databinding.SearchBinding
import com.example.searchviewapp.network.RiotApi
import com.example.searchviewapp.overview.OverviewViewModel

class SearchableActivity: AppCompatActivity() {

    private val viewModel: OverviewViewModel by viewModels()


    //Setup UI for data binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding: SearchBinding = DataBindingUtil.setContentView(this, R.layout.search)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.matchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.matchHistoryRecyclerView.adapter = CardAdapter()
        

        //Get the query from MainActivity in the form of an intent
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->

                doMySearch(query)
            }

        }
    }

    //Pass query to viewmodel and call api requests
    private fun doMySearch(query: String) {


        viewModel.getSummonerData(query)

    }
}