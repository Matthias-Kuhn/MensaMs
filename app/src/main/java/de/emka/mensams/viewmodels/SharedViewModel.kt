package de.emka.mensams.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import de.emka.mensams.data.BalanceResponse
import de.emka.mensams.data.BalanceUtils
import de.emka.mensams.data.ResponseType
import de.emka.mensams.data.StoringAndNotifyingUtils
import de.emka.mensams.views.AboutFragment
import de.emka.mensams.views.HomeFragment
import de.emka.mensams.views.OptionsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val sharedPreferences = application.getSharedPreferences("BALANCE_DATA", Context.MODE_PRIVATE)

    var currentFragment = Fragments.HOME
    var balanceString = "-"
    var balance = 0
    var cardNr: String = sharedPreferences.getString("CARD_NR", "").toString()


    private fun storeCardNr(cardNr: String) {
        val editor = sharedPreferences.edit()
        editor.putString("CARD_NR", cardNr)
        editor.apply()
    }

    fun setAndStoreCardNr(cardNr: String) {
        this.cardNr = cardNr
        storeCardNr(cardNr)
    }

    fun setAndStoreBalance(balance: Int, responseType: ResponseType) {
        this.balance = balance
        this.balanceString = BalanceUtils.intToString(balance)

        StoringAndNotifyingUtils.storeAndUpdateViews(
            getApplication<Application>().applicationContext,
            balance, responseType)
    }

    enum class Fragments {
        HOME, OPTIONS, ABOUT
    }
}