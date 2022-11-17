package de.emka.mensams.data

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.math.BigDecimal


object BalanceUtils {
    val BASE_URL = "https://api.topup.klarna.com/api/v1/STW_MUNSTER/cards/"

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

//    fun getBalanceString(cardNr: String) : String {
//        return try {
//            val balance = getBalance(cardNr)
//            intToString(balance)
//        } catch (e: IOException){
//            "Could not fetch data"
//        }
//    }

    @Throws(IOException::class)
    fun getBalanceAndExecute(cardNr: String, toExecute: (input: Int) -> Unit) {
        val url = "$BASE_URL$cardNr/"
        val retrofitData = BalanceUtils.getBalanceResponse(url)

        retrofitData.enqueue(object : Callback<BalanceResponse?> {

            override fun onResponse(
                call: Call<BalanceResponse?>,
                response: Response<BalanceResponse?>
            ) {
                val responseBody = response.body()

                if (responseBody != null) {
                    val balance = responseBody.balance
                    toExecute(balance)
                } else {
                    toExecute(-1)
                }
            }

            override fun onFailure(call: Call<BalanceResponse?>, t: Throwable) {
                toExecute(-100)
            }
        })

    }
}