package com.electrohub.schoolmanagementapp

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        findViewById<View>(R.id.main)?.let { applyBounceToAllClickables(it) }

        setupNavigation()
        setupScrollAnimations()
        loadUserProfile()

        findViewById<android.widget.Button>(R.id.logoutButton).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Handle back button to go to StudentDashboard
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToHome()
            }
        })
    }

    private fun navigateToHome() {
        val intent = Intent(this, StudentDashboard::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val nameText = findViewById<TextView>(R.id.profileName)
            val emailText = findViewById<TextView>(R.id.profileEmail)

            // Set email from Auth immediately
            emailText.text = currentUser.email

            // Fetch additional details from Firestore
            db.collection("users").document(currentUser.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val fullName = document.getString("fullName")
                        if (fullName != null) {
                            nameText.text = fullName
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to load profile details", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setupScrollAnimations() {
        val scrollView = findViewById<NestedScrollView>(R.id.profileScrollView)
        val contentLayout = findViewById<LinearLayout>(R.id.profileContentLayout)
        
        contentLayout?.layoutAnimation = AnimationUtils.loadLayoutAnimation(this, R.anim.layout_animation_slide_up)

        scrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, _, _, _ ->
            val centerY = v.height / 2
            for (i in 0 until (contentLayout?.childCount ?: 0)) {
                val child = contentLayout?.getChildAt(i) ?: continue
                val childRect = Rect()
                child.getGlobalVisibleRect(childRect)
                
                val viewCenterY = (childRect.top + childRect.bottom) / 2
                val distFromCenter = (centerY - viewCenterY).toFloat()
                val scale = 1f - (Math.abs(distFromCenter) / v.height) * 0.1f
                val rotation = (distFromCenter / v.height) * 5f
                
                if (childRect.intersect(Rect(0, 0, v.width, v.height))) {
                    child.scaleX = Math.max(0.9f, scale)
                    child.scaleY = Math.max(0.9f, scale)
                    child.rotationX = rotation
                }
            }
        })
    }

    private fun setupNavigation() {
        findViewById<LinearLayout>(R.id.navItemHome).setOnClickListener {
            navigateToHome()
        }
        findViewById<LinearLayout>(R.id.navItemSearch).setOnClickListener {
            startActivity(Intent(this, AcademicActivity::class.java))
            finish()
        }
        findViewById<LinearLayout>(R.id.navItemNotify).setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
            finish()
        }
        // Profile is current activity
    }
}