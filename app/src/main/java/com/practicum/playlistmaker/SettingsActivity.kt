package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val buttonOnBackButton = findViewById<Button>(R.id.backButton)
        buttonOnBackButton.setOnClickListener{
            finish()
        }
    }
}