package com.example.stubprojectkotlin.ui.main_activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.stubprojectkotlin.BaseActivity
import com.example.stubprojectkotlin.R
import com.example.stubprojectkotlin.ui.login_activity.LoginActivity
import com.example.stubprojectkotlin.utils.LocationManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var drawerToggle: ActionBarDrawerToggle

    override val layoutId: Int
        get() = R.layout.activity_main
    override val tag: String?
        get() = MainActivity::class.simpleName

    private lateinit var locationManager: LocationManager

    override fun created(savedInstance: Bundle?) {

        init();

    }

    private fun init(){

        // Drawer Toggle
        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Nav Item Selected Listener
        nav.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.account -> activityNavigation(
                    this,
                    LoginActivity::class.java, false, emptyList(), emptyList()
                )
                R.id.update -> {
                    Toast.makeText(this, "Update", Toast.LENGTH_SHORT).show()
                }
                R.id.logout -> Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (drawerToggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }
}