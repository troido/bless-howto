package com.troido.bless.app.common.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(msg:String){
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show()
}