package com.picpay

import com.picpay.tools.*
import org.junit.Assert.*
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

        assertEquals(
            3333.33,
            currencyFormatFromScreen(amount),
            0.001
        )
    }

    @Test
    fun unmask_isCorrect() {
        val value = "R$ 120.000,00-Hugo 12:00 ( -- )   hugo//();"

        assertEquals(
            "R$12000000Hugo1200hugo",
            unMask(value)

        )
    }

    @Test
    fun isValidCreditCard_isCorrect() {
        val creditCardNumber = "1111 2222 3333 4444"

        assertTrue(
            isValidCreditCard(creditCardNumber)
        )
    }

    @Test
    fun isValidCreditCard_isWrong() {
        val creditCardNumber = "11112222333344445"

        assertFalse(
            isValidCreditCard(creditCardNumber)
        )
    }

    @Test
    fun isValidExpirationDate_isCorrect() {
        val expirationDate = "12/56"

        assertTrue(
            isValidExpirationDate(expirationDate)
        )
    }

    @Test
    fun isValidExpirationDate_isWrong() {
        val expirationDate = "12/18"

        assertFalse(
            isValidExpirationDate(expirationDate)
        )
    }

    @Test
    fun creditCardLastNumbers_isCorrect() {
        val creditCardNumber = "1111 2222 3333 4444"

        assertEquals(
            "4444",
            creditCardLastNumbers(creditCardNumber)
        )
    }

    @Test
    fun creditCardLastNumbers_isWrong() {
        val creditCardNumber = "111122223333444"

        assertEquals(
            "",
            creditCardLastNumbers(creditCardNumber)
        )
    }


}
