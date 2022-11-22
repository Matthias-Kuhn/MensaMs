package de.emka.mensams.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import de.emka.mensams.R
import de.emka.mensams.data.UpdateWorker
import de.emka.mensams.viewmodels.SharedViewModel
import de.emka.mensams.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWorkManager()
        val viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        replaceFragment(viewModel.getFragment())
        binding.bottomNavigationView.selectedItemId = R.id.home


        binding.bottomNavigationView.setOnItemSelectedListener {
            Log.d("MainActivity", "navbar selection changed")
            when (it.itemId) {
                R.id.home -> viewModel.currentFragment = SharedViewModel.Fragments.HOME
                R.id.about -> viewModel.currentFragment = SharedViewModel.Fragments.ABOUT
                R.id.options -> viewModel.currentFragment = SharedViewModel.Fragments.OPTIONS

                else -> {}

            }
            replaceFragment(viewModel.getFragment())
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun initWorkManager() {
        val request = PeriodicWorkRequestBuilder<UpdateWorker>(15, TimeUnit.MINUTES)
            .addTag(TAG)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TAG, ExistingPeriodicWorkPolicy.KEEP, request)
    }
}
