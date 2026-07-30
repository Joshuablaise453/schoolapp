package com.electrohub.schoolmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class StudentDashboard : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_dashboard)

        auth = FirebaseAuth.getInstance()
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view_student)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        findViewById<View>(R.id.main)?.let { applyBounceToAllClickables(it) }

        setupNavigation()
        setupBackPress()

        if (savedInstanceState == null) {
            switchToDashboard()
        }
    }

    private fun setupNavigation() {
        findViewById<ImageButton>(R.id.menuButton).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_student_dashboard -> switchToDashboard()
                R.id.nav_student_assignments -> switchToAssignments()
                R.id.nav_student_settings -> switchToProfile()
                R.id.nav_student_logout -> logout()
                else -> Toast.makeText(this, "Module under development", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Bottom Navigation
        findViewById<LinearLayout>(R.id.navDash).setOnClickListener { switchToDashboard() }
        findViewById<LinearLayout>(R.id.navProfile).setOnClickListener { switchToProfile() }
        
        // Placeholder for other nav items
        findViewById<LinearLayout>(R.id.navClasses).setOnClickListener { updateBottomNav(1) }
        findViewById<LinearLayout>(R.id.navAssignments).setOnClickListener { 
            switchToAssignments()
        }
        findViewById<LinearLayout>(R.id.navMessages).setOnClickListener { updateBottomNav(3) }
    }

    private fun switchToDashboard() {
        updateBottomNav(0)
        replaceFragment(StudentDashboardFragment())
    }

    private fun switchToAssignments() {
        updateBottomNav(2)
        replaceFragment(StudentAssignmentsFragment())
    }

    private fun switchToProfile() {
        updateBottomNav(4)
        replaceFragment(StudentProfileFragment())
    }

    private fun updateBottomNav(index: Int) {
        val primaryGreen = ContextCompat.getColor(this, R.color.primary_green)
        val mutedGrey = ContextCompat.getColor(this, R.color.secondary_text)

        // Dashboard
        setNavItemState(R.id.indicatorDash, R.id.iconDash, R.id.textDash, index == 0, primaryGreen, mutedGrey)
        // My Classes
        setNavItemState(R.id.indicatorClasses, R.id.iconClasses, R.id.textClasses, index == 1, primaryGreen, mutedGrey)
        // Assignments
        setNavItemState(R.id.indicatorAssignments, R.id.iconAssignments, R.id.textAssignments, index == 2, primaryGreen, mutedGrey)
        // Messages
        setNavItemState(R.id.indicatorMessages, R.id.iconMessages, R.id.textMessages, index == 3, primaryGreen, mutedGrey)
        // Profile
        setNavItemState(R.id.indicatorProfile, R.id.iconProfile, R.id.textProfile, index == 4, primaryGreen, mutedGrey)
    }

    private fun setNavItemState(indicatorId: Int, iconId: Int, textId: Int, isActive: Boolean, activeColor: Int, inactiveColor: Int) {
        findViewById<View>(indicatorId).visibility = if (isActive) View.VISIBLE else View.INVISIBLE
        findViewById<ImageView>(iconId).setColorFilter(if (isActive) activeColor else inactiveColor)
        findViewById<TextView>(textId).setTextColor(if (isActive) activeColor else inactiveColor)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_enter,
                R.anim.fragment_exit
            )
            .replace(R.id.studentFragmentContainer, fragment)
            .commit()
    }

    private fun setupBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
    
    fun logout() {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}