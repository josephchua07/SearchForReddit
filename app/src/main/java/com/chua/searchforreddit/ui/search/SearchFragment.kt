package com.chua.searchforreddit.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnLifecycleDestroyed
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chua.searchforreddit.R
import com.chua.searchforreddit.databinding.FragmentSearchBinding
import com.chua.searchforreddit.network.Status
import com.google.android.material.composethemeadapter.MdcTheme
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

        binding.apply {
            searchRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = searchAdapter
            }

            searchButton.apply {
                setViewCompositionStrategy(
                    DisposeOnLifecycleDestroyed(viewLifecycleOwner)
                )
                setContent {
                    SearchButton {
                        searchViewModel.getSubreddit(binding.searchEditText.text.toString())
                    }
                }
            }
        }

        searchViewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Loading -> {
                    showLoading(true)
                }
                is Status.Error -> {
                    showLoading(false)
                    showDialog(it.e.message, resources.getString(R.string.error))
                }
                is Status.Success -> {
                    showLoading(false)
                    searchAdapter
                        .apply { updateDataSet(it.data) }
                        .also { adapter -> adapter.notifyDataSetChanged() }

                    showDetails(
                        searchViewModel.findNoUpVotes(),
                        searchViewModel.findFivePlusUpVotes(),
                        searchViewModel.findNoComments(),
                        searchViewModel.findFivePlusComments(),
                        searchViewModel.findMostComments()
                    )
                }
            }
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

    private fun showDetails(
        noUpvotes: Int,
        fivePlusUpvotes: Int,
        noComments: Int,
        fivePlusComments: Int,
        mostComment: Pair<String, Int>?
    ) {
        val message = resources.getString(R.string.no_up_votes, noUpvotes) +
                resources.getString(R.string.five_plus_votes, fivePlusUpvotes) +
                resources.getString(R.string.no_comment, noComments) +
                resources.getString(R.string.five_plus_comment, fivePlusComments) +
                resources.getString(R.string.most_comment, mostComment?.first, mostComment?.second)

        showDialog(message, resources.getString(R.string.success))
    }

    private fun showDialog(message: String?, title: String) {

        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton(resources.getString(R.string.ok)) { _, _ -> }
                setMessage(message)
                setTitle(title)
            }
            builder.create()
        }

        alertDialog?.show()

    }

    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.searchProgressBar.visibility = View.VISIBLE
            binding.searchButton.isEnabled = false
        } else {
            binding.searchProgressBar.visibility = View.GONE
            binding.searchButton.isEnabled = true
        }
    }

    @Composable
    private fun SearchButton(action: () -> Unit) {
        MdcTheme {
            Button(onClick = { action.invoke() }) {
                Text(text = "Search")
            }
        }
    }


}