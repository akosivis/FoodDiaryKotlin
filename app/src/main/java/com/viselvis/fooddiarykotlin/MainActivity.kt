package com.viselvis.fooddiarykotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.viselvis.fooddiarykotlin.databinding.ActivityMainBinding
import com.viselvis.fooddiarykotlin.fragments.AddFoodItemFragment
import com.viselvis.fooddiarykotlin.fragments.LeftFragment
import com.viselvis.fooddiarykotlin.fragments.MainFragment
import com.viselvis.fooddiarykotlin.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.left -> {
                    if (currentFragment != LeftFragment()) {
                        val fragment = LeftFragment()
                        commitFragment(fragment)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                R.id.add -> {
                    if (currentFragment != MainFragment()) {
                        val fragment = MainFragment()
                        commitFragment(fragment)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                R.id.settings -> {
                    if (currentFragment != SettingsFragment()) {
                        val fragment = SettingsFragment()
                        commitFragment(fragment)
                        return@OnNavigationItemSelectedListener true
                    }
                }
            }
            false
        }

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root

        mainBinding.bottomNavView.setOnNavigationItemSelectedListener(navItemSelectedListener)
        commitFragment(MainFragment())

        setContentView(view)
    }

    private fun commitFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.flt_main, fragment
        ).commit()

        currentFragment = MainFragment()
    }

}