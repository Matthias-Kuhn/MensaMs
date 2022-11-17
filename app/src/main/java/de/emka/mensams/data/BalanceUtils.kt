package de.emka.mensams.data

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal


object BalanceUtils {
    fun getBalanceResponse(url: String): Call<BalanceResponse> {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(url)
            .build()
            .create(BalanceApi::class.java)
        return retrofitBuilder.getBalance()
    }

    /**
     * Convert the balance in cents to a formatted String
     */
    fun intToString(balance: Int): String {
        return BigDecimal(balance).movePointLeft(2).toString() + "€"
    }
}