package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.presentation.media.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.presentation.media.view_model.state.FavoriteTracksState
import com.practicum.playlistmaker.ui.player.fragment.PlayerFragment
import com.practicum.playlistmaker.ui.search.fragment.AdapterTracks
import com.practicum.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoritesTracksFragment : Fragment() {

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AdapterTracks
    private val favoriteTracksViewModel by viewModel<FavoriteTracksViewModel>()
    private var isClickedAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteTracksViewModel.startDb()
        adapter = AdapterTracks({
            if (clickDebounce()){
                findNavController().navigate(R.id.action_mediaFragment_to_playerFragment,
                    PlayerFragment.createArgs(it))
            }
        })
        binding.recyclerViewTracks.adapter = adapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())
        favoriteTracksViewModel.observeStateLiveData().observe(viewLifecycleOwner){
            render(it)
        }
    }

    private fun render(state: FavoriteTracksState){
        when(state){
            is FavoriteTracksState.Empty -> showEmpty()
            is FavoriteTracksState.Content ->showContent(state.tracks)
        }
    }
    private fun showEmpty(){
        binding.mediaImagePlaceHolder.visibility = View.VISIBLE
        binding.textPlaceholder.visibility = View.VISIBLE
        binding.recyclerViewTracks.visibility = View.GONE
    }
    private fun showContent(tracks: List<Track>){
        binding.mediaImagePlaceHolder.visibility = View.GONE
        binding.textPlaceholder.visibility = View.GONE
        binding.recyclerViewTracks.visibility = View.VISIBLE
        adapter.trackList.clear()
        adapter.trackList.addAll(tracks)
        adapter.notifyDataSetChanged()

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private val clickDebounce:(Boolean)-> Unit = debounce<Boolean>(CLICK_DEBOUNCE_DELAY,
        lifecycleScope,
        false,
        {param->isClickedAllowed = param})
    private fun clickDebounce(): Boolean {
        val current = isClickedAllowed
        if (isClickedAllowed) {
            isClickedAllowed = false
            clickDebounce(true)
        }

        return current
    }
    companion object {
        fun newInstance() =FavoritesTracksFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}