package com.chua.searchforreddit.ui.search

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
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

    @Composable
    fun SearchScreen(viewModel: SearchViewModel = searchViewModel) {
        val searchText = viewModel.searchText.observeAsState("")

        val postList = viewModel.posts.observeAsState(emptyList())

        val status = viewModel.status.observeAsState()

        MdcTheme {

            Column(
                modifier = Modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RedditTextField(
                        label = "subreddits/communities",
                        value = searchText.value
                    ) {
                        viewModel.setSearchText(it)
                    }

                    RedditButton("Search") {
                        viewModel.getSubreddit(searchText.value)
                    }
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

    @Composable
    fun RedditTextField(label: String, value: String, onValueChange: (String) -> Unit) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(4.dp),
            value = value,
            onValueChange = { onValueChange.invoke(it) },
            label = { Text(text = label) })
    }

    @Composable
    fun RedditButton(text: String, action: () -> Unit = {}) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { action.invoke() }) {
            Text(text = text)
        }
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    RedditTextField(
                        label = "subreddits/communities",
                        value = "avengers/hot"
                    ) {}

                    RedditButton("Search")
                }

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