package com.practicum.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {


    private lateinit var artSong: String

    private val playerViewModel by viewModel<PlayerViewModel>()
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val track = arguments?.getParcelable<Track>(TRACK_KEY)
        if (!track?.previewUrl.isNullOrEmpty()){
            playerViewModel.preparePlayer(track?.previewUrl?:"")
        }




        if (!track?.trackName.isNullOrEmpty()) {
            binding.trackName.text = track.trackName
        } else {
            binding.trackName.visibility = View.GONE
        }

        if (!track?.artistName.isNullOrEmpty()) {
            binding.artistName.text = track.artistName
        } else {
            binding.artistName.visibility = View.GONE
        }

        val durationString = if (track?.duration != null) {
            track.duration
        } else {
            null
        }
        if (durationString != null) {
            binding.durationValue.text = durationString
        } else {
            binding.durationGroup.visibility = View.GONE
        }

        artSong = track?.artworkUrl100.toString().replaceAfterLast('/', "512x512bb.jpg")
        val radiusPx = resources.getDimensionPixelSize(R.dimen.cornerRadius10)
        Glide.with(this)
            .load(artSong)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(radiusPx))
            .into(binding.artImage)

        if (!track?.country.isNullOrEmpty()) {
            binding.cityValue.text = track.country
        } else {
            binding.cityGroup.visibility = View.GONE
        }

        if (!track?.primaryGenreName.isNullOrEmpty()) {
            binding.jenreValue.text = track.primaryGenreName
        } else {
            binding.jenreGroup.visibility = View.GONE
        }

        if (!track?.collectionName.isNullOrEmpty()) {
            binding.albumNameValue.text = track.collectionName
        } else {
            binding.albomGroup.visibility = View.GONE
        }

        if (!track?.releaseYear.isNullOrEmpty()) {
            binding.yearOfSongValue.text = track.releaseYear
        } else {
            binding.yearGroup.visibility = View.GONE
        }

        binding.playStop.isEnabled = false


        playerViewModel.observePlayerStateLiveData().observe(viewLifecycleOwner) {
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

        playerViewModel.observeTimerLiveData().observe(viewLifecycleOwner){
            binding.timeNow.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        const val TRACK_KEY = "track"

        fun createArgs(track: Track): Bundle {
            return Bundle().apply {
                putParcelable(TRACK_KEY, track)
            }
        }
    }
}
enum class StatePlayer(val value: Int){
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3);
    companion object{
        fun stateValue(value:Int): StatePlayer?{
            return entries.find { it.value ==value }
        }
    }

}