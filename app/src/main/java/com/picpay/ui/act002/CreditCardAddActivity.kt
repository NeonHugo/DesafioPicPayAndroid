package com.picpay.ui.act002

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import com.picpay.R
import com.picpay.base.BaseActivity
import com.picpay.ui.act001.PeopleListActivity
import com.picpay.ui.act003.CreditCardFormActivity
import com.picpay.widget.setSafeNavigationOnClickListener
import com.picpay.widget.setSafeOnClickListener
import kotlinx.android.synthetic.main.creditcardadd_activity.*
import kotlinx.android.synthetic.main.creditcardadd_content.*

/*
* Created By neomatrix on 2019-07-22
*/
class CreditCardAddActivity : BaseActivity() {

    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creditcardadd_activity)

        setSupportActionBar(toolbar)

        initVars()
        initActions()
    }

    override fun initVars() {
        super.initVars()

        setHeader()
        recoverParameters()
    }

    override fun initActions() {
        super.initActions()

        creditcardadd_btn_new.setSafeOnClickListener {
            callCreditCardFormActivity(bundle)
        }
    }

    private fun setHeader() {
        title = ""

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val icon = resources.getDrawable(R.drawable.ic_keyboard_arrow_left_black_24dp, null)
        icon.setColorFilter(resources.getColor(R.color.colorPicPayGreen, null), PorterDuff.Mode.SRC_IN)
        toolbar?.navigationIcon = icon

        toolbar?.setSafeNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun recoverParameters() {
        bundle = intent.extras ?: Bundle()
    }

    override fun onBackPressed() {
        callPeopleListActivity(Bundle())
    }

    private fun callCreditCardFormActivity(bundle: Bundle) {
        val moduleIntent = CreditCardFormActivity.newIntent(context, bundle)
        startActivity(moduleIntent)

        finish()
    }

    private fun callPeopleListActivity(bundle: Bundle) {
        val moduleIntent = PeopleListActivity.newIntent(context, bundle)
        startActivity(moduleIntent)

        finish()
    }

    companion object {

        fun newIntent(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, CreditCardAddActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }
}