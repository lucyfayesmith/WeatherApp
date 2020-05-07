package com.example.weatherapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.db.entity.Location
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_navigation_main.*


class NavigationMainActivity : AppCompatActivity() {

    private lateinit var locationViewModel: LocationViewModel
    private val newLocationActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_main)

        setSupportActionBar(findViewById(R.id.toolbar))

        val navView = findViewById<View>(R.id.navigation_view) as NavigationView

        createMenu(navView)

        val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
        drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.groupId) {
                R.id.misc -> {
                    if (it.title == "Add Location") {
                        val intent = Intent(this@NavigationMainActivity, NewLocationActivity::class.java)
                        startActivityForResult(intent, newLocationActivityRequestCode)
                    }
                }
                R.id.locations -> {
                    Toast.makeText(this, it.title, Toast.LENGTH_SHORT).show()
                }
            }
            drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    //Helps with handling any action implemented in the toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun createMenu(navView: NavigationView) {

        locationViewModel = ViewModelProvider(this).get(LocationViewModel::class.java)

        locationViewModel.allLocations.observe(this, Observer { locations ->
            locations?.let {
                val menu = navView.menu
                menu.clear()
                navView.invalidate()

                for(i in 0 until it.size) {
                        val item: MenuItem = menu.add(R.id.locations, i, 0, it.get(i).location)
                        item.setIcon(R.mipmap.ic_launcher)
                        item.isCheckable = true
                }

                val item: MenuItem = menu.add(R.id.misc, 0, 1, "Add Location")
                item.setIcon(R.drawable.ic_add)
                item.isCheckable = true
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newLocationActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewLocationActivity.EXTRA_REPLY)?.let {
                val location = Location(it)
                locationViewModel.insert(location)
            }
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show()
        }
    }

}
