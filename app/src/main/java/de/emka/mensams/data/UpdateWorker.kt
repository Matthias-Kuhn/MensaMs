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

        return Result.success()
    }
}