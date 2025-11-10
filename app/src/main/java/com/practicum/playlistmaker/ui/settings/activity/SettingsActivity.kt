package com.practicum.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.application.App
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingsViewModel = ViewModelProvider(this, SettingsViewModel.getFactory()).get(
            SettingsViewModel::class.java)
        binding.backButton.setOnClickListener {
            finish()
        }

        val app = applicationContext as App
        binding.themeSwitcher.isChecked = app.darkTheme
        settingsViewModel.observerSettings().observe(this){
            app.darkTheme = it
            binding.themeSwitcher.isChecked = it

        }

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            settingsViewModel.saveTheme()
        }




        settingsViewModel.observerSharing().observe(this){
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, it.text)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
        }



        binding.shareApp.setOnClickListener {
            settingsViewModel.sharing()
        }



        settingsViewModel.observerSupport().observe(this){
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



        settingsViewModel.observerUserAgreement().observe(this){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
            startActivity(intent)
        }

        binding.buttonUserArgemeent.setOnClickListener {
            settingsViewModel.userAgreement()
        }

        }
    }
