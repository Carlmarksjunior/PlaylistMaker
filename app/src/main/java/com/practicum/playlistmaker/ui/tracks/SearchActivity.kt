package com.practicum.playlistmaker.ui.tracks

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.api.interactor.HistoryInteractor
import com.practicum.playlistmaker.domain.api.interactor.TracksInteractor
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.ui.player.AudioPlayerActivity

class SearchActivity : AppCompatActivity() {
    var editText: String? = EDIT_INPUT
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, editText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText = savedInstanceState.getString(EDIT_TEXT, EDIT_INPUT)
    }


    private lateinit var placeholderImage: ImageView
    private lateinit var placeHolderTv: TextView
    private lateinit var recyclerViewTracks: RecyclerView
    lateinit var adapter: AdapterTracks
    private lateinit var buttonOnBackButton: Button
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var placeHolderButton: Button

    private lateinit var tvSearchHistory: TextView

    private lateinit var searchHistory: List<Track>
    private var listOfHistory: MutableList<Track> = mutableListOf()
    private lateinit var progressBar: ProgressBar

    val searchTrackInteractor: TracksInteractor = Creator.provideTracksInteractor()

    private var lastQuery: String = ""

    private val trackList = mutableListOf<Track>()
    private val searchRunnable = Runnable{retrofitEnqueue(inputEditText.text.toString())}
    private val hideKeyboardRunnable = Runnable { hideKeyboard()}
    private val historyInteractor: HistoryInteractor by lazy {
        Creator.provideHistoryInteractor(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        placeholderImage = findViewById<ImageView>(R.id.image_place_holder)
        placeHolderTv = findViewById<TextView>(R.id.tv_place_holder)
        buttonOnBackButton = findViewById<Button>(R.id.backButtonInSearch)
        recyclerViewTracks = findViewById<RecyclerView>(R.id.recyclerViewTracks)
        inputEditText = findViewById<EditText>(R.id.edit_text_search)
        clearButton = findViewById<ImageView>(R.id.clearIcon)
        placeHolderButton = findViewById<Button>(R.id.button_place_holder)
        tvSearchHistory = findViewById<TextView>(R.id.tv_search_history)
        progressBar = findViewById<ProgressBar>(R.id.progressBar)
        searchHistory = historyInteractor.getSaveTracks()
        adapter = AdapterTracks(trackList, { clickedTrack ->
            historyInteractor.saveTrack(clickedTrack)
            val intent = Intent(this, AudioPlayerActivity::class.java).apply {
                if (clickDebounce()) {
                    putExtra(AudioPlayerActivity.Companion.TRACK_KEY, clickedTrack)
                }
            }
            startActivity(intent)
        })
        recyclerViewTracks.adapter = adapter
        recyclerViewTracks.layoutManager= LinearLayoutManager(this)


        inputEditText.setText(editText)
        inputEditText.addTextChangedListener(
            onTextChanged = { s, start, before, count ->
                clearButton.isVisible = !s.isNullOrEmpty()
                if (inputEditText.hasFocus()&&inputEditText.text.isEmpty()){
                    if (!listOfHistory.isNullOrEmpty()){
                        progressBar.visibility = View.GONE
                        handleTextAndFocusState()
                    }
                }else{
                    visabilityGone()
                    progressBar.visibility = View.VISIBLE
                    searchDebounce()
                }
            },
            afterTextChanged = { s ->
                editText = s?.toString() ?: ""
            }
        )
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                retrofitEnqueue(inputEditText.text.toString())
                lastQuery = inputEditText.text.toString()
                true
            }
            false
        }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (inputEditText.hasFocus()&&inputEditText.text.isEmpty()){
                updateSearchHistory()
                if (!listOfHistory.isNullOrEmpty()){
                    handleTextAndFocusState()
                }
            }

        }

        buttonOnBackButton.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            inputEditText.clearFocus()
            trackList.clear()
            visabilityGone()
            adapter.notifyDataSetChanged()
        }




        placeHolderButton.setOnClickListener {
            if (placeHolderButton.text == getString(R.string.refresh)){
                lastQuery = inputEditText.text.toString()
                retrofitEnqueue(lastQuery)
            }else if (placeHolderButton.text== getString(R.string.clear_history)){
                historyInteractor.clearHistory()
                updateSearchHistory()
                adapter.updateData(mutableListOf())
                tvSearchHistory.visibility = View.GONE
                placeHolderButton.visibility = View.GONE
            }
        }
    }

    fun retrofitEnqueue(trackName: String) {
        searchTrackInteractor.searchTracks(trackName, object : TracksInteractor.TracksConsumer {
            override fun consume(foundTracks: List<Track>, isNetworkError: Boolean) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    trackList.clear()

                    if (isNetworkError) {
                        visabilityGone()
                        adapter.notifyDataSetChanged()
                        showPlaceHolder(true)
                    } else if (foundTracks.isNotEmpty()) {
                        trackList.addAll(foundTracks)
                        visabilityGone()
                        recyclerViewTracks.visibility = View.VISIBLE
                        adapter.notifyDataSetChanged()
                    } else {
                        visabilityGone()
                        showPlaceHolder(false)
                    }
                }
            }
        })
    }


    fun showPlaceHolder(disconnect: Boolean) {
        if (disconnect) {
            trackList.clear()
            adapter.notifyDataSetChanged()
            placeholderImage.setImageResource(R.drawable.ic_disconnect_120)
            placeHolderTv.text = getString(R.string.disconnect_message)
            placeholderImage.visibility = View.VISIBLE
            placeHolderTv.visibility = View.VISIBLE
            placeHolderButton.text = getString(R.string.refresh)
            placeHolderButton.visibility = View.VISIBLE

        } else {
            trackList.clear()
            adapter.notifyDataSetChanged()
            placeholderImage.setImageResource(R.drawable.ic_not_search_result_120)
            placeHolderTv.text = getString(R.string.result_is_empty)
            placeholderImage.visibility = View.VISIBLE
            placeHolderTv.visibility = View.VISIBLE
        }
    }

    fun visabilityGone() {
        recyclerViewTracks.visibility = View.GONE
        tvSearchHistory.visibility = View.GONE
        placeHolderButton.visibility = View.GONE
        placeholderImage.visibility = View.GONE
        placeHolderTv.visibility = View.GONE
    }
    private fun handleTextAndFocusState() {
        updateSearchHistory()
        val text = inputEditText.text.toString()

        if (text.isEmpty()) {
            if (!listOfHistory.isNullOrEmpty()) {
                tvSearchHistory.visibility = View.VISIBLE
                recyclerViewTracks.visibility = View.VISIBLE
                adapter.updateData(listOfHistory)
                placeHolderButton.text = getString(R.string.clear_history)
                placeHolderButton.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            } else {
                visabilityGone()
            }
        } else {
            placeHolderButton.text = getString(R.string.search)
            placeHolderButton.visibility = View.VISIBLE
            tvSearchHistory.visibility = View.GONE
        }
    }

    private fun updateSearchHistory() {
        listOfHistory = historyInteractor.getSaveTracks().toMutableList()
    }
    @SuppressLint("ServiceCast")
    fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
    private var isClickedAllowed = true
    private val handler: Handler = Handler(Looper.getMainLooper())
    private fun clickDebounce(): Boolean{
        val current = isClickedAllowed
        if (isClickedAllowed){
            isClickedAllowed=false
            handler.postDelayed({isClickedAllowed = true},CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce(){
        handler.removeCallbacks(searchRunnable)
        handler.removeCallbacks ( hideKeyboardRunnable )
        handler.postDelayed(searchRunnable,SEARCH_DEBOUNCE_DELAY)
        handler.postDelayed(hideKeyboardRunnable,SEARCH_DEBOUNCE_DELAY)
    }
    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        val EDIT_INPUT = null
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}