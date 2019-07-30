package com.picpay.ui.act004

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.picpay.R
import com.picpay.application.Constants
import com.picpay.base.BaseActivity
import com.picpay.entity.CreditCard
import com.picpay.entity.People
import com.picpay.factory.ViewModelFactory
import com.picpay.model.TransactionEnv
import com.picpay.model.TransactionReponse
import com.picpay.tools.creditCardLastNumbers
import com.picpay.tools.currencyFormatFromScreen
import com.picpay.tools.currencyFormatToScreen
import com.picpay.tools.unMask
import com.picpay.ui.act001.PeopleListActivity
import com.picpay.ui.act003.CreditCardFormActivity
import com.picpay.widget.setSafeNavigationOnClickListener
import com.picpay.widget.setSafeOnClickListener
import kotlinx.android.synthetic.main.transfer_activity.*
import kotlinx.android.synthetic.main.transfer_content.*
import java.text.NumberFormat
import javax.inject.Inject


/*
* Created By neomatrix on 2019-07-25
*/
class TransferActivity : BaseActivity() {

    private lateinit var bundle: Bundle
    private lateinit var mPeople: People
    private lateinit var mCreditCard: CreditCard
    private lateinit var mGlide: RequestManager

    private var people_id = -1L

    @Inject
    lateinit var mViewModelFactory: ViewModelFactory

    private lateinit var mTransferActivityViewModel: TransferActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transfer_activity)
        setSupportActionBar(toolbar)

        initVars()
        initActions()
    }

    override fun initVars() {
        super.initVars()

        mGlide = Glide.with(this)
        transfer_tv_currency.text = NumberFormat.getCurrencyInstance().currency.symbol
        transfer_et_value.setText(
            currencyFormatToScreen(
                "0.0"
            )
        )

        setHeader()
        recoverParameters()

        processFormValidation()

        mTransferActivityViewModel = ViewModelProviders.of(this, mViewModelFactory)
            .get(TransferActivityViewModel::class.java)

        mTransferActivityViewModel.response.observe(this, Observer {
            when (it) {
                is TransferOutResponse.PeopleGet -> processPeopleGet(it.people)
                is TransferOutResponse.CreditCardGet -> processCreditCardGet(it.creditCard)
                is TransferOutResponse.TransferSend -> processTransferSend(it.transaction)
                is TransferOutResponse.Failure -> processFailure(it.errorSendResouce)
            }
        })

        mTransferActivityViewModel.getPeopleUseCase(people_id)
        mTransferActivityViewModel.getCreditCardUseCase()
    }

    override fun initActions() {
        super.initActions()

        transfer_et_value.addTextChangedListener(object : TextWatcher {
            var current = ""

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != current) {
                    transfer_et_value.removeTextChangedListener(this)

                    val parsed = currencyFormatFromScreen(s.toString())

                    current = currencyFormatToScreen(parsed.toString())

                    transfer_et_value.setText(current)
                    transfer_et_value.setSelection(current.length)

                    transfer_et_value.addTextChangedListener(this)
                }

                processFormValidation()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        transfer_tv_edit.setSafeOnClickListener {
            bundle.putString(Constants.CREDIT_CARD_NUMBER, mCreditCard.creditCardNumber)
            callCreditCardFormActivity(bundle)
        }

        transfer_btn_pay.setSafeOnClickListener {
            val env = TransactionEnv()
            env.card_number = unMask(mCreditCard.creditCardNumber)
            env.cvv = mCreditCard.cvv
            env.expiry_date = mCreditCard.expirationDate
            env.destination_user_id = mPeople.id
            env.value = currencyFormatFromScreen(
                transfer_et_value.text.toString()
            )

            mTransferActivityViewModel.saveCreditCardUseCase(
                env
            )
        }
    }

    private fun processFormValidation() {
        var value = currencyFormatFromScreen(
            transfer_et_value.text.toString()
        )

        if (value > 0) {
            transfer_btn_pay.isEnabled = true
            transfer_tv_currency.setTextColor(getColor(R.color.colorPicPayGreen))
            transfer_et_value.setTextColor(getColor(R.color.colorPicPayGreen))
        } else {
            transfer_btn_pay.isEnabled = false
            transfer_tv_currency.setTextColor(getColor(R.color.colorPicPayGrayLight))
            transfer_et_value.setTextColor(getColor(R.color.colorPicPayGrayLight))

        }
    }

    private fun processPeopleGet(people: People?) {
        people?.let {

            mGlide.load(it.img)
                .placeholder(R.drawable.ic_person_black_24dp)
                .into(transfer_iv_photo)

            mPeople = people
            transfer_tv_name.text = it.username
        }
    }

    private fun processCreditCardGet(creditCard: CreditCard?) {
        creditCard?.let {
            mCreditCard = creditCard
            transfer_tv_cn.text = getString(
                R.string.transfer_mastercard, creditCardLastNumbers(it.creditCardNumber)
            )
        }
    }

    private fun processTransferSend(transaction: TransactionReponse?) {
        val resultsFrame = TransactionFragment()
        val resultsBundle = Bundle()

        if (transaction?.success!!) {

            transaction.creditCardNumber = mCreditCard.creditCardNumber

            resultsBundle.putSerializable("key", transaction)

            resultsFrame.setOnNavFragListener(object : TransactionFragment.NavFragListener {
                override fun onCloseFragment() {
                    callPeopleListActivity(Bundle())
                }
            })

            resultsFrame.arguments = resultsBundle
            resultsFrame.show(supportFragmentManager, "transaction_dialog")
        } else {
            showResults(R.string.transfer_error_send_title, R.string.transfer_error_send_refused)
        }
    }

    private fun processFailure(errorSendResouce: Int) {
        showResults(R.string.transfer_error_send_title, errorSendResouce)
    }

    override fun onResume() {
        super.onResume()

        transfer_et_value.requestFocus()
        window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
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
            val intent = Intent(context, TransferActivity::class.java)
            intent.putExtras(bundle)

            return intent
        }
    }

}