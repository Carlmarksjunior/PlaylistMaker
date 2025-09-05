package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial



class SettingsActivity : AppCompatActivity() {
    private lateinit var buttonOnBackButton: Button
    private lateinit var buttonOnShareApp: Button
    private lateinit var buttonMessageToSupport: Button
    private lateinit var buttonUserArgement: Button

    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
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


        buttonOnShareApp = findViewById<Button>(R.id.shareApp)
        buttonOnShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_YP))
            startActivity(shareIntent)

        }
        buttonMessageToSupport = findViewById<Button>(R.id.messageToSupport)
        buttonMessageToSupport.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                resources.getString(R.string.subject_to_mail)
            )
            shareIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(resources.getString(R.string.my_email))
            )
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.message_to_mail))
            startActivity(shareIntent)
        }
        buttonUserArgement = findViewById<Button>(R.id.buttonUserArgemeent)
        buttonUserArgement.setOnClickListener {

            val url = Uri.parse(resources.getString(R.string.practicum_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}


