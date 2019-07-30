package com.picpay.ui.act003

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import com.picpay.R
import com.picpay.application.Constants
import com.picpay.base.BaseActivity
import com.picpay.entity.CreditCard
import com.picpay.factory.ViewModelFactory
import com.picpay.ui.act001.PeopleListActivity
import com.picpay.ui.act004.TransferActivity
import com.picpay.widget.TextInputEditTextCustom
import com.picpay.widget.setSafeNavigationOnClickListener
import com.picpay.widget.setSafeOnClickListener
import kotlinx.android.synthetic.main.creditcardform_activity.*
import kotlinx.android.synthetic.main.creditcardform_content.*
import javax.inject.Inject


/*
* Created By neomatrix on 2019-07-24
*/
class CreditCardFormActivity : BaseActivity() {

    private lateinit var bundle: Bundle

    private var people_id = -1L
    private var credit_card_number = ""

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mCreditCardFormActivityViewModel: CreditCardFormActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.creditcardform_activity)
        setSupportActionBar(toolbar)

        initVars()
        initActions()
    }

    override fun initVars() {
        super.initVars()

        setHeader()
        recoverParameters()

        mCreditCardFormActivityViewModel = ViewModelProviders.of(this, mViewModelFactory)
            .get(CreditCardFormActivityViewModel::class.java)

        mCreditCardFormActivityViewModel.response.observe(this, Observer {

            when (it) {
                is CreditCardFormOutResponse.CreditCardSave -> processCreditCardSave(it.creditCard)
                is CreditCardFormOutResponse.CreditCardGet -> processCreditCardGet(it.creditCard)
                is CreditCardFormOutResponse.Failure -> processFailure(it.errorSaveResouce)
                is CreditCardFormOutResponse.NavigationTransfer -> processNavigationTransfer(it.bundle)
            }
        })

        if (credit_card_number.isNotEmpty()) {
            mCreditCardFormActivityViewModel.getCreditCardUseCase(credit_card_number)
        }
    }

    override fun initActions() {
        super.initActions()

        creditcardform_btn_save.setSafeOnClickListener {
            processFormValidation()
        }
    }

    private fun processFormValidation() {
        val widgetsLayOut = arrayListOf<TextInputLayout>()
        val widgets = arrayListOf<TextInputEditTextCustom>()

        widgetsLayOut.add(creditcardform_ti_nc)
        widgetsLayOut.add(creditcardform_ti_name)
        widgetsLayOut.add(creditcardform_ti_expiration_date)
        widgetsLayOut.add(creditcardform_ti_cvv)

        widgets.add(creditcardform_et_nc)
        widgets.add(creditcardform_et_name)
        widgets.add(creditcardform_et_expiration_date)
        widgets.add(creditcardform_et_cvv)

        var totalError = 0

        for (i in 0 until widgets.size) {
            if (widgets[i].isValid()) {
                widgetsLayOut[i].error = ""
            } else {
                totalError += 1
                widgetsLayOut[i].error = widgets[i].mErrorMSG
            }
        }

        if (totalError == 0) {

            val creditCard = CreditCard()
            creditCard.creditCardNumber = creditcardform_et_nc.text.toString()
            creditCard.name = creditcardform_et_name.text.toString()
            creditCard.expirationDate = creditcardform_et_expiration_date.text.toString()
            creditCard.cvv = creditcardform_et_cvv.text.toString()

            mCreditCardFormActivityViewModel.saveCreditCardUseCase(creditCard)
        }
    }

    private fun processCreditCardSave(creditCard: CreditCard) {
        mCreditCardFormActivityViewModel.creditCardSelectedUseCase(people_id)
    }

    private fun processCreditCardGet(creditCard: CreditCard?) {
        creditCard?.let {
            creditcardform_et_nc.setText(it.creditCardNumber)
            creditcardform_et_name.setText(it.name)
            creditcardform_et_expiration_date.setText(it.expirationDate)
            creditcardform_et_cvv.setText(it.cvv)
        }
    }

    private fun processFailure(errorSaveResouce: Int) {
        showResults(R.string.creditcardaform_error_save_title, errorSaveResouce)
    }

    private fun processNavigationTransfer(bundle: Bundle) {
        callTransferActivity(bundle)
    }

    private fun showResults(errorTitleResource: Int, errorMessageResouce: Int) {
        var mDialog: Dialog
        mDialog = Dialog(this)

        val inflater = LayoutInflater.from(context) as LayoutInflater
        val mView = inflater.inflate(R.layout.baseinfra_custom_dialog, null)

        var ivTitleIcon = mView.findViewById<ImageView>(R.id.baseinfra_custom_dialog_iv_title_icon)
        var tvTitleTxt = mView.findViewById<TextView>(R.id.baseinfra_custom_dialog_tv_title_txt)
        var pbStatus = mView.findViewById<ProgressBar>(R.id.baseinfra_custom_dialog_pb_status)
        var tvStatusTxt = mView.findViewById<TextView>(R.id.baseinfra_custom_dialog_tv_status_txt)
        var btnPositive = mView.findViewById<Button>(R.id.baseinfra_custom_dialog_btn_positive)

        pbStatus.visibility = View.GONE

        tvTitleTxt.text = getString(errorTitleResource)
        tvStatusTxt.text = getString(errorMessageResouce)

        mDialog.setContentView(mView)
        mDialog.setCancelable(false)
        mDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnPositive.setOnClickListener {
            mDialog.dismiss()
        }

        mDialog.show()
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

        people_id = bundle.getLong(Constants.PEOPLE_ID)
        credit_card_number = bundle.getString(Constants.CREDIT_CARD_NUMBER) ?: ""
    }

    override fun onBackPressed() {
        callPeopleListActivity(Bundle())
    }

    private fun callTransferActivity(bundle: Bundle) {
        val moduleIntent = TransferActivity.newIntent(context, bundle)
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
            val intent = Intent(context, CreditCardFormActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

}