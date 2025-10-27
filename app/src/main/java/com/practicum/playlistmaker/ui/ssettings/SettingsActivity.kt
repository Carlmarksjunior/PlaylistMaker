package com.practicum.playlistmaker.ui.ssettings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.interactor.SupportSharingInteractor

class SettingsActivity : AppCompatActivity() {
    private lateinit var buttonOnBackButton: Button

    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val supportSharingInteractor: SupportSharingInteractor = Creator.provideSupportSharingInteractor(this)
        buttonOnBackButton = findViewById<Button>(R.id.backButton)
        buttonOnBackButton.setOnClickListener {
            finish()
        }

        themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        val app = applicationContext as App
        themeSwitcher.isChecked = app.darkTheme
        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            app.switchTheme(checked)
        }

        findViewById<Button>(R.id.shareApp).setOnClickListener {
            val sharingData = supportSharingInteractor.getSharingAppData()
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, sharingData.text)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.share_app)))
        }

        findViewById<Button>(R.id.messageToSupport).setOnClickListener {
            val supportData = supportSharingInteractor.getSupportEmailData()
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(supportData.email))
                putExtra(Intent.EXTRA_SUBJECT, supportData.subject)
                putExtra(Intent.EXTRA_TEXT, supportData.message)
            }
            startActivity(Intent.createChooser(intent, getString(R.string.my_email)))
        }

        findViewById<Button>(R.id.buttonUserArgemeent).setOnClickListener {
            val url = supportSharingInteractor.getUserAgreementUrl()
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}