package com.picpay.ui.act001

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.picpay.R
import com.picpay.adapter.AdapterPeople
import com.picpay.base.BaseActivity
import com.picpay.entity.People
import com.picpay.factory.ViewModelFactory
import com.picpay.tools.UIStatusType
import com.picpay.ui.act002.CreditCardAddActivity
import com.picpay.ui.act004.TransferActivity
import com.picpay.widget.EditTextSearch
import kotlinx.android.synthetic.main.peoplelist_activity.*
import kotlinx.android.synthetic.main.peoplelist_content.*
import javax.inject.Inject


class PeopleListActivity : BaseActivity() {

    private lateinit var mAdapterPeople: AdapterPeople

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mPeopleListActivityViewModel: PeopleListActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.peoplelist_activity)
        setSupportActionBar(toolbar)

        initVars()
        initActions()
    }

    override fun initVars() {
        super.initVars()

        title = ""

        people_list_rv_list.layoutManager = LinearLayoutManager(context)

        mPeopleListActivityViewModel = ViewModelProviders.of(this, mViewModelFactory)
            .get(PeopleListActivityViewModel::class.java)

        mPeopleListActivityViewModel.response.observe(this, Observer {

            when (it) {
                is PeopleOutResponse.Peoples -> processPeople(it.peoples)
                is PeopleOutResponse.NavigationCreditCardAdd -> processNavigationCreditCardAdd(it.bundle)
                is PeopleOutResponse.NavigationTransfer -> processNavigationTransfer(it.bundle)
            }
        })

        uiStatus(UIStatusType.PROGRESS)
        mPeopleListActivityViewModel.peopleListUseCase()
    }

    override fun initActions() {
        super.initActions()

        people_list_sv_search.setOnReportTextChangeListener(object : EditTextSearch.IEditTextSearchChangeText {
            override fun reportTextChange(text: String) {
                mAdapterPeople.filter.filter(text)
            }
        })
    }

    private fun processPeople(peoples: ArrayList<People>) {
        mAdapterPeople =
            AdapterPeople(context, com.picpay.R.layout.peoplelist_adapter_cell, peoples, Glide.with(this))

        people_list_rv_list.adapter = mAdapterPeople

        mAdapterPeople.setOnItemClickListener(object : AdapterPeople.ItemClickListener {
            override fun onItemClick(position: Int, item: People) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                    return
                }
                mLastClickTime = SystemClock.elapsedRealtime()

                mPeopleListActivityViewModel.peopleSeletectedUseCase(item)
            }
        })

        uiStatus(UIStatusType.LIST)
    }

    private fun processNavigationCreditCardAdd(bundle: Bundle) {
        callCreditCardAddActivity(bundle)
    }

    private fun processNavigationTransfer(bundle: Bundle) {
        callTransferActivity(bundle)
    }


    private fun uiStatus(status: UIStatusType) {
        when (status) {
            UIStatusType.PROGRESS -> {
                people_list_pb_status.visibility = View.VISIBLE
                people_list_rv_list.visibility = View.GONE
            }

            UIStatusType.LIST -> {
                people_list_pb_status.visibility = View.GONE
                people_list_rv_list.visibility = View.VISIBLE
            }
        }
    }

    override fun UIInteractStatus(status: Boolean) {
        people_list_rv_list.isEnabled = status
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun callCreditCardAddActivity(bundle: Bundle) {
        val moduleIntent = CreditCardAddActivity.newIntent(context, bundle)
        startActivity(moduleIntent)

        finish()
    }

    private fun callTransferActivity(bundle: Bundle) {
        val moduleIntent = TransferActivity.newIntent(context, bundle)
        startActivity(moduleIntent)

        finish()
    }

    companion object {
        fun newIntent(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, PeopleListActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

}
