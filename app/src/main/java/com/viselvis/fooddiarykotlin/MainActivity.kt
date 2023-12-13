package com.viselvis.fooddiarykotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.viselvis.fooddiarykotlin.activity.AddFoodItemActivity
import com.viselvis.fooddiarykotlin.application.FoodItemListApplication
import com.viselvis.fooddiarykotlin.databinding.ActivityMainBinding
import com.viselvis.fooddiarykotlin.fragments.MainFragment
import com.viselvis.fooddiarykotlin.fragments.SelectFoodTypeFragment
import com.viselvis.fooddiarykotlin.ui.NoteEatApp
import com.viselvis.fooddiarykotlin.ui.theme.NoteEatTheme

// import kotlinx.android.synthetic.main.main_activity_content.view.*

class MainActivity : ComponentActivity(),
    MainFragment.MainFragmentListener,
    SelectFoodTypeFragment.SelectFoodTypeListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var mainBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val application: FoodItemListApplication = (application as FoodItemListApplication)
        setContent {
            NoteEatApp(app = application)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

//    override fun onSupportNavigateUp(): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_main)
//        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
//    }

    override fun navigateToSelectFoodTypes() {
        val navController = findNavController(R.id.nav_host_fragment_main)
        navController.navigate(R.id.selectFoodTypeFragment)
    }

    override fun navigateToFoodHistory() {
        val navController = findNavController(R.id.nav_host_fragment_main)
        navController.navigate(R.id.foodHistoryFragment)
    }

    override fun navigateToAddFoodItemFragment(foodTypeId: Int) {
//        val intentToAddFoodItem = Intent(this, AddFoodItemActivity::class.java)
//        intentToAddFoodItem.putExtra("foodType", foodTypeId)
//        startActivity(intentToAddFoodItem)
        val bundle: Bundle? = Bundle()
        bundle?.putInt("foodTypeId", foodTypeId)
        val navController = findNavController(R.id.nav_host_fragment_main)
        navController.navigate(R.id.addFoodItemFragment, bundle)
    }

    @Composable
    fun MainActivityView() {
        val drawerState  = rememberDrawerState(initialValue = DrawerValue.Open)

//        AndroidViewBinding(ActivityMainBinding::inflate) {
//            navController = findNavController(mainBinding.mainContent.mainFragmentContent.root.id)
//        }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { 
                ModalDrawerSheet {
                    Text("NoteEat")
                    Divider()
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = false,
                        onClick = { /*TODO*/ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Print") },
                        selected = false,
                        onClick = { /*TODO*/ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        onClick = { /*TODO*/ }
                    )
                }
            },
            gesturesEnabled = true
        ) {
            AndroidView(
                factory = { context ->
                    NavigationView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setupWithNavController(navController)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}