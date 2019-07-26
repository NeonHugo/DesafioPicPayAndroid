package com.picpay.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.picpay.R
import dagger.android.support.DaggerAppCompatActivity

/*
* Created By neomatrix on 2019-07-19
*/
@SuppressLint("Registered")
open class BaseActivity : DaggerAppCompatActivity() {

    lateinit var context: Context
    var mLastClickTime: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    open fun initVars() {
        context = this@BaseActivity

        window.navigationBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }

    open fun initActions() {
    }

    open fun UIInteractStatus(status: Boolean){
    }


}