package com.practicum.playlistmaker.ui.playList.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.databinding.FragmentPlayListCreateBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.presentation.playList.view_model.PlayListCreateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListCreateFragment : Fragment() {

    private var _binding: FragmentPlayListCreateBinding? = null
    private val binding get() = _binding!!

    private lateinit var textWatcherForName: TextWatcher

    private lateinit var textWatcherForDescription: TextWatcher

    private var albumName: String? = null

    private var albumImagePath: String? = null

    private var albumDescription: String? = null

    private val playListCreateViewModel by viewModel<PlayListCreateViewModel>()

    lateinit var confirmDialog: MaterialAlertDialogBuilder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButtonPlayList.setOnClickListener {
            if (!albumName.isNullOrEmpty()||!albumImagePath.isNullOrEmpty()||!albumDescription.isNullOrEmpty()){
                confirmDialog.show()
            }else{
                findNavController().popBackStack()
            }
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){uri ->
            if (uri!=null){
                Glide.with(this)
                    .load(uri)
                    .transform(RoundedCorners(8))
                    .into(binding.imagePlayList)
                albumImagePath = "album_cover_${System.currentTimeMillis()}.jpg"
                playListCreateViewModel.saveImageToPrivateStorage(uri,albumImagePath?:"")
            }else{}
        }

        binding.imagePlayList.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        playListCreateViewModel.observePathImageLiveData().observe(viewLifecycleOwner){
            albumImagePath = it
        }

        textWatcherForName = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.createButton.isEnabled = !p0.isNullOrEmpty()
                albumName = p0?.toString()?: ""
            }

        }
        textWatcherForName.let { binding.namePlayList.addTextChangedListener(it) }

        textWatcherForDescription = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                albumDescription = p0?.toString()?: ""
            }

        }
        textWatcherForDescription.let { binding.discriptionPlayList.addTextChangedListener(it)}

        binding.createButton.setOnClickListener {
            playListCreateViewModel.insertAlbum(Album(albumName = albumName, albumDescription = albumDescription, pathImage = albumImagePath))

        }

        playListCreateViewModel.observerInsertTrackLiveData().observe(viewLifecycleOwner){
            if (it){
                Toast.makeText(requireActivity(), "Плейлист '$albumName' создан", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireActivity(), "Плейлист c именем: '$albumName' уже существует", Toast.LENGTH_SHORT).show()

            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена"){dialog, which->}
            .setPositiveButton("Завершить"){dialog,whish -> findNavController().popBackStack()}


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (!albumName.isNullOrEmpty()||!albumImagePath.isNullOrEmpty()||!albumDescription.isNullOrEmpty()){
                    confirmDialog.show()
                }else{
                    findNavController().navigateUp()
                }

            }
        })

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}