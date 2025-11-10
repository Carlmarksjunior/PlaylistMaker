package com.practicum.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.ui.media.MediaActivity
import com.practicum.playlistmaker.ui.settings.activity.SettingsActivity
import com.practicum.playlistmaker.ui.search.activity.SearchActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch=findViewById<Button>(R.id.searchButton)
        buttonSearch.setOnClickListener{
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        val buttonMedia=findViewById<Button>(R.id.mediaButton)
        buttonMedia.setOnClickListener{
            val intent = Intent(this, MediaActivity::class.java)
            startActivity(intent)
        }
        val buttonSetting = findViewById<Button>(R.id.settingButton)
        buttonSetting.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}