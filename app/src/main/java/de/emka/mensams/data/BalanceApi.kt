package de.emka.mensams.data

import retrofit2.Call
import retrofit2.http.GET

interface BalanceApi {

    @GET("balance")
    fun getBalance(): Call<BalanceResponse>
}