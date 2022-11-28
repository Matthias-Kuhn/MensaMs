package de.emka.mensams.views

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import de.emka.mensams.R
import de.emka.mensams.data.BalanceUtils
import de.emka.mensams.data.UpdateWorker
import de.emka.mensams.viewmodels.SharedViewModel
import de.emka.mensams.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding : ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val aboutFragment = AboutFragment()
    private val optionsFragment = OptionsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initWorkManager()
        val viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        showNotification(123)
        replaceFragment(viewModel.currentFragment)
        binding.bottomNavigationView.selectedItemId = R.id.home


        binding.bottomNavigationView.setOnItemSelectedListener {
            Log.d("MainActivity", "navbar selection changed")
            when (it.itemId) {
                R.id.home -> viewModel.currentFragment = SharedViewModel.Fragments.HOME
                R.id.about -> viewModel.currentFragment = SharedViewModel.Fragments.ABOUT
                R.id.options -> viewModel.currentFragment = SharedViewModel.Fragments.OPTIONS

                else -> {}

            }
            replaceFragment(viewModel.currentFragment)
            true
        }
    }

    private fun replaceFragment(fragment: SharedViewModel.Fragments) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, getFragment(fragment))
        fragmentTransaction.commit()
    }

    fun getFragment(currentFragment: SharedViewModel.Fragments) : Fragment = when (currentFragment) {
        SharedViewModel.Fragments.HOME -> homeFragment
        SharedViewModel.Fragments.OPTIONS -> optionsFragment
        SharedViewModel.Fragments.ABOUT -> aboutFragment
    }
    
    private fun initWorkManager() {
        val request = PeriodicWorkRequestBuilder<UpdateWorker>(15, TimeUnit.MINUTES)
            .addTag(TAG)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TAG, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    fun showNotification(diff:Int) {
        createNotificationChannel()

        val notificationId = SimpleDateFormat("ddHHmmss", Locale.GERMANY).format(Date()).toInt()

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notificationTitle = "Zahlung mit der MensaCard"
        val contentText = "Du hast ${BalanceUtils.intToString(diff)} bezahlt."
        //val subtitleNotification = applicationContext.getString(R.string.notification_subtitle)


        val notificationBuilder = NotificationCompat.Builder(applicationContext,
            UpdateWorker.NOTIFICATION_CHANNEL
        )

        notificationBuilder
            .setContentTitle(notificationTitle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_notification)
        notificationBuilder.priority = NotificationCompat.PRIORITY_DEFAULT


        notificationManager.notify(notificationId, notificationBuilder.build())

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(UpdateWorker.NOTIFICATION_CHANNEL, UpdateWorker.NOTIFICATION_NAME, importance)
            notificationChannel.description = "test Beschreibung"
            val notificationManager =
                applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
