package com.picpay.ui.act000

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.picpay.base.BaseActivity
import com.picpay.factory.ViewModelFactory
import com.picpay.tools.UIStatusType
import com.picpay.ui.act001.PeopleListActivity
import kotlinx.android.synthetic.main.splash_activity.*
import kotlinx.android.synthetic.main.splash_content.*
import javax.inject.Inject

/*
* Created By neomatrix on 2019-07-22
*/
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mSplashActivityViewModel: SplashActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.picpay.R.layout.splash_activity)
        setSupportActionBar(toolbar)

        initVars()
        initActions()
    }

    override fun initVars() {
        super.initVars()

        title = ""

        mSplashActivityViewModel = ViewModelProviders.of(this, mViewModelFactory)
            .get(SplashActivityViewModel::class.java)

        mSplashActivityViewModel.response.observe(this, Observer {
            when (it) {
                is SplashOutResponse.Navigation -> processNavigation(it.bundle)
                is SplashOutResponse.Failure -> processFailure()
            }
        })

        startSync()
    }

    override fun initActions() {
        super.initActions()

        splash_iv_error.setOnClickListener {
            startSync()
        }
    }

    private fun startSync() {
        uiStatus(UIStatusType.PROGRESS)
        mSplashActivityViewModel.peopleListUseCase()
    }

    private fun uiStatus(status: UIStatusType) {
        when (status) {
            UIStatusType.PROGRESS -> {
                splash_pb_status.visibility = View.VISIBLE
                splash_iv_error.visibility = View.GONE
            }

            UIStatusType.ERROR -> {
                splash_pb_status.visibility = View.GONE
                splash_iv_error.visibility = View.VISIBLE
            }
        }
    }

    private fun processNavigation(bundle: Bundle) {
        callPeopleListActivity(bundle)
    }

    private fun processFailure() {
        uiStatus(UIStatusType.ERROR)
    }

    private fun callPeopleListActivity(bundle: Bundle) {
        val moduleIntent = PeopleListActivity.newIntent(context, bundle)
        startActivity(moduleIntent)

        finish()
    }

}