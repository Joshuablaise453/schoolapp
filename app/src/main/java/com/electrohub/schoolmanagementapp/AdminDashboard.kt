package com.electrohub.schoolmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class AdminDashboard : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var menuButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.nav_view)
        menuButton = findViewById(R.id.menuButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        findViewById<View>(R.id.main)?.let { applyBounceToAllClickables(it) }

        setupNavigation()
        setupBackPress()

        // Default fragment
        if (savedInstanceState == null) {
            switchToDashboard()
        }
    }

    private fun setupNavigation() {
        menuButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_dashboard -> switchToDashboard()
                R.id.nav_users -> switchToUsers()
                R.id.nav_academics -> switchToClasses()
                R.id.nav_settings -> switchToSettings()
                R.id.nav_logout -> logout()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Bottom Navigation Listeners
        findViewById<LinearLayout>(R.id.navDashboard).setOnClickListener { switchToDashboard() }
        findViewById<LinearLayout>(R.id.navUsers).setOnClickListener { switchToUsers() }
        findViewById<LinearLayout>(R.id.navClasses).setOnClickListener { switchToClasses() }
        findViewById<LinearLayout>(R.id.navSettings).setOnClickListener { switchToSettings() }
        
        findViewById<LinearLayout>(R.id.navStudents).setOnClickListener {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivity(intent)
        }
    }

    fun switchToDashboard() {
        updateBottomNav(0)
        replaceFragment(AdminDashboardFragment())
    }

    fun switchToUsers() {
        updateBottomNav(1)
        replaceFragment(UserManagementFragment())
    }

    fun switchToClasses() {
        updateBottomNav(3)
        replaceFragment(AdminClassesFragment())
    }

    fun switchToSettings() {
        updateBottomNav(4)
        replaceFragment(AdminSettingsFragment())
    }

    fun switchToAddUser() {
        replaceFragment(AddUserFragment(), addToBackStack = true)
    }

    fun switchToAddClass() {
        replaceFragment(AddClassFragment(), addToBackStack = true)
    }

    fun switchToEditClass(className: String, level: String, teacher: String) {
        replaceFragment(EditClassFragment.newInstance(className, level, teacher), addToBackStack = true)
    }

    fun updateTopBar(showBack: Boolean) {
        val searchButton = findViewById<View>(R.id.searchButton)
        if (showBack) {
            menuButton.setImageResource(R.drawable.ic_back_circle)
            menuButton.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            searchButton.visibility = View.GONE
        } else {
            menuButton.setImageResource(android.R.drawable.ic_menu_sort_by_size)
            menuButton.setOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            searchButton.visibility = View.VISIBLE
        }
    }

    private fun updateBottomNav(index: Int) {
        val primaryGreen = ContextCompat.getColor(this, R.color.primary_green)
        val mutedGrey = ContextCompat.getColor(this, R.color.secondary_text)

        findViewById<View>(R.id.indicatorDashboard).visibility = if (index == 0) View.VISIBLE else View.INVISIBLE
        findViewById<ImageView>(R.id.iconDashboard).setColorFilter(if (index == 0) primaryGreen else mutedGrey)
        findViewById<TextView>(R.id.textDashboard).setTextColor(if (index == 0) primaryGreen else mutedGrey)

        findViewById<View>(R.id.indicatorUsers).visibility = if (index == 1) View.VISIBLE else View.INVISIBLE
        findViewById<ImageView>(R.id.iconUsers).setColorFilter(if (index == 1) primaryGreen else mutedGrey)
        findViewById<TextView>(R.id.textUsers).setTextColor(if (index == 1) primaryGreen else mutedGrey)

        findViewById<View>(R.id.indicatorClasses).visibility = if (index == 3) View.VISIBLE else View.INVISIBLE
        findViewById<ImageView>(R.id.iconClasses).setColorFilter(if (index == 3) primaryGreen else mutedGrey)
        findViewById<TextView>(R.id.textClasses).setTextColor(if (index == 3) primaryGreen else mutedGrey)

        findViewById<View>(R.id.indicatorSettings).visibility = if (index == 4) View.VISIBLE else View.INVISIBLE
        findViewById<ImageView>(R.id.iconSettings).setColorFilter(if (index == 4) primaryGreen else mutedGrey)
        findViewById<TextView>(R.id.textSettings).setTextColor(if (index == 4) primaryGreen else mutedGrey)
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_enter,
                R.anim.fragment_exit
            )
            .replace(R.id.fragmentContainer, fragment)
        
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}