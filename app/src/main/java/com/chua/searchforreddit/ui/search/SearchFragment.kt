package com.chua.searchforreddit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chua.searchforreddit.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()
    private val searchAdapter = SearchAdapter { url ->
        findNavController().navigate(SearchFragmentDirections.actionBlankFragmentToWebFragment(url))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = searchAdapter
        }

        binding.searchButton.setOnClickListener {
            searchViewModel.getSubreddit(binding.searchEditText.text.toString())
        }

        searchViewModel.posts.observe(viewLifecycleOwner) {

            searchAdapter.updateDataSet(it)
            searchAdapter.notifyDataSetChanged()

            println(searchViewModel.findNoUpVotes())
            println(searchViewModel.findFivePlusUpVotes())
            println(searchViewModel.findNoComments())
            println(searchViewModel.findFivePlusComments())
            println(searchViewModel.findMostComments())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}