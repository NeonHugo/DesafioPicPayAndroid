package com.picpay.widget

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.view.View.OnTouchListener
import android.widget.EditText
import android.widget.LinearLayout
import com.picpay.R

class EditTextSearch : LinearLayout {

    var mHint: String = ""
    var mValue: String = ""
        set(value) {
            field = value
            //
            setControlValues()
        }

    var mEnabled: Boolean = true
        set(value) {
            field = value
            //
            cfgStatus(value)
        }

    private var mBlock_CFG_EDITTEXTSEARCH: Boolean = false

    @Transient
    private var ll_background: LinearLayout? = null

    @Transient
    private var et_value: EditText? = null


    constructor(context: Context?) : super(context) {
        initialize(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize(attrs, defStyleAttr)
    }

    private fun initialize(attrs: AttributeSet?, defStyleAttr: Int) {
        defaultValues()

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.EditTextSearch, 0, 0)

            mHint = typedArray.getString(R.styleable.EditTextSearch_mHint) ?: ""
            mValue = typedArray.getString(R.styleable.EditTextSearch_mValue) ?: ""
            mEnabled = typedArray.getBoolean(R.styleable.EditTextSearch_mEnabled, true)

            typedArray.recycle()
        }

        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.edittextsearch, this)

        ll_background = findViewById<LinearLayout>(R.id.edittextsearch_ll_background)
        et_value = findViewById(R.id.edittextsearch_et_value)

    }

    interface IEditTextSearchChangeText {
        fun reportTextChange(text: String)
    }

    private var delegateTextChange: IEditTextSearchChangeText? = null

    fun setOnReportTextChangeListener(delegateTextChange: IEditTextSearchChangeText) {
        this.delegateTextChange = delegateTextChange
    }

    private fun defaultValues() {
        mHint = ""
        mValue = ""
        mEnabled = true

    }

    private fun setControlValues() {
        if (!mBlock_CFG_EDITTEXTSEARCH) {
            et_value?.let { etValue ->
                etValue.hint = mHint
                etValue.setText(mValue)

                if (etValue.text.toString().isNotEmpty()) {
                    setRightIconVisibility(etValue, true)
                } else {
                    setRightIconVisibility(etValue, false)
                }

                etValue.setOnTouchListener(OnTouchListener { view, motionEvent ->
                    val DRAWABLE_LEFT = 0
                    val DRAWABLE_TOP = 1
                    val DRAWABLE_RIGHT = 2
                    val DRAWABLE_BOTTOM = 3

                    try {
                        if (motionEvent.action == MotionEvent.ACTION_UP) {
                            if (motionEvent.rawX >= etValue.right - etValue.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {

                                setRightIconVisibility(etValue, false)
                                etValue.setText("")

                                return@OnTouchListener true
                            }
                        }

                        return@OnTouchListener false


                    } catch (e: Exception) {
                        return@OnTouchListener false
                    }
                })

                etValue.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                    }

                    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                        if (charSequence.isNotEmpty()) {
                            setRightIconVisibility(etValue, true)
                        } else {
                            setRightIconVisibility(etValue, false)
                        }
                    }

                    override fun afterTextChanged(editable: Editable) {
                        if (delegateTextChange != null) {
                            mBlock_CFG_EDITTEXTSEARCH = true
                            mValue = editable.toString()
                            mBlock_CFG_EDITTEXTSEARCH = false
                            delegateTextChange!!.reportTextChange(mValue)
                        }
                    }
                })

                etValue.customSelectionActionModeCallback = object : ActionMode.Callback {
                    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                        return false
                    }

                    override fun onDestroyActionMode(mode: ActionMode) {}

                    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                        return false
                    }

                    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                        return false
                    }
                }

                etValue.isLongClickable = false
            }
        }
    }

    private fun setRightIconVisibility(et_search: EditText, bStatus: Boolean) {
        if (bStatus) {
            val x = resources.getDrawable(R.drawable.ic_backspace_24dp)

            x.setBounds(0, 0, x.intrinsicWidth, x.intrinsicHeight)

            et_search.setCompoundDrawables(et_search.compoundDrawables[0], null, x, null)

        } else {
            et_search.setCompoundDrawables(et_search.compoundDrawables[0], null, null, null)
        }
    }

    private fun cfgStatus(enabled: Boolean) {
        et_value?.let { etValue ->
            etValue.isEnabled = enabled
            //
            if (!mEnabled) {
                etValue.setTextColor(0x1000000)
            }
        }
    }

    override fun onAttachedToWindow() {
        if (!isInEditMode) {
            setControlValues()
        }
        //
        super.onAttachedToWindow()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        //
        ss.mHint = mHint
        ss.mValue = mValue
        ss.mEnabled = mEnabled
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        //
        mHint = ss.mHint
        mValue = ss.mValue
        mEnabled = ss.mEnabled
    }

    /**
     * ***************************************************************
     * Saved State inner static class
     * ***************************************************************
     */
    private class SavedState : View.BaseSavedState {
        var mHint = ""
        var mValue = ""
        var mEnabled = true

        constructor(superState: Parcelable) : super(superState)

        private constructor(source: Parcel) : super(source) {
            mHint = source.readString()!!
            mValue = source.readString()!!
            mEnabled = source.readInt() == 1
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)

            out.writeString(mHint)
            out.writeString(mValue)
            out.writeInt(if (mEnabled) 1 else 0)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {

            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }

        }

        override fun describeContents(): Int {
            return 0
        }
    }
}
