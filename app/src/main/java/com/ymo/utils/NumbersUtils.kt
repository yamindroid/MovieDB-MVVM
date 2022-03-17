package com.ymo.utils
import java.text.SimpleDateFormat
import java.util.*

fun getLocalTimeFromUnix(unixTime: String): String {
    val simpleDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val date = dateFormat.parse(unixTime)
    return simpleDateFormat.format(date)
}

