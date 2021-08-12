package com.chua.searchforreddit.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chua.searchforreddit.App
import com.chua.searchforreddit.R
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.Status
import com.chua.searchforreddit.ui.composables.*
import com.chua.searchforreddit.ui.theme.AppTheme
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var app: App

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                is Status.Error -> {
                    showDialog(it.e.message, resources.getString(R.string.error))
                }
                is Status.Success -> {
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

    @ExperimentalComposeUiApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen(viewModel = searchViewModel)
            }
        }
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

    @ExperimentalComposeUiApi
    @Composable
    fun SearchScreen(viewModel: SearchViewModel = searchViewModel) {
        val query = viewModel.query.value

        val postList = viewModel.posts.observeAsState(emptyList())

        val status = viewModel.status.observeAsState()

        AppTheme(app.isDark.value) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SearchAppBar(
                    query = query,
                    onQueryChange = viewModel::onQueryChange,
                    onExecuteSearch = viewModel::onExecuteSearch,
                    scrollPosition = viewModel.scrollPosition,
                    suggestions = listOf(
                        "wedding",
                        "etoro",
                        "food",
                        "android",
                        "iOS",
                        "pokemon",
                        "cryptocurrency",
                        "happy",
                        "mobile"
                    ),
                    onSuggestionSelected = viewModel::onSuggestionSelected,
                    onToggleTheme = { app.toggleLightTheme() }
                )

                when (status.value) {

                    is Status.Success -> {
                        PostCardList(postList = postList.value) { url ->
                            findNavController().navigate(
                                SearchFragmentDirections.actionBlankFragmentToWebFragment(url)
                            )
                        }

                        //show details
                    }
                    is Status.Loading -> {
                        Spacer(modifier = Modifier.height(20.dp))
                        CircularProgressIndicator()
                    }

                    is Status.Error -> {
                        //show alert dialog with error message
                    }
                }
            }
        }
    }

    @ExperimentalComposeUiApi
    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun SearchScreenPreview() {
        AppTheme {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SearchAppBar(
                    query = "query",
                    onQueryChange = { },
                    onExecuteSearch = { },
                    scrollPosition = 0,
                    suggestions = listOf(
                        "wedding",
                        "etoro",
                        "food",
                        "android",
                        "iOS",
                        "pokemon",
                        "cryptocurrency",
                        "happy",
                        "mobile"
                    ),
                    onSuggestionSelected = { _, _ -> },
                    onToggleTheme = {}
                )

                PostCardList(
                    postList = listOf(
                        Post(
                            title = "Title",
                            likes = 1,
                            comments = 3,
                            url = "url",
                            imageUrl = "image_url",
                        )
                    )
                ) {}
            }
        }
    }

}