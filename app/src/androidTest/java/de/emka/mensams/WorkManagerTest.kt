package de.emka.mensams

import android.content.Context
import android.util.Log

import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.impl.utils.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import de.emka.mensams.data.UpdateWorker
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat

import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

class WorkManagerTest {

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
    }



    @Test
    @Throws(Exception::class)
    fun testPeriodicWork() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val request = PeriodicWorkRequestBuilder<UpdateWorker>(15, TimeUnit.MINUTES)
            .build()
        val workManager = WorkManager.getInstance(context)
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        workManager.enqueue(request).result.get()
        testDriver?.setPeriodDelayMet(request.id)

        val workInfo = workManager.getWorkInfoById(request.id).get()

        assertThat(workInfo.state, `is`(WorkInfo.State.ENQUEUED))

    }
}