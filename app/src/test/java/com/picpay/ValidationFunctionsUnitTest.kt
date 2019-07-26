package com.picpay

import com.picpay.tools.currencyFormatFromScreen
import com.picpay.tools.currencyFormatToScreen
import com.picpay.tools.formatValueClean
import com.picpay.tools.timeStampToDateTime
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.NumberFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ValidationFunctionsUnitTest {


    @Test
    fun timeStampWithSplit_isCorrect() {

        val timestamp = 1563549107L

        assertEquals(
            "19/07/2019 às 12:11",
            timeStampToDateTime(timestamp, "às")
        )
    }

    @Test
    fun timeStampWithoutSplit_isCorrect() {

        val timestamp = 1563549107L

        assertEquals(
            "19/07/2019 12:11",
            timeStampToDateTime(timestamp)
        )
    }

    @Test
    fun formatValueClean_isCorrect() {
        val amount = "3.333,33"

        assertEquals(
            "3,333.33",
            formatValueClean(false, amount)
        )
    }

    @Test
    fun currencyFormatToScreen_isCorrect() {
        val crSymbol = NumberFormat.getCurrencyInstance().currency
        val amount = "3333.33"

        assertEquals(
            "$crSymbol 3,333.33",
            currencyFormatToScreen(amount, true)
        )
    }

    @Test
    fun currencyFormatFromScreen_isCorrect() {
        val crSymbol = NumberFormat.getCurrencyInstance().currency.symbol
        val amount = "$crSymbol 3,333.33"

        var res = currencyFormatFromScreen(amount)

        assertEquals(
            3333.33,
            currencyFormatFromScreen(amount),
            0.001
        )
    }
}
