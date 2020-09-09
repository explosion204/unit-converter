package com.explosion204.unitconverter

import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, KeyboardFragmant.onNumButtonClickListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var data_fragment: DataFragment
    //private lateinit var converter: Converter
    private lateinit var categories: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRules()
        data_fragment = fr_data as DataFragment



//        data_fragment.arguments = Bundle().apply {
//            putString("converter", Converter())
//        }
        

        //val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }


        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this);

        val menuItem = findViewById<NavigationView>(R.id.nav_view).menu.getItem(0)
        onNavigationItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var adapter = when(item.itemId) {
            R.id.category_length -> {
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    (categories["Length"] as JSONObject).keys().iterator().asSequence().toList())
            }
            R.id.category_weight -> {
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    (categories["Weight"] as JSONObject).keys().iterator().asSequence().toList())
            }
            else -> {
                ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    (categories["Temperature"] as JSONObject).keys().iterator().asSequence().toList())
            }

        }

        data_fragment.initialUnit.adapter = adapter
        data_fragment.convertedUnit.adapter = adapter
        return true
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is KeyboardFragmant) {
            fragment.setNumpadClickListener(this)
        }

    }

    override fun onNumButtonClick(num: Int) {
        data_fragment.editInitialValue(num)
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