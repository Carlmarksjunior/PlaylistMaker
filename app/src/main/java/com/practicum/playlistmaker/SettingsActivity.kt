package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonOnBackButton = findViewById<Button>(R.id.backButton)
        buttonOnBackButton.setOnClickListener {
            finish()
        }
        val buttonOnShareApp = findViewById<Button>(R.id.shareApp)
        buttonOnShareApp.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.url_YP))
            startActivity(shareIntent)

        }
        val buttonMessageToSupport = findViewById<Button>(R.id.messageToSupport)
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
        val buttonUserArgeement = findViewById<Button>(R.id.buttonUserArgemeent)
        buttonUserArgeement.setOnClickListener {

            val url = Uri.parse(resources.getString(R.string.practicum_offer))
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }
}


