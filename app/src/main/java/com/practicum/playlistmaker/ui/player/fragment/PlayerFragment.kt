package com.practicum.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.presentation.media.view_model.state.PlayListState
import com.practicum.playlistmaker.presentation.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.presentation.player.view_model.state.PlayerState
import com.practicum.playlistmaker.presentation.player.view_model.state.TrackInAlbumState
import com.practicum.playlistmaker.ui.player.fragment.adapter.AlbumsBottomSheetAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {


    private lateinit var artSong: String

    private val playerViewModel by viewModel<PlayerViewModel>()
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: AlbumsBottomSheetAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        playerViewModel.getAllAlbums()
        playerViewModel.observeAlbumsLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        playerViewModel.observeTrackInAlbumLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is TrackInAlbumState.isSucces -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        requireActivity(),
                        "Добавлено в плейлист [{${it.name}}]",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is TrackInAlbumState.isFail -> {
                    Toast.makeText(
                        requireActivity(),
                        "Трек уже добавлен в плейлист [${it.name}]",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.addAlbum.setOnClickListener {
            playerViewModel.getAllAlbums()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(p0: View, p1: Float) {
            }
        })

        binding.bottomSheetButton.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_playListCreateFragment)
        }


        binding.menuButtonAudioPlayer.setOnClickListener {
            findNavController().popBackStack()
        }
        val track = arguments?.getParcelable<Track>(TRACK_KEY)

        if (track != null && !track.previewUrl.isNullOrBlank()) {
            playerViewModel.preparePlayer(track.previewUrl)
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
        if (track?.isFavorite == true) {
            binding.favourite.setImageResource(R.drawable.ic_favourite_is_active_25_23)
        } else {
            binding.favourite.setImageResource(R.drawable.ic_favourite_25_23)
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
            when (it) {
                is PlayerState.Default -> binding.playStop.isEnabled = false
                is PlayerState.Prepared -> {
                    binding.playStop.isEnabled = true
                    binding.playStop.setImageResource(R.drawable.ic_playstop_84)
                    binding.timeNow.text = it.progress
                }

                is PlayerState.Playing -> {
                    binding.playStop.setImageResource(R.drawable.ic_stopplay_84)
                    binding.timeNow.text = it.progress

                }

                is PlayerState.Paused -> {
                    binding.playStop.setImageResource(R.drawable.ic_playstop_84)
                    binding.timeNow.text = it.progress
                }

            }
        }
        playerViewModel.observeIsFavoriteLiveData().observe(viewLifecycleOwner) {
            if (it) {
                binding.favourite.setImageResource(R.drawable.ic_favourite_is_active_25_23)
                track?.isFavorite = it
            } else {
                binding.favourite.setImageResource(R.drawable.ic_favourite_25_23)
                track?.isFavorite = it
            }
        }
        binding.favourite.setOnClickListener {
            playerViewModel.onFavoriteClicked(track!!)
        }

        binding.playStop.setOnClickListener {
            playerViewModel.onPlayButtonClicked()
        }


    }


    private fun render(playListState: PlayListState) {
        when (playListState) {
            is PlayListState.Empty -> {showEmpty(playListState.message)}
            is PlayListState.Content -> showContent(playListState.albums)
        }
    }

        private fun showEmpty(message: String){
            binding.recyclerBottomSheet.visibility = View.GONE
            binding.tvPlaceHolder.apply {
                text = message
                visibility = View.VISIBLE
            }
            binding.mediaImagePlaceHolder.visibility= View.VISIBLE
    }
    private fun showContent(content: List<Album>) {
        adapter = AlbumsBottomSheetAdapter(content, { album ->
            playerViewModel.insertTrackInAlbum(album, arguments?.getParcelable<Track>(TRACK_KEY)!!)
        })
        binding.recyclerBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        binding.tvPlaceHolder.visibility= View.GONE
        binding.mediaImagePlaceHolder.visibility= View.GONE
        binding.recyclerBottomSheet.visibility = View.VISIBLE
        binding.recyclerBottomSheet.adapter = adapter
        adapter.notifyDataSetChanged()
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
