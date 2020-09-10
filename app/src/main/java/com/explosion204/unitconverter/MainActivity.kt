package com.explosion204.unitconverter

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
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
                     KeyboardFragment.OnNumButtonClickListener {

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
        var adapter = when(item.itemId) {
            R.id.category_length -> {
                dataFragment.converter = Converter(categories["Length"] as JSONObject)
                ArrayAdapter<String>(this, R.layout.spinner_unit_item,
                    (categories["Length"] as JSONObject).keys().iterator().asSequence().toList())
            }
            R.id.category_weight -> {
                dataFragment.converter = Converter(categories["Weight"] as JSONObject)
                ArrayAdapter<String>(this, R.layout.spinner_unit_item,
                    (categories["Weight"] as JSONObject).keys().iterator().asSequence().toList())
            }
            else -> {
                dataFragment.converter = Converter(categories["Temperature"] as JSONObject)
                ArrayAdapter<String>(this, R.layout.spinner_unit_item,
                    (categories["Temperature"] as JSONObject).keys().iterator().asSequence().toList())
            }

        }

        dataFragment.initialUnit.adapter = adapter
        dataFragment.convertedUnit.adapter = adapter

        drawerLayout.closeDrawer(Gravity.LEFT)

        return true
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is KeyboardFragment) {
            fragment.setNumpadClickListener(this)
        }
        if (fragment is DataFragment) {
            fragment.converter = Converter(categories["Length"] as JSONObject)
        }

    }

    override fun onNumButtonClick(num: Int) {
        dataFragment.editInitialValue(num)
    }

    fun onMenuButtonClick(view: View) {
        drawerLayout.openDrawer(Gravity.LEFT)
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