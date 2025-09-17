package com.practicum.playlistmaker

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var trackNameTv: TextView

    private lateinit var artistNameTv: TextView
    private lateinit var durationTv: TextView
    private lateinit var artSong: String
    private lateinit var artSongImage: ImageView
    private lateinit var countryTv: TextView
    private lateinit var genreTv: TextView
    private lateinit var collectionTv: TextView
    private lateinit var releaseDateTv: TextView

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        backButton = findViewById<ImageButton>(R.id.menu_button_audio_player)
        backButton.setOnClickListener {
            finish()
        }

        val track = intent.getParcelableExtra<Track>("track", Track::class.java)
        trackNameTv = findViewById<TextView>(R.id.trackName)
        trackNameTv.text = track?.trackName

        artistNameTv = findViewById<TextView>(R.id.artistName)
        artistNameTv.text = track?.artistName

        val durationString = if (track?.trackTimeMillis != null) {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(track.trackTimeMillis))
        } else {
            null
        }
        durationTv = findViewById<TextView>(R.id.durationValue)
        if (durationString != null) {
            durationTv.text = durationString
        } else {
            val durationGroup = findViewById<Group>(R.id.durationGroup)
            durationGroup.visibility = View.GONE
        }

        artSong = track?.artworkUrl100.toString().replaceAfterLast('/', "512x512bb.jpg")
        artSongImage = findViewById<ImageView>(R.id.artImage)
        val radiusPx = resources.getDimensionPixelSize(R.dimen.cornerRadius10)
        Glide.with(this)
            .load(artSong)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(radiusPx))
            .into(artSongImage)

        if (!track?.country.isNullOrEmpty()) {
            countryTv = findViewById<TextView>(R.id.cityValue)
            countryTv.text = track.country
        } else {
            val countryGroup = findViewById<Group>(R.id.cityGroup)
            countryGroup.visibility = View.GONE
        }



        if (!track?.primaryGenreName.isNullOrEmpty()) {
            genreTv = findViewById<TextView>(R.id.jenreValue)
            genreTv.text = track.primaryGenreName
        } else {
            val genreGroup = findViewById<Group>(R.id.jenreGroup)
            genreGroup.visibility = View.GONE
        }



        if (!track?.collectionName.isNullOrEmpty()) {
            collectionTv = findViewById<TextView>(R.id.albumNameValue)
            collectionTv.text = track.collectionName
        } else {
            val collectionGroup = findViewById<Group>(R.id.albomGroup)
            collectionGroup.visibility = View.GONE
        }



        if (!track?.releaseDate.isNullOrEmpty()) {
            releaseDateTv = findViewById<TextView>(R.id.yearOfSongValue)
            releaseDateTv.text = track.releaseDate.substring(0,4)
        } else {
            val collectionGroup = findViewById<Group>(R.id.yearGroup)
            collectionGroup.visibility = View.GONE
        }


    }
}
