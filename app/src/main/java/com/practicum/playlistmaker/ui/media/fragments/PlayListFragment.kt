package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayListsBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.presentation.media.view_model.PlayListViewModel
import com.practicum.playlistmaker.presentation.media.view_model.state.PlayListState
import com.practicum.playlistmaker.ui.detailsPlayList.fragment.DetailsPlayListFragment
import com.practicum.playlistmaker.ui.media.fragments.adapter.AlbumsAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListFragment : Fragment() {

    private var _binding: FragmentPlayListsBinding? = null
    private val binding get() = _binding!!

    private val playListViewModel by viewModel<PlayListViewModel>()

    private lateinit var adapter: AlbumsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlayListButtom.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playListCreateFragment)
        }
        playListViewModel.getAllAlbums()
        playListViewModel.observePlayerListLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

    }

    private fun render(playListState: PlayListState) {
        when (playListState) {
            is PlayListState.Empty -> showEmpty(playListState.message)
            is PlayListState.Content -> showContent(playListState.albums)
        }
    }

    private fun showEmpty(message: Int) {
        binding.mediaImagePlaceHolder.isVisible = true
        binding.tvPlaceHolder.apply {
            isVisible = true
            text = context.getString(message)
        }

        binding.recyclerViewAlbums.visibility = View.GONE
    }

    private fun showContent(content: List<Album>) {
        adapter = AlbumsAdapter(content,
            context,
            {findNavController().navigate(R.id.action_mediaFragment_to_detailsPlayList,
                DetailsPlayListFragment.createArgs(it.id))})
        binding.recyclerViewAlbums.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.mediaImagePlaceHolder.visibility = View.GONE
        binding.tvPlaceHolder.visibility = View.GONE
        binding.recyclerViewAlbums.visibility = View.VISIBLE
        binding.recyclerViewAlbums.adapter = adapter
        adapter.notifyDataSetChanged()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance() = PlayListFragment()
    }
}