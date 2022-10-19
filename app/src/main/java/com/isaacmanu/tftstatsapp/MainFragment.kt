package com.isaacmanu.tftstatsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.isaacmanu.tftstatsapp.databinding.FragmentMainBinding
import com.isaacmanu.tftstatsapp.overview.OverviewViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.searchButton.setOnClickListener {
            getUserData()
        }



        val items = listOf("EUW", "NA", "KR", "OCE")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)

        // Temporary solution to autocompletetextview bug (https://github.com/material-components/material-components-android/issues/1464)
        binding.serverSelect.isSaveEnabled = false

        binding.serverSelect.setAdapter(adapter)

        binding.serverSelect.setText(resources.getStringArray(R.array.simple_items)[0], false)

        binding.usernameText.requestFocus()

        return binding.root
    }

    //Takes the string entered by the user and passes it to the SearchResultsFragment
    private fun getUserData() {
        if (!isEmpty()) {
            val usernameString: String = binding.usernameText.text.toString()
            val serverSelection: String = binding.serverSelect.text.toString()
            val action = MainFragmentDirections.actionMainFragmentToSearchResultsFragment(usernameString, serverSelection)
            findNavController().navigate(action)
        } else {
            Toast.makeText(context, "Please enter a valid username", Toast.LENGTH_SHORT).show()
        }
    }

    // Check if the user has entered a valid username
    private fun isEmpty(): Boolean {
        return binding.usernameText.text.isNullOrBlank()
    }


}