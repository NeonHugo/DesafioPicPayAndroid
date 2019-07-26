package com.picpay.base

import dagger.android.support.DaggerFragment

open class BaseFragment : DaggerFragment() {

    var mLastClickTime: Long = -1L

}