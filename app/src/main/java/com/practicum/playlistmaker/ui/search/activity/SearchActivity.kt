package com.practicum.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import com.practicum.playlistmaker.ui.search.state.TrackState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {
    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        val EDIT_INPUT = null
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }

    var editText: String? = EDIT_INPUT
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT, editText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText = savedInstanceState.getString(EDIT_TEXT, EDIT_INPUT)
    }

    lateinit var adapter: AdapterTracks

    private lateinit var binding: ActivitySearchBinding

    private var viewModel: SearchViewModel? = null

    private lateinit var textWatcher: TextWatcher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = AdapterTracks({ clickedTrack ->
            viewModel?.saveTrack(clickedTrack)
            val intent = Intent(this, PlayerActivity::class.java).apply {
                if (clickDebounce()) {
                    putExtra(PlayerActivity.Companion.TRACK_KEY, clickedTrack)
                }
            }
            startActivity(intent)
        })


        binding.recyclerViewTracks.adapter = adapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(this)

        viewModel =
            ViewModelProvider(this, SearchViewModel.getFactory()).get(SearchViewModel::class.java)

        viewModel?.observeStateLiveData()?.observe(this) {
            render(it)
        }
        viewModel?.observeHistoryLiveData()?.observe(this){
            adapter.searchHistory.clear()
            adapter.searchHistory?.addAll(it)
        }



        binding.editTextSearch.setText(editText)
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString() ?: ""
                binding.clearIcon.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
                if (text.isEmpty()) {
                    if (adapter.searchHistory.isNullOrEmpty()) {
                        binding.tvSearchHistory.visibility = View.GONE
                        binding.recyclerViewTracks.visibility = View.GONE
                        binding.buttonPlaceHolder.visibility = View.GONE

                    } else {
                        binding.tvSearchHistory.visibility = View.VISIBLE
                        binding.recyclerViewTracks.visibility = View.VISIBLE
                        adapter.updateData(adapter.searchHistory!!)
                        binding.buttonPlaceHolder.text = getString(R.string.clear_history)
                        binding.buttonPlaceHolder.visibility = View.VISIBLE
                        binding.tvPlaceHolder.visibility=View.GONE
                        binding.imagePlaceHolder.visibility = View.GONE
                        adapter.notifyDataSetChanged()
                    }


                }
                viewModel?.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
        }
        textWatcher?.let { binding.editTextSearch.addTextChangedListener(it) }

        binding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if (binding.editTextSearch.hasFocus() && binding.editTextSearch.text.isEmpty()) {
                if (adapter.searchHistory.isNotEmpty()) {
                    binding.tvSearchHistory.visibility = View.VISIBLE
                    binding.recyclerViewTracks.visibility = View.VISIBLE
                    adapter.updateData(adapter.searchHistory)
                    binding.buttonPlaceHolder.text = getString(R.string.clear_history)
                    binding.buttonPlaceHolder.visibility = View.VISIBLE
                    adapter.notifyDataSetChanged()
                } else {
                    binding.tvSearchHistory.visibility = View.GONE
                    binding.recyclerViewTracks.visibility = View.GONE
                    binding.buttonPlaceHolder.visibility = View.GONE
                }

            }

            binding.backButtonInSearch.setOnClickListener {
                finish()
            }
            binding.buttonPlaceHolder.setOnClickListener {
                if (binding.buttonPlaceHolder.text == getString(R.string.refresh)) {
                    viewModel?.searchDebounce(binding.editTextSearch.text.toString())
                } else if (binding.buttonPlaceHolder.text == getString(R.string.clear_history)) {
                    viewModel?.clearHistoryTracks()
                    binding.recyclerViewTracks.visibility=View.GONE
                    binding.tvSearchHistory.visibility = View.GONE
                    binding.buttonPlaceHolder.visibility = View.GONE
                }
            }

            binding.clearIcon.setOnClickListener {
                binding.editTextSearch.setText("")
                hideKeyboard()
                binding.editTextSearch.clearFocus()
                adapter.trackList.clear()
                adapter.notifyDataSetChanged()
                hideAllView()
            }

        }
    }

    private fun hideAllView(){
        binding.apply {
            tvSearchHistory.visibility= View.GONE
            recyclerViewTracks.visibility= View.GONE
            buttonPlaceHolder.visibility= View.GONE
            clearIcon.visibility= View.GONE
            imagePlaceHolder.visibility= View.GONE
            tvPlaceHolder.visibility= View.GONE
            progressBar.visibility= View.GONE
        }

    }
    @SuppressLint("ServiceCast")
    fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private var isClickedAllowed = true
    private val handler: Handler = Handler(Looper.getMainLooper())
    private fun clickDebounce(): Boolean {
        val current = isClickedAllowed
        if (isClickedAllowed) {
            isClickedAllowed = false
            handler.postDelayed({ isClickedAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    fun render(state: TrackState) {
        when (state) {
            is TrackState.Loading -> showLoading()
            is TrackState.Content -> showContent(state.tracks)
            is TrackState.Error -> showError(state.errorMessage)
            is TrackState.Empty -> showEmpty(state.message)
        }
    }

    private fun showLoading() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            recyclerViewTracks.visibility = View.GONE
            tvSearchHistory.visibility = View.GONE
            tvPlaceHolder.visibility = View.GONE
            buttonPlaceHolder.visibility = View.GONE
            imagePlaceHolder.visibility = View.GONE
        }
        hideKeyboard()
    }

    private fun showContent(trackList: List<Track>) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerViewTracks.visibility = View.VISIBLE
            tvSearchHistory.visibility = View.GONE
            tvPlaceHolder.visibility = View.GONE
            buttonPlaceHolder.visibility = View.GONE
            imagePlaceHolder.visibility = View.GONE
        }
        adapter.trackList.clear()
        adapter.trackList.addAll(trackList)
        adapter.notifyDataSetChanged()
    }

    private fun showError(errorMessage: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerViewTracks.visibility = View.GONE
            tvSearchHistory.visibility = View.GONE
            tvPlaceHolder.visibility = View.VISIBLE
            tvPlaceHolder.text = errorMessage
            buttonPlaceHolder.visibility = View.VISIBLE
            buttonPlaceHolder.text = getString(R.string.refresh)
            imagePlaceHolder.visibility = View.VISIBLE
            imagePlaceHolder.setImageResource(R.drawable.ic_disconnect_120)
        }

    }

    private fun showEmpty(emptyMessage: String) {
        binding.apply {
            progressBar.visibility = View.GONE
            recyclerViewTracks.visibility = View.GONE
            tvSearchHistory.visibility = View.GONE
            tvPlaceHolder.visibility = View.VISIBLE
            tvPlaceHolder.text = emptyMessage
            buttonPlaceHolder.visibility = View.GONE
            imagePlaceHolder.visibility = View.VISIBLE
            imagePlaceHolder.setImageResource(R.drawable.ic_not_search_result_120)
        }
    }
}