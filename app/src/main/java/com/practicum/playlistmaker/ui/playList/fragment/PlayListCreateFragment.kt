package com.practicum.playlistmaker.ui.playList.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayListCreateBinding
import com.practicum.playlistmaker.domain.db.playLists.model.Album
import com.practicum.playlistmaker.presentation.playList.view_model.PlayListCreateViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

open class PlayListCreateFragment : Fragment() {

    private var _binding: FragmentPlayListCreateBinding? = null
    open val binding get() = _binding!!


    open var albumName: String? = null

    open var albumImagePath: String? = null

    open var albumDescription: String? = null

    open val playListCreateViewModel by viewModel<PlayListCreateViewModel>()

    open lateinit var confirmDialog: MaterialAlertDialogBuilder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayListCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playListCreateViewModel.observerAlbumLiveData().observe(viewLifecycleOwner){
            albumName=it.albumName
            albumImagePath = it.albumImagePath
            albumDescription = it.albumDescription
        }

        playListCreateViewModel.observeInsertAlbumState().observe(viewLifecycleOwner){
            if (it){
                Toast.makeText(requireContext(), getString(R.string.complete_create_playlist,albumName), Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(), getString(R.string.playlist_exists,albumName), Toast.LENGTH_SHORT).show()
            }
        }
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
                playListCreateViewModel.saveImageToPrivateStorage(uri,"album_cover_${System.currentTimeMillis()}.jpg")
            }else{
                Toast.makeText(requireActivity(), getString(R.string.playlist_exists,albumName), Toast.LENGTH_SHORT).show()
            }
        }

        binding.imagePlayList.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }



        binding.namePlayList.doOnTextChanged { p0,p1,p2,p3 ->
            binding.createButton.isEnabled = !p0.isNullOrEmpty()&&!p0.isBlank()
            this.albumName=p0?.toString()?.trimStart()?:""
        }


        binding.discriptionPlayList.doOnTextChanged { p0,p1,p2,p3 ->
            this.albumDescription = p0?.toString()?.trimStart()?: ""
        }

        binding.createButton.setOnClickListener {
            playListCreateViewModel.insertAlbum(Album(albumName = albumName, albumDescription = albumDescription, pathImage = albumImagePath))

        }


        confirmDialog = MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.finish_to_create_playlist)
            .setMessage(R.string.all_data_will_be_lose)
            .setNegativeButton(R.string.cancel){dialog, which->}
            .setPositiveButton(R.string.finish){dialog,whish -> findNavController().popBackStack()}


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