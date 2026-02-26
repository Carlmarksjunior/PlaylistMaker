package com.practicum.playlistmaker.ui.playList.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.playList.view_model.PlayListRedactViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListRedactFragment : PlayListCreateFragment() {

    override val playListCreateViewModel by viewModel<PlayListRedactViewModel>()

    private var albumId: Int? = null

    override var albumName: String?
        get() = super.albumName
        set(value) {super.albumName = value}

    override var albumDescription: String?
        get() = super.albumDescription
        set(value) {super.albumDescription = value}

    override var albumImagePath: String?
        get() = super.albumImagePath
        set(value) {super.albumImagePath = value}


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumId = arguments?.getInt(ALBUM_KEY)
        playListCreateViewModel.getAlbumFromId(albumId)
        binding.mainTV.text = context?.getString(R.string.redact)
        binding.createButton.text = context?.getString(R.string.save)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(this)
                        .load(uri)
                        .transform(RoundedCorners(8))
                        .into(binding.imagePlayList)
                    playListCreateViewModel.saveImageToPrivateStorage(
                        uri,
                        "album_cover_${System.currentTimeMillis()}.jpg"
                    )
                } else {
                }
            }

        binding.imagePlayList.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        playListCreateViewModel.observerAlbumLiveData().observe(viewLifecycleOwner) {
            albumName = it.albumName
            albumImagePath = it.albumImagePath
            albumDescription = it.albumDescription
            Log.d("tete","asd$albumName,$albumImagePath,$albumDescription=======$it")
            showContent()

        }


        binding.createButton.setOnClickListener {
            playListCreateViewModel.updateAlbum(
                albumId!!,
                albumName!!,
                albumDescription!!,
                albumImagePath!!
            )
            Log.d("tete","отправка на обновление $albumName,$albumDescription,$albumImagePath")
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()

            }
        })


    }

    private fun showContent() {
        binding.namePlayList.setText(this.albumName)
        binding.discriptionPlayList.setText(this.albumDescription)
        Glide.with(this)
            .load(this.albumImagePath)
            .into(binding.imagePlayList)
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