package com.practicum.playlistmaker.ui.detailsPlayList.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentDetailsPlayListBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.presentation.detailsPlayList.DetailsPlayListViewModel
import com.practicum.playlistmaker.presentation.detailsPlayList.state.DetailsPlayListState
import com.practicum.playlistmaker.presentation.search.view_model.state.TrackState
import com.practicum.playlistmaker.ui.playList.fragment.PlayListRedactFragment
import com.practicum.playlistmaker.ui.player.fragment.PlayerFragment
import com.practicum.playlistmaker.ui.search.fragment.AdapterTracks
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsPlayListFragment : Fragment() {

    private var _binding: FragmentDetailsPlayListBinding? = null
    private val binding get() = _binding!!

    private var albumId: Int? = null

    lateinit var album: Album

    private val detailsPlayListViewModel by viewModel<DetailsPlayListViewModel>()

    private lateinit var adapter: AdapterTracks

    private lateinit var confirmDialog: MaterialAlertDialogBuilder

    private lateinit var deleteConfirmDialog: MaterialAlertDialogBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButtonPlayList.setOnClickListener {
            findNavController().popBackStack()
        }
        val displayMetrics = resources.displayMetrics
        val screenHeightPx = displayMetrics.heightPixels


        val peekHeightPx = (screenHeightPx * 0.33).toInt()
        val bottomSheetBehaviorStandard =
            BottomSheetBehavior.from(binding.standardBottomSheet).apply {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                peekHeight = peekHeightPx
            }
        albumId = arguments?.getInt(ALBUM_KEY)
        detailsPlayListViewModel.getAlbum(albumId!!)

        detailsPlayListViewModel.observeAlbumState().observe(viewLifecycleOwner) {
            when (it) {
                is DetailsPlayListState.Content -> {
                    showAlbumContent(it.album)
                    album = it.album
                }

                is DetailsPlayListState.Empty -> {}
            }
        }

        detailsPlayListViewModel.observeTracksState().observe(viewLifecycleOwner) {
            when (it) {
                is TrackState.Content -> {
                    showFullDurationTracksInAlbum(it.tracks)
                    showTracksInAlbum(it.tracks)
                }

                is TrackState.Empty -> {
                    showEmpty()
                }

                is TrackState.History -> {}
                is TrackState.Error -> {}
                is TrackState.Loading -> {}
            }
        }

        detailsPlayListViewModel.observeDeleteTracksState().observe(viewLifecycleOwner) {
            if (it) {
                detailsPlayListViewModel.getAlbum(albumId!!)
            } else {
            }
        }



        binding.shareAlbum.setOnClickListener {
            detailsPlayListViewModel.shareTracksInPlayList(albumId!!)
        }
        val bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.menuAlbum.setOnClickListener {
            binding.menuBottomSheet.visibility = View.VISIBLE
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            bottomSheetBehaviorMenu.addBottomSheetCallback(object :
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

                override fun onSlide(p0: View, p1: Float) = Unit
            })

        }

        binding.shareAlbumSecond.setOnClickListener {
            detailsPlayListViewModel.shareTracksInPlayList(albumId!!)
        }

        detailsPlayListViewModel.observeShareAlbumText().observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT, getString(
                            R.string.share_album_text,
                            album.albumName, album.albumDescription,
                            getPluralTracks(album.tracksCount), it
                        )
                    )
                }
                bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
                startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
            }else{
                Toast.makeText(requireContext(), R.string.track_in_album_Empty, Toast.LENGTH_SHORT).show()
                bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        binding.deletePlaylist.setOnClickListener {
            deleteAlbum()
            bottomSheetBehaviorMenu.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.redactAlbum.setOnClickListener {
            findNavController().navigate(R.id.action_detailsPlayList_to_playListRedactFragment,
                PlayListRedactFragment.createArgs(arguments?.getInt(ALBUM_KEY)!!))
        }

    }


    fun deleteAlbum() {
        deleteConfirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.do_you_want_delete_album, album.albumName))
            .setNegativeButton(R.string.cancel) { dialog, which -> }
            .setPositiveButton(R.string.delete) { dialog, whish ->
                detailsPlayListViewModel.deleteAlbum(
                    albumId!!
                )
                findNavController().popBackStack()
            }
        deleteConfirmDialog.show()
    }

    fun deleteTrack(trackId: Int?) {
        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.delete_track)
            .setNegativeButton(R.string.no) { dialog, which -> }
            .setPositiveButton(R.string.yes) { dialog, whish ->
                detailsPlayListViewModel.deleteTrack(
                    trackId!!,
                    albumId!!
                )
            }
        confirmDialog.show()
    }

    private fun showTracksInAlbum(content: List<Track>) {
        binding.mediaImagePlaceHolder.visibility = View.GONE
        binding.tvPlaceHolder.visibility = View.GONE
        binding.recyclerBottomSheet.isVisible = true
        adapter = AdapterTracks({
            findNavController()
                .navigate(
                    R.id.action_detailsPlayList_to_playerFragment,
                    PlayerFragment.createArgs(it)
                )
        })
        adapter.longListener = { deleteTrack(it.trackId) }
        adapter.updateData(content)

        binding.recyclerBottomSheet.adapter = adapter
        binding.recyclerBottomSheet.layoutManager = LinearLayoutManager(requireContext())
        adapter.notifyDataSetChanged()

    }

    private fun showAlbumContent(album: Album) {
        val radius = binding.root.context.resources.getDimensionPixelSize(R.dimen.marginTop10)
        Glide.with(this)
            .load(album.pathImage ?: "")
            .placeholder(R.drawable.ic_placeholder_360)
            .into(binding.imageViewAlbum)
        Glide.with(this)
            .load(album.pathImage ?: "")
            .placeholder(R.drawable.ic_placeholder_360)
            .centerCrop()
            .transform(RoundedCorners(radius))
            .into(binding.artAlbum)

        binding.nameAlbumTv.text = album.albumName ?: ""
        binding.albumNameTv.text = album.albumName ?: ""
        binding.descriptionAlbumTv.text = album.albumDescription ?: ""
        binding.trackCountTv.text = getPluralTracks(album.tracksCount)
        binding.tracksCountTv.text = getPluralTracks(album.tracksCount)
    }

    private fun showFullDurationTracksInAlbum(tracks: List<Track>) {
        val totalSeconds = tracks.sumOf { track ->
            track.duration?.split(":")?.let { parts ->
                if (parts.size == 2) {
                    (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0)
                } else 0
            } ?: 0
        }

        val minutes = totalSeconds / 60
        binding.durationAllTracksTv.text = getPluralMinutes(minutes)
    }

    private fun showEmpty() {
        binding.mediaImagePlaceHolder.visibility = View.VISIBLE
        binding.tvPlaceHolder.visibility = View.VISIBLE
        binding.recyclerBottomSheet.visibility = View.GONE
        binding.durationAllTracksTv.text = getPluralMinutes(0)
    }

    private fun getPluralTracks(count: Int): String {
        return context?.resources!!.getQuantityString(R.plurals.tracks_count, count, count)
    }

    private fun getPluralMinutes(count: Int): String {
        return context?.resources!!.getQuantityString(R.plurals.minutes_count, count, count)
    }

    companion object {
        const val ALBUM_KEY = "album"

        fun createArgs(albumId: Int): Bundle {
            return Bundle().apply {
                putInt(ALBUM_KEY, albumId)
            }
        }
    }
}