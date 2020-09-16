package com.explosion204.unitconverter

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter


class MainActivity : AppCompatActivity(),
                     NavigationView.OnNavigationItemSelectedListener
{
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var dataViewModel: DataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataViewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)

        if (dataViewModel.initRequired) {
            dataViewModel.setCategories(getRules())
        }

        setContentView(R.layout.activity_main)

        //dataFragment = fr_data as DataFragment
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this)

        // set selected item in navigation view
        var menuItems = findViewById<NavigationView>(R.id.nav_view).menu.getItem(0).subMenu
        if (dataViewModel.currentCategoryName.isEmpty()) {
            var menuItem = menuItems.getItem(0)
            navView.setCheckedItem(menuItem)
            onNavigationItemSelected(menuItem)
        }
        else {
            for (menuItem in menuItems) {
                if (menuItem.title.toString() == dataViewModel.currentCategoryName) {
                    navView.setCheckedItem(menuItem)
                    onNavigationItemSelected(menuItem)
                    break
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.category_length -> {
                dataViewModel.setCurrentCategory("Length")
            }
            R.id.category_weight -> {
                dataViewModel.setCurrentCategory("Weight")
            }
            else -> {
                dataViewModel.setCurrentCategory("Volume")
            }

        }

        drawerLayout.closeDrawer(Gravity.LEFT)
        return true
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is KeyboardFragment) {
            //fragment.setNumpadClickListener(this)
            fragment.setNumpadClickListener(dataViewModel)
        }
    }


    fun onMenuButtonClick(view: View) {
        drawerLayout.openDrawer(Gravity.LEFT)
    }

    fun onRotateButtonClick(view: View) {
        requestedOrientation = when(requestedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            else -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }

    private fun getRules(): JSONObject {
        var stream = resources.openRawResource(R.raw.rules)
        var writer = StringWriter()
        var buffer = CharArray(1024)

        try {
            var reader = BufferedReader(InputStreamReader(stream, "UTF-8"))
            var n: Int

            do {
                n = reader.read(buffer)
                if (n != -1)
                    writer.write(buffer, 0, n)
            }
            while (n != -1)
        }
        finally {
            stream.close()
        }

        var jsonString = writer.toString()
        return JSONObject(jsonString)
    }
}