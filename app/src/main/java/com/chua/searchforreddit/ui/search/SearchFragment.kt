package com.chua.searchforreddit.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy.DisposeOnLifecycleDestroyed
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chua.searchforreddit.R
import com.chua.searchforreddit.databinding.FragmentSearchBinding
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.Status
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            searchEditText.apply {
                setViewCompositionStrategy(
                    DisposeOnLifecycleDestroyed(viewLifecycleOwner)
                )

                setContent {
                    SearchTextField(searchViewModel)
                }
            }

            searchButton.apply {
                setViewCompositionStrategy(
                    DisposeOnLifecycleDestroyed(viewLifecycleOwner)
                )
                setContent {
                    SearchButton {
                        searchViewModel.searchText.value?.let {
                            searchViewModel.getSubreddit(it)
                        }
                    }
                }
            }

            searchRecyclerView.apply {
                setViewCompositionStrategy(
                    DisposeOnLifecycleDestroyed(viewLifecycleOwner)
                )

                setContent { PostList(searchViewModel) }
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
    fun SearchTextField(viewModel: SearchViewModel) {
        val searchText = viewModel.searchText.observeAsState()

        OutlinedTextField(
            value = searchText.value ?: "",
            onValueChange = { viewModel.setSearchText(it) },
            label = { Text(text = "subreddits/communities") })
    }

    @Composable
    fun SearchButton(action: () -> Unit = {}) {
        Button(onClick = { action.invoke() }) {
            Text(text = "Search")
        }
    }

    @Composable
    fun PostList(
        viewModel: SearchViewModel
    ) {
        val postsState: State<List<Post>> = viewModel.posts.observeAsState(emptyList())

        LazyColumn {
            items(postsState.value.size) { index ->
                Post(post = postsState.value[index]) { url ->
                    findNavController().navigate(
                        SearchFragmentDirections.actionBlankFragmentToWebFragment(url)
                    )
                }
            }
        }
    }

    @Composable
    fun Post(post: Post, action: (String) -> Unit = {}) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable { action.invoke(post.url.drop(1)) }
        ) {
            Text(
                text = "Title: ${post.title}",
                style = MaterialTheme.typography.h5
            )
            Text(
                text = "likes: ${post.likes}",
                style = MaterialTheme.typography.subtitle1
            )
            Text(
                text = "comments: ${post.comments}",
                style = MaterialTheme.typography.subtitle2
            )
        }
    }

    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun SearchScreenPreview() {
        MdcTheme {
            Column {
                Row {
//                    SearchTextField()
                    SearchButton()
                }

                Post(
                    post = Post(
                        title = "Title",
                        comments = 5,
                        likes = 3,
                        url = "google.com"
                    )
                )
            }
        }
    }

}