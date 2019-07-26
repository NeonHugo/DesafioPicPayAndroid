package com.picpay.widget

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.picpay.R
import com.picpay.tools.*

/*
* Created By neomatrix on 2019-07-23
*/
class TextInputEditTextCustom : TextInputEditText {

    var mRequired: Boolean = false
    var mFieldType: FieldType = FieldType.CHAR
        set(value) {
            field = value
            cfgType()
        }
    var mInputType: Int = InputType.TYPE_CLASS_TEXT
    var mMask: String = FORMAT_EMPTY
    var mErrorMSG: String = FORMAT_EMPTY

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
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, com.picpay.R.styleable.TextInputEditTextCustom, 0, 0)

            mRequired = typedArray.getBoolean(
                R.styleable.TextInputEditTextCustom_mRequired, false
            )
            mFieldType = FieldType.values()[typedArray.getInt(
                R.styleable.TextInputEditTextCustom_mFieldType, Companion.FieldType.CHAR.ordinal
            )]
            mInputType = typedArray.getInt(
                R.styleable.TextInputEditTextCustom_mInputType, InputType.TYPE_CLASS_TEXT
            )
            mMask = typedArray.getString(
                R.styleable.TextInputEditTextCustom_mMask
            ) ?: FORMAT_EMPTY
            mErrorMSG = typedArray.getString(
                R.styleable.TextInputEditTextCustom_mErrorMSG
            ) ?: FORMAT_EMPTY

            typedArray.recycle()
        }

        addTextChangedListener(txtWatcher)

        cfgType()
    }

    private val txtWatcher = object : TextWatcher {
        var old = ""

        override fun afterTextChanged(editable: Editable?) {
            removeTextChangedListener(this)

            val result = if (mMask.isNotEmpty()) {
                sMask(unMask(editable.toString()), unMask(old))
            } else {
                editable.toString()
            }

            if (mMask.isNotEmpty()) {
                setText(result)
                setSelection(result.length)
            }

            addTextChangedListener(this)
        }

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            old = charSequence.toString()
        }

        override fun onTextChanged(editable: CharSequence?, start: Int, before: Int, count: Int) {

        }
    }

    private fun cfgType() {
        when (mFieldType) {
            FieldType.CHAR -> {
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
            }
            FieldType.CHAR_NUMBER -> {
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.CPF -> {
                mMask = FORMAT_CPF
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.CNPJ -> {
                mMask = FORMAT_CNPJ
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.PHONE -> {
                mMask = FORMAT_PHONE
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.CEP -> {
                mMask = FORMAT_CEP
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.DATE -> {
                mMask = FORMAT_DATE
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.EXPIRATION_DATE -> {
                mMask = FORMAT_EXPIRATION_DATE
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.HOUR -> {
                mMask = FORMAT_HOUR
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            FieldType.CREDIT_CARD -> {
                mMask = FORMAT_CREDIT_CARD
                inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }

    private fun sMask(str: String, old: String): String {
        var result = ""

        if (str.trim().isNotEmpty()) {
            var i = 0

            for (m in mMask.toCharArray()) {
                if (m != '#' && m != '$' && str.length >= old.length) {
                    result += m
                    continue
                }
                try {
                    result += str[i]
                } catch (e: Exception) {
                    break
                }

                i++
            }
        }

        return result
    }

    private fun unMask(value: String): String {
        return value
            .replace("[.]".toRegex(), "")
            .replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "")
            .replace("[(]".toRegex(), "")
            .replace("[)]".toRegex(), "")
            .replace("[:]".toRegex(), "")
            .replace("[ ]".toRegex(), "")
    }

    fun isValid(): Boolean {
        mErrorMSG = ""

        if (!mRequired && text.toString().trim().isEmpty()) {
            return true
        }

        if (mRequired && text.toString().trim().isEmpty()) {
            mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_no_value)

            return false
        }

        when (mFieldType) {
            FieldType.CPF -> {
                if (!isValidCPF(text.toString())) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_cpf)

                    return false
                }
            }
            FieldType.CNPJ -> {
                if (!isValidCNPJ(text.toString())) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_cnpj)

                    return false
                }
            }
            FieldType.PHONE -> {
                if (!isValidPhone(text.toString())) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_phone)

                    return false
                }
            }
            FieldType.CEP -> {
                if (!isValidCep(text.toString())) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_cep)

                    return false
                }
            }
            FieldType.DATE -> {
                if (!isValidDate(text.toString(), "yyyy-MM-dd")) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_date)

                    return false
                }
            }
            FieldType.EXPIRATION_DATE -> {
                if (!isValidExpirationDate(text.toString())) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_expiration_date)

                    return false
                }
            }
            FieldType.HOUR -> {
                if (!isValidHour24(text.toString())) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_hour)

                    return false
                }
            }
            FieldType.CREDIT_CARD -> {
                if (!isValidCreditCard(text.toString())) {
                    mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_credit_card)

                    return false
                }
            }
            else -> {
                return true
            }
        }

        if (mMask.isNotEmpty()) {
            if (!isValidMask(mMask, text.toString())) {
                mErrorMSG = context.getString(R.string.text_input_edit_text_custom_error_invalid_mask)

                return false
            }
        }

        return true
    }

    companion object {
        const val FORMAT_EMPTY = ""
        const val FORMAT_CPF = "###.###.###-##"
        const val FORMAT_CNPJ = "##.###.###/####-##"
        const val FORMAT_PHONE = "(##)#####-#####"
        const val FORMAT_CEP = "#####-###"
        const val FORMAT_DATE = "##/##/####"
        const val FORMAT_EXPIRATION_DATE = "##/##"
        const val FORMAT_HOUR = "##:##"
        const val FORMAT_CREDIT_CARD = "#### #### #### ####"

        enum class FieldType {
            CHAR,
            CHAR_NUMBER,
            CPF,
            CNPJ,
            PHONE,
            CEP,
            DATE,
            EXPIRATION_DATE,
            HOUR,
            CREDIT_CARD
        }
    }

    override fun onAttachedToWindow() {
        if (!isInEditMode) {
            cfgType()
        }

        super.onAttachedToWindow()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState!!)
        ss.mRequired = mRequired
        ss.mFieldType = mFieldType
        ss.mInputType = mInputType
        ss.mMask = mMask
        ss.mErrorMSG = mErrorMSG
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        //
        mRequired = ss.mRequired
        mFieldType = ss.mFieldType
        mInputType = ss.mInputType
        mMask = ss.mMask
        mErrorMSG = ss.mErrorMSG
    }

    /**
     * ***************************************************************
     * Saved State inner static class
     * ***************************************************************
     */
    private class SavedState : BaseSavedState {
        var mRequired = false
        var mFieldType = FieldType.CHAR
        var mInputType = InputType.TYPE_CLASS_TEXT
        var mMask: String = FORMAT_EMPTY
        var mErrorMSG: String = FORMAT_EMPTY

        constructor(superState: Parcelable) : super(superState)

        private constructor(source: Parcel) : super(source) {
            mRequired = source.readInt() == 1
            mFieldType = FieldType.values()[source.readInt()]
            mInputType = source.readInt()
            mMask = source.readString()!!
            mErrorMSG = source.readString()!!
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)

            out.writeInt(if (mRequired) 1 else 0)
            out.writeInt(mFieldType.ordinal)
            out.writeInt(mInputType)
            out.writeString(mMask)
            out.writeString(mErrorMSG)
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