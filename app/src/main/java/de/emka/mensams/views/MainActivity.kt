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
import de.emka.mensams.data.StoringAndNotifyingUtils
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
        val request = PeriodicWorkRequestBuilder<UpdateWorker>(25, TimeUnit.MINUTES)
            .addTag(TAG)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TAG, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    private fun showNotification(balance: Int, balanceOld: Int) {
        StoringAndNotifyingUtils.showNotification(applicationContext, balance, balanceOld)

    }

}
