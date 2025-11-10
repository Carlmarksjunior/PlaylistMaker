package com.practicum.playlistmaker.ui.player.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel

class PlayerActivity() : AppCompatActivity() {

    private lateinit var artSong: String
    private lateinit var binding: ActivityAudioPlayerBinding


    private lateinit var playerViewModel: PlayerViewModel


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track = intent.getParcelableExtra<Track>(TRACK_KEY)
        playerViewModel = ViewModelProvider(
            this,
            PlayerViewModel.getFactory(track?.previewUrl!!))
            .get(PlayerViewModel::class.java)
        binding.menuButtonAudioPlayer.setOnClickListener {
            finish()
        }



        if (!track.trackName.isNullOrEmpty()) {
            binding.trackName.text = track.trackName
        } else {
            binding.trackName.visibility = View.GONE
        }

        if (!track.artistName.isNullOrEmpty()) {
            binding.artistName.text = track.artistName
        } else {
            binding.artistName.visibility = View.GONE
        }

        val durationString = if (track.duration != null) {
            track.duration
        } else {
            null
        }
        if (durationString != null) {
            binding.durationValue.text = durationString
        } else {
            binding.durationGroup.visibility = View.GONE
        }

        artSong = track.artworkUrl100.toString().replaceAfterLast('/', "512x512bb.jpg")
        val radiusPx = resources.getDimensionPixelSize(R.dimen.cornerRadius10)
        Glide.with(this)
            .load(artSong)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(radiusPx))
            .into(binding.artImage)

        if (!track.country.isNullOrEmpty()) {
            binding.cityValue.text = track.country
        } else {
            binding.cityGroup.visibility = View.GONE
        }

        if (!track.primaryGenreName.isNullOrEmpty()) {
            binding.jenreValue.text = track.primaryGenreName
        } else {
            binding.jenreGroup.visibility = View.GONE
        }

        if (!track.collectionName.isNullOrEmpty()) {
            binding.albumNameValue.text = track.collectionName
        } else {
            val collectionGroup = findViewById<Group>(R.id.albomGroup)
            collectionGroup.visibility = View.GONE
        }

        if (!track.releaseYear.isNullOrEmpty()) {
            binding.yearOfSongValue.text = track.releaseYear
        } else {
            binding.yearGroup.visibility = View.GONE
        }

        binding.playStop.isEnabled = false


        playerViewModel.observePlayerStateLiveData().observe(this) {
            val state = StatePlayer.stateValue(it)
            when (state) {
                StatePlayer.STATE_DEFAULT -> binding.playStop.isEnabled = false
                StatePlayer.STATE_PREPARED -> binding.playStop.isEnabled = true
                StatePlayer.STATE_PLAYING -> {
                    binding.playStop.setImageResource(R.drawable.ic_stopplay_84)

                }

                StatePlayer.STATE_PAUSED -> {
                    binding.playStop.setImageResource(R.drawable.ic_playstop_84)
                }

                null -> Log.e("Player", "Неизвестное состояние: $it")
            }
        }
        binding.playStop.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }

        playerViewModel.observeTimerLiveData().observe(this){
            binding.timeNow.text = it
        }
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.pausePlayer()
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}
enum class StatePlayer(val value: Int){
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3);
    companion object{
        fun stateValue(value:Int): StatePlayer?{
            return entries.find { it.value == value }
        }
    }


}