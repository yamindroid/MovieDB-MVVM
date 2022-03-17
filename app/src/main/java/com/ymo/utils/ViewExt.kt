package com.ymo.utils

import android.app.Service
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.ymo.R

val Context.networkInfo: NetworkInfo? get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo


fun View.showKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.showSoftInput(this, 0)
}

fun View.hideKeyboard() {
    (this.context.getSystemService(Service.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    hideKeyboard()
    Snackbar.make(this, snackbarText, timeLength).show()
}

fun View.showToast(toastText: String, timeLength: Int) {
    hideKeyboard()
    Toast.makeText(this.context, toastText, timeLength).show()
}

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View {
    return LayoutInflater.from(context)
        .inflate(layoutId, this, false)
}


fun ImageView.loadFromUrl(url: String?) =
    if(url!= null){
        Glide.with(this.context.applicationContext)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }else{
        Glide.with(this.context.applicationContext)
            .load(R.drawable.ic_launcher_background)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
