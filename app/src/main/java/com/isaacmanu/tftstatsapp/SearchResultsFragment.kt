package com.isaacmanu.tftstatsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.isaacmanu.tftstatsapp.adapter.CardAdapter
import com.isaacmanu.tftstatsapp.databinding.FragmentSearchResultsBinding
import com.isaacmanu.tftstatsapp.overview.OverviewViewModel

class SearchResultsFragment : Fragment() {

    private val args: SearchResultsFragmentArgs by navArgs()
    private val viewModel: OverviewViewModel by activityViewModels()

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameString = args.username
        val server = args.server

        viewModel.getSummonerData(usernameString, server)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        binding.matchHistoryRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.matchHistoryRecyclerView.adapter = CardAdapter()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearData()
    }
}