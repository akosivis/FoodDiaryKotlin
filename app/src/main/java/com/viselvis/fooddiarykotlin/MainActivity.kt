package com.viselvis.fooddiarykotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.viselvis.fooddiarykotlin.activity.AddFoodItemActivity
import com.viselvis.fooddiarykotlin.databinding.ActivityMainBinding
import com.viselvis.fooddiarykotlin.fragments.MainFragment
import com.viselvis.fooddiarykotlin.fragments.SelectFoodTypeFragment
// import kotlinx.android.synthetic.main.main_activity_content.view.*

class MainActivity : AppCompatActivity(),
    MainFragment.MainFragmentListener,
    SelectFoodTypeFragment.SelectFoodTypeListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setSupportActionBar(mainBinding.mainContent.toolbar)

        val drawerLayout: DrawerLayout = mainBinding.drawerLayout
        val navView: NavigationView = mainBinding.navigation
        val navController = findNavController(R.id.nav_host_fragment_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.mainFragment, R.id.printDiaryFragment, R.id.settingsFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun navigateToSelectFoodTypes() {
        val navController = findNavController(R.id.nav_host_fragment_main)
        navController.navigate(R.id.selectFoodTypeFragment)
    }

    override fun navigateToAddFoodItemFragment(foodTypeId: Int?) {
        val intentToAddFoodItem = Intent(this, AddFoodItemActivity::class.java)
        intentToAddFoodItem.putExtra("foodType", foodTypeId)
        startActivity(intentToAddFoodItem)

//        = Intent(this, AddFoodItemActivity::class.java)
//        intentToAddFoodItem.
//        startActivity(intentToAddFoodItem)
    }


}