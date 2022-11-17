package de.emka.mensams.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import de.emka.mensams.views.AboutFragment
import de.emka.mensams.views.HomeFragment
import de.emka.mensams.views.OptionsFragment


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