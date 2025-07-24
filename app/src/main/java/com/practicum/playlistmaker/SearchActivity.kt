package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener

class SearchActivity : AppCompatActivity() {
    var editText: String? = EDIT_INPUT
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EDIT_TEXT,editText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        editText=savedInstanceState.getString(EDIT_TEXT, EDIT_INPUT)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val buttonOnBackButton = findViewById<Button>(R.id.backButtonInSearch)
        buttonOnBackButton.setOnClickListener {
            finish()
        }
        val inputEditText = findViewById<EditText>(R.id.edit_text_search)
        inputEditText.setText(editText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            inputEditText.clearFocus()
        }


//        val textWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                clearButton.isVisible = !s.isNullOrEmpty()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                editText = s?.toString() ?:""
//                Log.d(EDIT_TEXT,"$editText")
//            }
//        }
        inputEditText.addTextChangedListener(
            onTextChanged= {s,start,before,count->clearButton.isVisible = !s.isNullOrEmpty() },
            afterTextChanged={s->
                editText = s?.toString() ?:""
                Log.d(EDIT_TEXT,"$editText")
            }
        )
    }

    @SuppressLint("ServiceCast")
    fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
    companion object{
        const val EDIT_TEXT = "EDIT_TEXT"
        val EDIT_INPUT = null
    }
}
