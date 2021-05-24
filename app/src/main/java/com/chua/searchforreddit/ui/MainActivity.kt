package com.chua.searchforreddit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.chua.searchforreddit.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel.getSubreddit("avengers/hot")

        with(mainViewModel) {
            posts.observe(this@MainActivity) {
                println(this.findNoUpVotes())
                println(this.findFivePlusUpVotes())
                println(this.findNoComments())
                println(this.findFivePlusComments())
                println(this.findMostComments())
            }
        }
    }
}