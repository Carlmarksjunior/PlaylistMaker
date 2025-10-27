package com.practicum.playlistmaker.ui.player

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.interactor.AudioPlayerInteractor
import com.practicum.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable
import java.text.SimpleDateFormat
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
    private lateinit var playStop: ImageButton
    private val handler: Handler = Handler(Looper.getMainLooper())
    private lateinit var timerTv: TextView
    private lateinit var timer: Runnable
    private var playerState = STATE_DEFAULT

    private var isPlayState = true
    val mediaPlayer: AudioPlayerInteractor = Creator.provideAudioPlayerInteractor()


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        backButton = findViewById<ImageButton>(R.id.menu_button_audio_player)
        backButton.setOnClickListener {
            finish()
        }

        timerTv = findViewById<TextView>(R.id.timeNow)
        val track = intent.getParcelableExtra<Track>(TRACK_KEY)

        trackNameTv = findViewById<TextView>(R.id.trackName)
        if (!track?.trackName.isNullOrEmpty()) {
            trackNameTv.text = track.trackName
        } else {
            trackNameTv.visibility = View.GONE
        }

        artistNameTv = findViewById<TextView>(R.id.artistName)
        if (!track?.artistName.isNullOrEmpty()) {
            artistNameTv.text = track.artistName
        } else {
            artistNameTv.visibility = View.GONE
        }

        val durationString = if (track?.duration != null) {
            track.duration
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

        if (!track?.releaseYear.isNullOrEmpty()) {
            releaseDateTv = findViewById<TextView>(R.id.yearOfSongValue)
            releaseDateTv.text = track.releaseYear
        } else {
            val collectionGroup = findViewById<Group>(R.id.yearGroup)
            collectionGroup.visibility = View.GONE
        }
        playStop = findViewById<ImageButton>(R.id.playStop)
        playStop.isEnabled= false
        if (!track?.previewUrl.isNullOrEmpty()){
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playStop.isEnabled = true
                playerState = STATE_PREPARED
            }
            mediaPlayer.setOnCompletionListener {
                playStop.setImageResource(R.drawable.ic_playstop_84)
                isPlayState = true
                playerState = STATE_PREPARED
                handler.removeCallbacksAndMessages(null)
                timerTv.text =getString(R.string.reset_timer)
            }
        }

        timer = Runnable {
            timerTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition())
            handler.postDelayed(timer, MUSIC_TIMER_DELAY)
        }
        playStop.setOnClickListener {
            if (isPlayState){
                playStop.setImageResource(R.drawable.ic_stopplay_84)
                mediaPlayer.play()
                playerState = STATE_PLAYING
                timer = Runnable{ timerTv.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.getCurrentPosition())
                    handler.postDelayed(timer, MUSIC_TIMER_DELAY)}
                handler.postDelayed(timer, MUSIC_TIMER_DELAY)


            }else{
                playStop.setImageResource(R.drawable.ic_playstop_84)
                mediaPlayer.pause()
                playerState = STATE_PAUSED
                handler.removeCallbacks ( timer )
            }
            isPlayState = !isPlayState
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
        handler.removeCallbacks ( timer )
        isPlayState=true
        playStop.setImageResource(R.drawable.ic_playstop_84)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks ( timer )
    }
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        private const val MUSIC_TIMER_DELAY = 500L
        const val TRACK_KEY = "track"
    }
}