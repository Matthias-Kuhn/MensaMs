package de.emka.mensams.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.emka.mensams.R
import de.emka.mensams.viewmodels.SharedViewModel
import de.emka.mensams.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val homeFragment = HomeFragment()
    private val aboutFragment = AboutFragment()
    private val optionsFragment = OptionsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}
