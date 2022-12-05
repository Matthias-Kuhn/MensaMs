package de.emka.mensams.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UpdateWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    val TAG = "UpdateWorker"

    override fun doWork(): Result {
        Log.d(TAG, "doWork called")
        val prefs = applicationContext.getSharedPreferences("BALANCE_DATA", Context.MODE_PRIVATE)
        val cardNr = prefs.getString("CARD_NR", "").toString()
        BalanceUtils.getBalanceAndExecute(cardNr, ::storeAndUpdateViews)
        return Result.success()
    }

    fun storeAndUpdateViews(balance: Int, responseType: ResponseType){
        StoringAndNotifyingUtils.storeAndUpdateViews(applicationContext, balance, responseType)
    }

}