package com.picpay.ui.act004

import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.picpay.R
import com.picpay.model.TransactionReponse
import com.picpay.tools.creditCardLastNumbers
import com.picpay.tools.currencyFormatToScreen
import com.picpay.tools.timeStampToDateTime

/*
* Created By neomatrix on 2019-07-26
*/
class TransactionFragment : BottomSheetDialogFragment() {

    private lateinit var mBehavior: BottomSheetBehavior<View>
    private lateinit var mGlide: RequestManager

    private lateinit var transaction: TransactionReponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGlide = Glide.with(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        if (bundle != null) {
            transaction = bundle.getSerializable("key") as TransactionReponse
        }

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, com.picpay.R.layout.bottom_sheet_layout, null)

        val linearLayout = view.findViewById(R.id.transactionfragment_ll_root) as LinearLayout
        val params = linearLayout.layoutParams as LinearLayout.LayoutParams
        params.height = getScreenHeight()
        linearLayout.layoutParams = params

        val ivPhoto = view.findViewById<ImageView>(R.id.transactionfragment_iv_photo)
        mGlide.load(transaction.destination_user.img)
            .placeholder(R.drawable.ic_person_black_24dp)
            .into(ivPhoto)

        val tvName = view.findViewById<TextView>(R.id.transactionfragment_tv_name)
        tvName.text = transaction.destination_user.username

        val tvTime = view.findViewById<TextView>(R.id.transactionfragment_tv_time)
        tvTime.text = timeStampToDateTime(transaction.timestamp, "às")

        val tvCode = view.findViewById<TextView>(R.id.transactionfragment_tv_code)
        tvCode.text = getString(
            R.string.transactionfragment_transaction,
            transaction.id.toString()
        )

        val tvCn = view.findViewById<TextView>(R.id.transactionfragment_tv_cn)
        tvCn.text = getString(
            R.string.transactionfragment_mastercard,
            creditCardLastNumbers(transaction.creditCardNumber)
        )

        val tvValue = view.findViewById<TextView>(R.id.transactionfragment_tv_value)
        tvValue.text = currencyFormatToScreen(transaction.value.toString(), true)

        val tvValueTotal = view.findViewById<TextView>(R.id.transactionfragment_tv_total_value)
        tvValueTotal.text = currencyFormatToScreen(transaction.value.toString(), true)

        dialog.setContentView(view)
        mBehavior = BottomSheetBehavior.from(view.parent as View)
        return dialog
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    override fun onStart() {
        super.onStart()
        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroy() {
        super.onDestroy()

        navFragListener?.onCloseFragment()
    }

    /**
     * Interface nav
     */
    interface NavFragListener {

        fun onCloseFragment()
    }

    private var navFragListener: NavFragListener? = null

    fun setOnNavFragListener(navFragListener: NavFragListener) {
        this.navFragListener = navFragListener
    }

}