package com.practicum.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlayListsBinding
import com.practicum.playlistmaker.ui.media.view_model.PlayListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListFragment : Fragment() {

    private var _binding: FragmentPlayListsBinding? = null
    private val binding get() = _binding!!

    private val playListViewModel by viewModel<PlayListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayListsBinding.inflate(inflater,container,false)

        return binding.root
    }

    companion object {

        fun newInstance() = PlayListFragment()
    }
}