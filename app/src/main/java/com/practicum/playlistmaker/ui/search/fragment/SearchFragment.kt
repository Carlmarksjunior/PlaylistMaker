package com.practicum.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.player.fragment.PlayerFragment
import com.practicum.playlistmaker.ui.search.state.TrackState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    var editText: String? = SearchFragment.Companion.EDIT_INPUT

    lateinit var adapter: AdapterTracks

    private val searchViewModel by viewModel<SearchViewModel>()

    private lateinit var textWatcher: TextWatcher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AdapterTracks({ clickedTrack ->
            searchViewModel.saveTrack(clickedTrack)

            if (clickDebounce()){
                findNavController().navigate(R.id.action_searchFragment_to_playerFragment,
                    PlayerFragment.createArgs(clickedTrack))
            }
        })


        binding.recyclerViewTracks.adapter = adapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(requireContext())


        searchViewModel.observeStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
        searchViewModel.observeHistoryLiveData().observe(viewLifecycleOwner){
            adapter.searchHistory.clear()
            adapter.searchHistory.addAll(it)
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
                searchViewModel.searchDebounce(
                    changedText = s?.toString() ?: ""
                )
            }
        }
        textWatcher.let { binding.editTextSearch.addTextChangedListener(it) }

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


            binding.buttonPlaceHolder.setOnClickListener {
                if (binding.buttonPlaceHolder.text == getString(R.string.refresh)) {
                    searchViewModel.searchDebounce(binding.editTextSearch.text.toString())
                } else if (binding.buttonPlaceHolder.text == getString(R.string.clear_history)) {
                    searchViewModel.clearHistoryTracks()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        val inputMethodManager = requireActivity()
            .getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(
            requireActivity().currentFocus?.windowToken,
            0
        )
    }

    private var isClickedAllowed = true
    private val handler: Handler = Handler(Looper.getMainLooper())
    private fun clickDebounce(): Boolean {
        val current = isClickedAllowed
        if (isClickedAllowed) {
            isClickedAllowed = false
            handler.postDelayed({ isClickedAllowed = true },
                SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
            )
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

    companion object {
        const val EDIT_TEXT = "EDIT_TEXT"
        val EDIT_INPUT = null
        private const val CLICK_DEBOUNCE_DELAY = 1000L

    }
}