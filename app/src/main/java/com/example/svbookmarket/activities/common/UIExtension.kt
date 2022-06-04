package com.example.svbookmarket.activities.common

import android.content.res.Resources

// common conversion
fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()