package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSearch=findViewById<Button>(R.id.searchButton)
        buttonSearch.setOnClickListener{
            val intent = Intent(this,SearchActivity::class.java)
            startActivity(intent)
        }
        //Реализация через лямбду
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
