package com.syimicode.nuvastock.utils

import android.content.Context
import android.widget.Toast

fun Context.showMsg(
    msg: String,
    duration: Int = Toast.LENGTH_LONG
) = Toast.makeText(this, msg, duration).show()