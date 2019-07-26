package com.picpay.tools

import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/*
* Created By neomatrix on 2019-07-23
*/
fun isValidCPF(xCPF: String): Boolean {
    var cpf = xCPF
    cpf = unmask(cpf)
    return if (
        cpf != "00000000000" &&
        cpf != "11111111111" &&
        cpf != "22222222222" &&
        cpf != "33333333333" &&
        cpf != "44444444444" &&
        cpf != "55555555555" &&
        cpf != "66666666666" &&
        cpf != "77777777777" &&
        cpf != "88888888888" &&
        cpf != "99999999999" &&
        cpf.length == 11
    ) {

        try {
            var sm = 0
            var peso = 10

            var i = 0
            var num: Int
            while (i < 9) {
                num = cpf[i].toInt() - 48
                sm += num * peso
                --peso
                ++i
            }

            var r = 11 - sm % 11
            val dig10: Char
            dig10 = if (r != 10 && r != 11) {
                (r + 48).toChar()
            } else {
                48.toChar()
            }

            sm = 0
            peso = 11

            i = 0
            while (i < 10) {
                num = cpf[i].toInt() - 48
                sm += num * peso
                --peso
                ++i
            }

            r = 11 - sm % 11
            val dig11: Char
            dig11 = if (r != 10 && r != 11) {
                (r + 48).toChar()
            } else {
                48.toChar()
            }

            dig10 == cpf[9] && dig11 == cpf[10]
        } catch (var9: Exception) {
            false
        }

    } else {
        false
    }
}

fun isValidCNPJ(xCNPJ: String): Boolean {
    var cnpj = xCNPJ
    cnpj = unmask(cnpj)
    return if (
        cnpj != "00000000000000" &&
        cnpj != "11111111111111" &&
        cnpj != "22222222222222" &&
        cnpj != "33333333333333" &&
        cnpj != "44444444444444" &&
        cnpj != "55555555555555" &&
        cnpj != "66666666666666" &&
        cnpj != "77777777777777" &&
        cnpj != "88888888888888" &&
        cnpj != "99999999999999" &&
        cnpj.length == 14
    ) {

        try {
            var sm = 0
            var peso = 2

            var i = 11
            var num: Int
            while (i >= 0) {
                num = cnpj[i].toInt() - 48
                sm += num * peso
                ++peso
                if (peso == 10) {
                    peso = 2
                }
                --i
            }

            var r = sm % 11
            val dig13: Char
            dig13 = if (r != 0 && r != 1) {
                (11 - r + 48).toChar()
            } else {
                48.toChar()
            }

            sm = 0
            peso = 2

            i = 12
            while (i >= 0) {
                num = cnpj[i].toInt() - 48
                sm += num * peso
                ++peso
                if (peso == 10) {
                    peso = 2
                }
                --i
            }

            r = sm % 11
            val dig14: Char
            dig14 = if (r != 0 && r != 1) {
                (11 - r + 48).toChar()
            } else {
                48.toChar()
            }

            dig13 == cnpj[12] && dig14 == cnpj[13]
        } catch (var9: Exception) {
            false
        }

    } else {
        false
    }
}

fun isValidDate(sData: String, sDF: String): Boolean {

    return try {
        val df = SimpleDateFormat(sDF)
        df.isLenient = false
        df.parse(sData)

        true

    } catch (e: ParseException) {
        false
    }
}

fun isValidHour24(s_hour: String): Boolean {
    val pattern = Pattern.compile("([01]?[0-9]|2[0-3]):[0-5][0-9]")
    val matcher = pattern.matcher(s_hour)

    return matcher.matches()
}

fun isValidEmailAddress(emailAddress: String): Boolean {
    val expression =
        "[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"
    val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(emailAddress)
    return matcher.matches()
}

fun isValidCep(CEP: String): Boolean {

    val sCep = unmask(CEP)

    try {
        if (sCep.length != 8) {
            return false
        }

        Integer.parseInt(sCep.replaceFirst("^0+(?!$)".toRegex(), ""))

        return true

    } catch (e: Exception) {
        return false
    }
}

fun isValidPhone(phone: String): Boolean {
    val sPhone = unmask(phone).replace(" ", "")

    try {

        if (sPhone.length != 10 && sPhone.length != 11) {
            return false
        }

        val tAux = sPhone.replaceFirst("^0+(?!$)".toRegex(), "")

        tAux.toDouble()

        return true

    } catch (e: Exception) {

        return false
    }

}

