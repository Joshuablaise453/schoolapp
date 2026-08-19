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

class TeacherDashboard : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private var userData: Map<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teacher_dashboard)

        auth = FirebaseAuth.getInstance()
        drawerLayout = findViewById(R.id.drawer_layout_teacher)
        navigationView = findViewById(R.id.nav_view_teacher)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        findViewById<View>(R.id.main)?.let { applyBounceToAllClickables(it) }

        setupNavigation()
        setupBackPress()
        fetchUserData()

        if (savedInstanceState == null) {
            switchToDashboard()
        }
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid ?: return
        val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    userData = document.data
                    updateHeader(document.getString("fullName"))
                }
            }
    }

    private fun updateHeader(name: String?) {
        val headerView = navigationView.getHeaderView(0)
        val nameTextView = headerView.findViewById<TextView>(R.id.drawerTeacherName)
        nameTextView.text = name ?: "Teacher"
    }

    private fun setupNavigation() {
        findViewById<ImageButton>(R.id.menuButton).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_teacher_dashboard -> switchToDashboard()
                R.id.nav_teacher_logout -> logout()
                else -> Toast.makeText(this, "Module under development", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Bottom Navigation
        findViewById<LinearLayout>(R.id.navDash).setOnClickListener { switchToDashboard() }
        findViewById<LinearLayout>(R.id.navClasses).setOnClickListener { updateBottomNav(1) }
        findViewById<LinearLayout>(R.id.navStudents).setOnClickListener { updateBottomNav(2) }
        findViewById<LinearLayout>(R.id.navAssignments).setOnClickListener { switchToAssignments() }
        findViewById<LinearLayout>(R.id.navQuizzes).setOnClickListener { switchToQuizzes() }
    }

    fun switchToDashboard() {
        updateBottomNav(0)
        replaceFragment(com.electrohub.schoolmanagementapp.fragments.TeacherDashboardFragment())
    }

    fun switchToAssignments() {
        updateBottomNav(3)
        replaceFragment(com.electrohub.schoolmanagementapp.fragments.TeacherAssignmentsFragment())
    }

    fun switchToQuizzes() {
        updateBottomNav(4)
        replaceFragment(com.electrohub.schoolmanagementapp.fragments.TeacherQuizzesFragment())
    }

    fun switchToCreateAssignment() {
        replaceFragment(com.electrohub.schoolmanagementapp.fragments.CreateAssignmentFragment(), addToBackStack = true)
    }

    fun switchToCreateQuiz() {
        replaceFragment(com.electrohub.schoolmanagementapp.fragments.AddQuizFragment(), addToBackStack = true)
    }

    private fun updateBottomNav(index: Int) {
        val primaryGreen = ContextCompat.getColor(this, R.color.primary_green)
        val mutedGrey = ContextCompat.getColor(this, R.color.secondary_text)

        setNavItemState(R.id.indicatorDash, R.id.iconDash, R.id.textDash, index == 0, primaryGreen, mutedGrey)
        setNavItemState(R.id.indicatorClasses, R.id.iconClasses, R.id.textClasses, index == 1, primaryGreen, mutedGrey)
        setNavItemState(R.id.indicatorStudents, R.id.iconStudents, R.id.textStudents, index == 2, primaryGreen, mutedGrey)
        setNavItemState(R.id.indicatorAssignments, R.id.iconAssignments, R.id.textAssignments, index == 3, primaryGreen, mutedGrey)
        setNavItemState(R.id.indicatorQuizzes, R.id.iconQuizzes, R.id.textQuizzes, index == 4, primaryGreen, mutedGrey)
    }

    private fun setNavItemState(indicatorId: Int, iconId: Int, textId: Int, isActive: Boolean, activeColor: Int, inactiveColor: Int) {
        findViewById<View>(indicatorId).visibility = if (isActive) View.VISIBLE else View.INVISIBLE
        findViewById<ImageView>(iconId).setColorFilter(if (isActive) activeColor else inactiveColor)
        findViewById<TextView>(textId).setTextColor(if (isActive) activeColor else inactiveColor)
    }

    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fragment_enter,
                R.anim.fragment_exit,
                R.anim.fragment_enter,
                R.anim.fragment_exit
            )
            .replace(R.id.teacherFragmentContainer, fragment)
        
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
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