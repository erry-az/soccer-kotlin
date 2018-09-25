package id.eaz.soccerclub

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun TextView.enableMarque(cs: CharSequence, delayed: Long = 1500){
    this.text = cs
    this.isSelected = false
    this.postDelayed({
        isSelected = true
    }, delayed)
}

fun ImageView.loadImage(context: Context, url: String?,
                        placeholder: Int = R.drawable.badge_placeholder, resize: Int = 80){
    val option = RequestOptions().placeholder(placeholder)
            .error(placeholder)
            .fallback(placeholder)

    if(resize > 0) option.override(resize)

    Glide.with(context).load(url).apply(option).into(this)
}

fun String?.ctr() : String {
    return this?.replace(";", "\n") ?: "-"
}

fun String?.cstr() : String {
    return this?.replace("; ", "\n") ?: "-"
}

fun Activity.hideKeyboard(){
    val inputMethodManager = this.getSystemService(
            Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(
            this.currentFocus?.windowToken, 0)
}