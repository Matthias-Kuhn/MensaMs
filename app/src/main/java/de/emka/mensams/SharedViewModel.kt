package de.emka.mensams

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel



class SharedViewModel : ViewModel() {
    private val homeFragment = HomeFragment()
    private val aboutFragment = AboutFragment()
    private val optionsFragment = OptionsFragment()

    var currentFragment = Fragments.HOME

    fun getFragment() : Fragment = when (currentFragment) {
        Fragments.HOME -> homeFragment
        Fragments.OPTIONS -> optionsFragment
        Fragments.ABOUT -> aboutFragment
    }

    enum class Fragments {
        HOME, OPTIONS, ABOUT
    }
}