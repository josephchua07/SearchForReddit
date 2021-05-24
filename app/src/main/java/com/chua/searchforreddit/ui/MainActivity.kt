package com.chua.searchforreddit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.chua.searchforreddit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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