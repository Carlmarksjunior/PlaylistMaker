package com.practicum.playlistmaker.ui.media.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediaBinding
import com.practicum.playlistmaker.ui.media.fragments.adapter.MediaViewPagerAdapter

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var adapter: MediaViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mediaBackButton.setOnClickListener {
            finish()
        }
        adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        tabMediator= TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = this.getString(R.string.favoriteTracks)
                1 -> tab.text = this.getString(R.string.playList)
            }
        }
        tabMediator.attach()




    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}