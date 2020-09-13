package com.explosion204.unitconverter

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter


class MainActivity : AppCompatActivity(),
                     NavigationView.OnNavigationItemSelectedListener,
                     KeyboardFragment.OnNumButtonClickListener
{
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var dataFragment: DataFragment
    private lateinit var categories: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRules()
        setContentView(R.layout.activity_main)

        dataFragment = fr_data as DataFragment
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this);

        var menuItem = findViewById<NavigationView>(R.id.nav_view).menu.getItem(0).subMenu.getItem(0)
        navView.setCheckedItem(menuItem)
        onNavigationItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        onNavigationItemSelected(menu.findItem(R.id.category_length))
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.category_length -> {
                dataFragment.modifyConverter(categories["Length"] as JSONObject)
            }
            R.id.category_weight -> {
                dataFragment.modifyConverter(categories["Weight"] as JSONObject)
            }
            else -> {
                dataFragment.modifyConverter(categories["Volume"] as JSONObject)
            }

        }

        drawerLayout.closeDrawer(Gravity.LEFT)
        return true
    }



    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is KeyboardFragment) {
            fragment.setNumpadClickListener(this)
        }
    }

    override fun onNumButtonClick(num: Int) {
        dataFragment.editInitialValue(num)
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

    private fun initRules() {
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
        categories = JSONObject(jsonString)
    }
}