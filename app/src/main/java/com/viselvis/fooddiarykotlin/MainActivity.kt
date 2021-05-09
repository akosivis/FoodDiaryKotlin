package com.viselvis.fooddiarykotlin

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.viselvis.fooddiarykotlin.databinding.ActivityMainBinding
import com.viselvis.fooddiarykotlin.fragments.AddFoodItemFragment
import com.viselvis.fooddiarykotlin.fragments.LeftFragment
import com.viselvis.fooddiarykotlin.fragments.MainFragment
import com.viselvis.fooddiarykotlin.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var currentFragment: Fragment
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navItemSelectedListener = NavigationView.OnNavigationItemSelectedListener { menuItem ->
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
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root

        mainBinding.imHamburger.setOnClickListener {
            mainBinding.drawerLayout.openDrawer(GravityCompat.START)
        }

        toggle = ActionBarDrawerToggle(this, mainBinding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mainBinding.drawerLayout.addDrawerListener(toggle)
        mainBinding.navigation.setNavigationItemSelectedListener(navItemSelectedListener)
        commitFragment(MainFragment())

        setContentView(view)
    }

    private fun commitFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.flt_main, fragment
        ).commit()

        currentFragment = MainFragment()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (mainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mainBinding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}