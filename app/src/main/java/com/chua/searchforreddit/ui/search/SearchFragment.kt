package com.chua.searchforreddit.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chua.searchforreddit.App
import com.chua.searchforreddit.R
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.Status
import com.chua.searchforreddit.ui.composables.PostCardList
import com.chua.searchforreddit.ui.composables.SearchAppBar
import com.chua.searchforreddit.ui.search.SearchEvent.ExecuteSearch
import com.chua.searchforreddit.ui.search.SearchEvent.QueryChange
import com.chua.searchforreddit.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var app: App

    private val searchViewModel: SearchViewModel by viewModels()

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

    @SuppressLint("CoroutineCreationDuringComposition")
    @ExperimentalComposeUiApi
    @Composable
    fun SearchScreen(viewModel: SearchViewModel = searchViewModel) {

        val query = viewModel.query.value

        val statusState = viewModel.status.observeAsState()

        val suggestions = viewModel.listOfSuggestions

        val scaffoldState = rememberScaffoldState()

        val suggestionsScrollState = rememberScrollState()

        val scope = rememberCoroutineScope()

        AppTheme(app.isDark.value) {
            Scaffold(
                scaffoldState = scaffoldState,
                topBar = {
                    SearchAppBar(
                        query = query,
                        onQueryChange = { viewModel.onTriggerEvent(QueryChange(it)) },
                        onExecuteSearch = { viewModel.onTriggerEvent(ExecuteSearch) },
                        keyboardController = LocalSoftwareKeyboardController.current,
                        scrollPosition = viewModel.scrollPosition,
                        suggestions = suggestions,
                        suggestionsScrollState = suggestionsScrollState,
                        coroutineScope = scope,
                        onSuggestionSelected = { suggestion, scrollState ->
                            viewModel.onTriggerEvent(
                                SearchEvent.SuggestionSelected(
                                    suggestion,
                                    scrollState
                                )
                            )
                        },
                        onToggleTheme = { app.toggleLightTheme() }
                    )
                }) {
                when (val status = statusState.value) {
                    is Status.Success -> {
                        PostCardList(postList = status.data) { url ->
                            findNavController().navigate(
                                SearchFragmentDirections.actionBlankFragmentToWebFragment(url)
                            )
                        }

                        showDetails(
                            scope = scope,
                            scaffoldState = scaffoldState,
                            noUpVotesCount = status.noUpVotesCount,
                            fivePlusUpVotesCount = status.fivePlusUpVotesCount,
                            noCommentsCount = status.noCommentsCount,
                            fivePlusCommentsCount = status.fivePlusCommentsCount,
                            mostComments = status.mostComments
                        )

                    }

                    is Status.Loading -> {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalArrangement = Arrangement.Center
                        ) { CircularProgressIndicator() }
                    }

                    is Status.Error -> {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "${status.e}",
                                actionLabel = "Hide",
                                duration = SnackbarDuration.Indefinite
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showDetails(
        scope: CoroutineScope,
        scaffoldState: ScaffoldState,
        noUpVotesCount: Int,
        fivePlusUpVotesCount: Int,
        noCommentsCount: Int,
        fivePlusCommentsCount: Int,
        mostComments: Pair<String, Int>?
    ) {
        scope.launch {
            launchSnackBar(
                scaffoldState,
                resources.getString(
                    R.string.no_up_votes,
                    noUpVotesCount
                )
            )

            launchSnackBar(
                scaffoldState,
                resources.getString(
                    R.string.five_plus_votes,
                    fivePlusUpVotesCount
                )
            )

            launchSnackBar(
                scaffoldState,
                resources.getString(
                    R.string.no_comment,
                    noCommentsCount
                )
            )

            launchSnackBar(
                scaffoldState,
                resources.getString(
                    R.string.five_plus_comment,
                    fivePlusCommentsCount
                )
            )

            launchSnackBar(
                scaffoldState,
                resources.getString(
                    R.string.most_comment,
                    mostComments?.first,
                    mostComments?.second
                )
            )
        }
    }

    private suspend fun launchSnackBar(
        scaffoldState: ScaffoldState,
        message: String
    ) {
        scaffoldState.snackbarHostState.showSnackbar(
            message = message,
            actionLabel = "Hide",
            duration = SnackbarDuration.Indefinite
        )
    }

    @ExperimentalComposeUiApi
    @SuppressLint("CoroutineCreationDuringComposition")
    @Preview(name = "Light Theme")
    @Composable
    fun LightTheme() {
        SearchScreenPreview()
    }

    @ExperimentalComposeUiApi
    @SuppressLint("CoroutineCreationDuringComposition")
    @Preview(name = "Dark Theme")
    @Composable
    fun DarkTheme() {
        SearchScreenPreview(true)
    }

    @ExperimentalComposeUiApi
    @Composable
    fun SearchScreenPreview(darkTheme: Boolean = false) {
        AppTheme(darkTheme) {
            Scaffold(
                topBar = {
                    SearchAppBar(
                        query = "query",
                        onQueryChange = {},
                        onExecuteSearch = {},
                        keyboardController = null,
                        scrollPosition = 0,
                        suggestions = listOf(
                            "food",
                            "dog",
                            "wedding",
                            "happy",
                            "android",
                            "pokemon",
                            "etoro",
                            "iOS",
                            "cryptocurrency",
                            "mobile"
                        ),
                        suggestionsScrollState = rememberScrollState(),
                        coroutineScope = rememberCoroutineScope(),
                        onSuggestionSelected = { _, _ -> },
                        onToggleTheme = { app.toggleLightTheme() }
                    )
                },
            ) {

                PostCardList(
                    postList = listOf(
                        Post(
                            title = "Title",
                            likes = 1,
                            comments = 3,
                            url = "url",
                            imageUrl = "image_url",
                        ),
                        Post(
                            title = "Title",
                            likes = 1,
                            comments = 3,
                            url = "url",
                            imageUrl = "image_url",
                        ),
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