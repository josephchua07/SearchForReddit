package com.chua.searchforreddit.ui.search

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.compose.rememberImagePainter
import com.chua.searchforreddit.R
import com.chua.searchforreddit.domain.Post
import com.chua.searchforreddit.network.Status
import com.google.android.material.composethemeadapter.MdcTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

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

        MdcTheme {

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                RedditTextField(
                    label = "subreddits/communities",
                    value = query,
                    onValueChange = { viewModel.onQueryChange(it) },
                    onKeyboardSearchClicked = viewModel::search
                )

                HorizontalSuggestions(
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
                    selectedSuggestion = query,
                    preservedScroll = viewModel.preservedScrollState
                ) { selectedSuggestion, scrollState ->
                    viewModel.onSelectedSuggestionChange(selectedSuggestion, scrollState)
                }

                when (status.value) {

                    is Status.Success -> {
                        PostList(postList = postList.value) { url ->
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

    @SuppressLint("CoroutineCreationDuringComposition")
    @Composable
    fun HorizontalSuggestions(
        suggestions: List<String>,
        selectedSuggestion: String?,
        preservedScroll: Int,
        onItemSelected: (String, Int) -> Unit
    ) {
        val scrollState = rememberScrollState()
        val scope = rememberCoroutineScope()
        Row(
            modifier = Modifier
                .padding(8.dp)
                .horizontalScroll(state = scrollState)
        ) {

            scope.launch { scrollState.scrollTo(preservedScroll) }

            suggestions.forEach { suggestion ->
                SuggestionChip(
                    text = suggestion,
                    isSelected = selectedSuggestion == suggestion
                ) { onItemSelected(it, scrollState.value) }
            }
        }
    }

    @Composable
    fun SuggestionChip(
        text: String,
        isSelected: Boolean = false,
        onSelected: (String) -> Unit
    ) {
        Surface(
            modifier = Modifier
                .padding(end = 8.dp)
                .toggleable(value = isSelected, onValueChange = {
                    onSelected(text)
                }),
            elevation = 8.dp,
            shape = MaterialTheme.shapes.large,
            color = if (isSelected) Color.Gray else MaterialTheme.colors.primarySurface
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(8.dp),
                color = Color.White
            )
        }
    }

    @ExperimentalComposeUiApi
    @Composable
    fun RedditTextField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        onKeyboardSearchClicked: () -> Unit
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = value,
            onValueChange = { onValueChange.invoke(it) },
            label = { Text(text = label) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search"
                )
            },
            keyboardActions = KeyboardActions(
                onSearch = {
                    onKeyboardSearchClicked.invoke()
                    keyboardController?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            )
        )
    }

    @Composable
    fun PostList(
        postList: List<Post>,
        action: (String) -> Unit
    ) {
        LazyColumn {
            items(postList.size) { index ->
                Post(post = postList[index]) { url ->
                    action.invoke(url)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    @Composable
    fun Post(post: Post, action: (String) -> Unit = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { action.invoke(post.url.drop(1)) },
            border = BorderStroke(2.dp, MaterialTheme.colors.onPrimary),
            elevation = 8.dp
        ) {
            Column {

                post.imageUrl?.let { imageUrl ->
                    Image(
                        painter = rememberImagePainter(
                            data = imageUrl,
                            onExecute = { _, _ -> true },
                            builder = {
                                crossfade(true)
                                placeholder(R.drawable.ic_baseline_image_24)
                            }
                        ),
                        contentDescription = "Post Image",
                        modifier = Modifier
                            .height(240.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }

                Text(
                    text = post.title,
                    modifier = Modifier.padding(4.dp),
                    style = MaterialTheme.typography.h5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "likes: ${post.likes}",
                        modifier = Modifier.padding(4.dp),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = "comments: ${post.comments}",
                        modifier = Modifier.padding(4.dp),
                        style = MaterialTheme.typography.subtitle1
                    )
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
        MdcTheme {
            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                RedditTextField(
                    label = "subreddits/communities",
                    value = "avengers/hot",
                    onValueChange = {}
                ) {}

                HorizontalSuggestions(
                    listOf(
                        "wedding",
                        "etoro",
                        "food",
                        "android",
                        "pokemon",
                        "cryptocurrency",
                        "happy",
                        "mobile"
                    ),
                    "food",
                    0
                ) { _, _ -> }

                PostList(
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