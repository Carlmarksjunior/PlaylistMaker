package com.practicum.playlistmaker.ui.settings.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSettingBinding
import com.practicum.playlistmaker.ui.settings.application.App
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingsViewModel by viewModel<SettingsViewModel>()

    private lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = requireContext().applicationContext as App
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitcher.isChecked = app.darkTheme
        settingsViewModel.observerThemeLiveData().observe(viewLifecycleOwner){
            app.darkTheme = it
            binding.themeSwitcher.isChecked = it

        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.saveTheme()
        }




        settingsViewModel.observerSharing().observe(viewLifecycleOwner){
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, it.text)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
        }



        binding.shareApp.setOnClickListener {
            settingsViewModel.sharing()
        }



        settingsViewModel.observerSupport().observe(viewLifecycleOwner){
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(it.email))
                putExtra(Intent.EXTRA_SUBJECT, it.subject)
                putExtra(Intent.EXTRA_TEXT, it.message)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.my_email)))
        }

        binding.messageToSupport.setOnClickListener {
            settingsViewModel.supportEmailData()
        }



        settingsViewModel.observerUserAgreement().observe(viewLifecycleOwner){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }

        binding.buttonUserArgemeent.setOnClickListener {
            settingsViewModel.userAgreement()
        }

    }
    }

