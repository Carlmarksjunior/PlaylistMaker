package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


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

    private val itunesBaseUrl = "https://itunes.apple.com/"
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(itunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }
    private lateinit var placeholderImage: ImageView
    private lateinit var placeHolderTv: TextView
    private lateinit var recyclerViewTracks: RecyclerView
    lateinit var adapter: AdapterTracks
    private lateinit var buttonOnBackButton: Button
    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var placeHolderButton: Button
    private lateinit var lastQuery: String

    private lateinit var tvSearchHistory: TextView

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var listOfHistory: MutableList<Track>


    private val trackList = mutableListOf<Track>()


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
        sharedPreferences = getSharedPreferences("music_history_prefs", MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        listOfHistory = searchHistory.getSaveTracks().toMutableList()
        adapter = AdapterTracks(trackList,searchHistory)
        recyclerViewTracks.adapter = adapter
        recyclerViewTracks.layoutManager= LinearLayoutManager(this)


        inputEditText.setText(editText)
        inputEditText.addTextChangedListener(
            onTextChanged = { s, start, before, count ->
                clearButton.isVisible = !s.isNullOrEmpty()
                if (inputEditText.hasFocus()&&inputEditText.text.isEmpty()){
                    if (!listOfHistory.isNullOrEmpty()){
                        handleTextAndFocusState()
                    }
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
            retrofitEnqueue(lastQuery)
            }else if (placeHolderButton.text== getString(R.string.clear_history)){
                searchHistory.clearHistory()
                updateSearchHistory()
                adapter.updateData(mutableListOf())
                tvSearchHistory.visibility = View.GONE
                placeHolderButton.visibility = View.GONE
            }
        }
    }

    fun retrofitEnqueue(trackName: String) {
        retrofit.search(trackName.trimStart())
            .enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse?>,
                    response: Response<ItunesResponse?>
                ) {
                    if (response.isSuccessful) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            response.body()?.results?.let { tracks ->
                                trackList.addAll(tracks)
                            }
                            visabilityGone()
                            recyclerViewTracks.visibility = View.VISIBLE
                            adapter.notifyDataSetChanged()
                        } else if (trackList.isEmpty()) {
                            visabilityGone()
                            showPlaceHolder(false)
                        } else {
                            visabilityGone()
                            showPlaceHolder(true)
                        }
                    } else {
                        visabilityGone()
                        showPlaceHolder(true)
                    }
                }

                override fun onFailure(call: Call<ItunesResponse?>, t: Throwable) {
                    visabilityGone()
                    adapter.notifyDataSetChanged()
                    showPlaceHolder(true)
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
        listOfHistory = searchHistory.getSaveTracks().toMutableList() // ОБНОВЛЯЕМ историю
    }
    @SuppressLint("ServiceCast")
    fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        val EDIT_INPUT = null
    }
}




