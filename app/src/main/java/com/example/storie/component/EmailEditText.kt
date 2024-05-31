package com.example.storie.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.example.storie.R
import java.util.regex.Matcher
import java.util.regex.Pattern

class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : AppCompatEditText(
    context,
    attrs
) {
    private val emailPattern: Pattern = Pattern.compile(
        "^\\S+@\\S+\\.\\S+$"
    )

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString().trim()

                error = if (!isValidEmail(email)) {
                    context.getString(R.string.validation_email)
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        val matcher: Matcher = emailPattern.matcher(email)
        return matcher.matches()
    }

}