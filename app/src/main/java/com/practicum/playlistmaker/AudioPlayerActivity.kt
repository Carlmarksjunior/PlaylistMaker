package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var trackName: String
    private lateinit var trackNameTv: TextView
    private lateinit var artistName: String
    private lateinit var artistNameTv: TextView
    private lateinit var durationTv: TextView
    private lateinit var artSong: String
    private lateinit var artSongImage: ImageView
    private lateinit var countryTv: TextView
    private lateinit var genreTv: TextView
    private lateinit var collectionTv: TextView
    private lateinit var releaseDateTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        backButton = findViewById<ImageButton>(R.id.menu_button_audio_player)
        backButton.setOnClickListener {
            finish()
        }

        trackName = intent.getStringExtra("trackName").toString()
        trackNameTv = findViewById<TextView>(R.id.trackName)
        trackNameTv.text = trackName

        artistName = intent.getStringExtra("artistName").toString()
        artistNameTv = findViewById<TextView>(R.id.artistName)
        artistNameTv.text = artistName

        val duration: String? = intent.getStringExtra("duration")
        if (!duration.isNullOrEmpty()){
            durationTv = findViewById<TextView>(R.id.durationValue)
            durationTv.text = duration
        }else{
            val durationGroup = findViewById<Group>(R.id.durationGroup)
            durationGroup.visibility = View.GONE
        }


        artSong = intent.getStringExtra("artSongImage").toString()
        artSong = artSong.replaceAfterLast('/', "512x512bb.jpg")
        artSongImage = findViewById<ImageView>(R.id.artImage)
        Glide.with(this)
            .load(artSong)
            .placeholder(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(artSongImage)

        val country: String? = intent.getStringExtra("country")
        if (!country.isNullOrEmpty()){
            countryTv = findViewById<TextView>(R.id.cityValue)
            countryTv.text = country
        }else{
            val countryGroup = findViewById<Group>(R.id.cityGroup)
            countryGroup.visibility = View.GONE
        }


        val genre: String? = intent.getStringExtra("genre")
        if (!genre.isNullOrEmpty()){
            genreTv = findViewById<TextView>(R.id.jenreValue)
            genreTv.text = genre
        }else{
            val genreGroup = findViewById<Group>(R.id.jenreGroup)
            genreGroup.visibility = View.GONE
        }



        var collection: String? = intent.getStringExtra("collection")
        collectionTv = findViewById<TextView>(R.id.albumNameValue)
        if (!collection.isNullOrEmpty()){
            collectionTv.text = collection
        }else{
            val collectionGroup = findViewById<Group>(R.id.albomGroup)
            collectionGroup.visibility = View.GONE
        }


        var releaseYear: String? = intent.getStringExtra("releaseDate")
        if (!releaseYear.isNullOrEmpty()){
            releaseYear = releaseYear.substring(0,4)
            releaseDateTv = findViewById<TextView>(R.id.yearOfSongValue)
            releaseDateTv.text = releaseYear
        }else{
            val collectionGroup = findViewById<Group>(R.id.yearGroup)
            collectionGroup.visibility = View.GONE
        }


    }
}