fun isValidNumber(value: String): Boolean {
    val tValue = value.toUpperCase().replace("[^0123456789]".toRegex(), "_")

    val word = Pattern.compile("_", Pattern.CASE_INSENSITIVE)
    val match = word.matcher(tValue)

    return !match.find()
}

fun isValidCreditCard(value: String): Boolean {
    val creditCard = unmask(value)

    if (creditCard.length != 16) {
        return false
    }

    return isValidNumber(creditCard)
}

fun isValidExpirationDate(expirationDate: String): Boolean {

    val parts = expirationDate.split("/")

    try {

        if (parts.size != 2) {
            return false
        }

        val today = Calendar.getInstance()

        val month = today.get(Calendar.MONTH)
        val year = today.get(Calendar.YEAR) % 100

        val pMonth = Integer.parseInt(parts[0].replaceFirst("^0+(?!$)".toRegex(), ""))
        val pYear = Integer.parseInt(parts[1].replaceFirst("^0+(?!$)".toRegex(), ""))


        if (pYear < year) {
            return false
        }

        if (pMonth <= 0 || pMonth > 12) {
            return false
        }

        if (pMonth < month) {
            return false
        }

        return true

    } catch (e: Exception) {
        return false
    }
}


fun unmask(value: String): String {
    return value
        .replace("[.]".toRegex(), "")
        .replace("[-]".toRegex(), "")
        .replace("[/]".toRegex(), "")
        .replace("[(]".toRegex(), "")
        .replace("[)]".toRegex(), "")
        .replace("[:]".toRegex(), "")
        .replace("[ ]".toRegex(), "")
}

fun isValidMask(mask: String, str: String): Boolean {
    if (mask.length != str.length) {
        return false
    }

    return true
}

fun creditCardLastNumbers(creditCardNumber: String): String {
    val parts = creditCardNumber.split(" ")

    return try {
        parts[3]
    } catch (ex: Exception) {
        ""
    }
}

fun formatValue(typedBackSpace: Boolean, txt: String): String {
    var mTxt = txt
    mTxt = mTxt.replace(",", "")
    mTxt = mTxt.replace(".", "")

    if (typedBackSpace) {
        if (mTxt.length < 0) {
            mTxt.subSequence(0, mTxt.length - 1)
        }
    }
    val df = DecimalFormat("#,##0.00")
    var aux = 0.00
    var msg = "0.00"

    if (mTxt.length == 1) {
        msg = "0.0$mTxt"
    }
    if (mTxt.length == 2) {
        msg = "0.$mTxt"
    }
    if (mTxt.length > 2) {
        msg = mTxt.substring(0, mTxt.length - 2) + "." + mTxt.substring(mTxt.length - 2)
    }

    aux = java.lang.Double.parseDouble(msg)
    msg = df.format(aux)
    return msg
}

fun currencyFormatToScreen(amount: String, includeSymbol: Boolean = false): String {
    val formatter = DecimalFormat("###,###,##0.00")
    var crSymbol = NumberFormat.getCurrencyInstance().currency.symbol

    return if (includeSymbol) {
        "$crSymbol ${formatter.format(java.lang.Double.parseDouble(amount))}"
    } else {
        formatter.format(java.lang.Double.parseDouble(amount))
    }
}

fun currencyFormatFromScreen(amount: String): Double {
    var crSymbol = NumberFormat.getCurrencyInstance().currency.symbol

    var mTxt = amount
    mTxt = mTxt.replace(",", "")
    mTxt = mTxt.replace(".", "")
    mTxt = mTxt.replace(" ", "")
    mTxt = mTxt.replace(crSymbol, "")

    var msg = "0.00"

    if (mTxt.length == 1) {
        msg = "0.0$mTxt"
    }
    if (mTxt.length == 2) {
        msg = "0.$mTxt"
    }
    if (mTxt.length > 2) {
        msg = mTxt.substring(0, mTxt.length - 2) + "." + mTxt.substring(mTxt.length - 2)
    }

    return msg.toDouble()
}

fun subTxT(txt: String): String {
    if (txt.isNotEmpty()) {
        return txt.substring(0, txt.length - 1)
    }

    return ""
}

fun timeStampToDateTime(
    timestamp: Long = -1L,
    splitString: String = "",
    pattern: String = "dd/MM/yyyy HH:mm"
): String? {
    return try {
        val sdf = SimpleDateFormat(pattern)
        val netDate = Date(timestamp * 1000)

        var parts = sdf.format(netDate).split(" ")

        if (splitString.isEmpty()) {
            "${parts[0]} ${parts[1]}"
        } else {
            "${parts[0]} $splitString ${parts[1]}"
        }
    } catch (e: Exception) {
        e.toString()
    }
}