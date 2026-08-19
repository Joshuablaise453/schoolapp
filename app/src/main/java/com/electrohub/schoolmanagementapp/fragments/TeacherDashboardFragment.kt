package com.electrohub.schoolmanagementapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.electrohub.schoolmanagementapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TeacherDashboardFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teacher_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        setupQuickActions(view)
        setupAssignments(view)
        setupSubmissions(view)
        setupSchedule(view)
        fetchTeacherData(view)

        // Stat cards navigation
        view.findViewById<LinearLayout>(R.id.teacherDashboardContent)?.let { content ->
            val statsGrid = content.getChildAt(1) as? GridLayout
            statsGrid?.let { grid ->
                // Assignments card (index 2)
                grid.getChildAt(2).setOnClickListener {
                    (activity as? com.electrohub.schoolmanagementapp.TeacherDashboard)?.switchToAssignments()
                }
                // Quizzes card (index 3)
                grid.getChildAt(3).setOnClickListener {
                    (activity as? com.electrohub.schoolmanagementapp.TeacherDashboard)?.switchToQuizzes()
                }
            }
        }

        // View all assignments
        view.findViewById<View>(R.id.teacherDashboardContent)?.let { content ->
            // Find "Recent Assignments" title row's "View all"
            // It's the 3rd child (index 2) LinearLayout -> 1st child (index 0) CardView -> LinearLayout -> RelativeLayout -> 2nd child (index 1) TextView
            try {
                val row3 = (content as LinearLayout).getChildAt(2) as LinearLayout
                val card1 = row3.getChildAt(0) as androidx.cardview.widget.CardView
                val innerLayout = card1.getChildAt(0) as LinearLayout
                val header = innerLayout.getChildAt(0) as android.widget.RelativeLayout
                val viewAll = header.getChildAt(1) as TextView
                viewAll.setOnClickListener {
                    (activity as? com.electrohub.schoolmanagementapp.TeacherDashboard)?.switchToAssignments()
                }

                // Create Assignment button in the same card
                val createBtn = innerLayout.getChildAt(2) as android.widget.Button
                createBtn.setOnClickListener {
                    (activity as? com.electrohub.schoolmanagementapp.TeacherDashboard)?.switchToCreateAssignment()
                }
            } catch (e: Exception) {}
        }
        
        // Add animation
        val content = view.findViewById<LinearLayout>(R.id.teacherDashboardContent)
        content?.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_up)
    }

    private fun fetchTeacherData(view: View) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val name = document.getString("fullName") ?: "Teacher"
                    view.findViewById<TextView>(R.id.teacherName).text = "Mr. $name"
                    // Role or subject could be set here if available in Firestore
                }
            }
    }

    private fun setupQuickActions(view: View) {
        val actionsLayout = view.findViewById<LinearLayout>(R.id.quickActionsLayout)

        actionsLayout?.let { layout ->
            val actions = listOf(
                Pair("Create\nAssignment", R.drawable.ic_edit),
                Pair("Create\nQuiz", R.drawable.ic_help),
                Pair("Take\nAttendance", R.drawable.ic_attendance),
                Pair("Send\nAnnouncement", android.R.drawable.ic_dialog_info),
                Pair("View\nReports", R.drawable.ic_merit)
            )
            
            val colors = listOf("#086B38", "#4285F4", "#FBBC05", "#9333EA", "#00ACC1")

            for (i in 0 until layout.childCount) {
                val child = layout.getChildAt(i)
                if (i < actions.size) {
                    child.findViewById<TextView>(R.id.actionText).text = actions[i].first
                    val icon = child.findViewById<ImageView>(R.id.actionIcon)
                    icon.setImageResource(actions[i].second)
                    icon.setColorFilter(android.graphics.Color.parseColor(colors[i]))
                    icon.setBackgroundResource(R.drawable.nav_circle_bg)
                    icon.backgroundTintList = ContextCompat.getColorStateList(requireContext(), android.R.color.transparent)
                    icon.background.setTint(android.graphics.Color.parseColor(colors[i].replace("#", "#1A")))
                    
                    if (i == 0) { // Create Assignment
                        child.setOnClickListener {
                            (activity as? com.electrohub.schoolmanagementapp.TeacherDashboard)?.switchToCreateAssignment()
                        }
                    }
                    if (i == 1) { // Create Quiz
                        child.setOnClickListener {
                            (activity as? com.electrohub.schoolmanagementapp.TeacherDashboard)?.switchToCreateQuiz()
                        }
                    }
                }
            }
        }
    }

    private fun setupAssignments(view: View) {
        val a1 = view.findViewById<View>(R.id.assignment1)
        a1.findViewById<TextView>(R.id.title).text = "Counting Numbers"
        a1.findViewById<TextView>(R.id.subtitle).text = "Baby Class - Numeracy"
        a1.findViewById<TextView>(R.id.dueDate).text = "22 May"

        val a2 = view.findViewById<View>(R.id.assignment2)
        a2.findViewById<TextView>(R.id.title).text = "Letter Sounds"
        a2.findViewById<TextView>(R.id.subtitle).text = "P1 Red - English"
        a2.findViewById<TextView>(R.id.dueDate).text = "23 May"

        val a3 = view.findViewById<View>(R.id.assignment3)
        a3.findViewById<TextView>(R.id.title).text = "Drawing Shapes"
        a3.findViewById<TextView>(R.id.subtitle).text = "Middle Class - Art"
        a3.findViewById<TextView>(R.id.dueDate).text = "24 May"
    }

    private fun setupSubmissions(view: View) {
        val s1 = view.findViewById<View>(R.id.submission1)
        s1.findViewById<TextView>(R.id.classTitle).text = "P1 Red - English"
        s1.findViewById<TextView>(R.id.countBadge).text = "24"

        val s2 = view.findViewById<View>(R.id.submission2)
        s2.findViewById<TextView>(R.id.classTitle).text = "Baby Class - Numeracy"
        s2.findViewById<TextView>(R.id.countBadge).text = "15"

        val s3 = view.findViewById<View>(R.id.submission3)
        s3.findViewById<TextView>(R.id.classTitle).text = "Middle Class - Art"
        s3.findViewById<TextView>(R.id.countBadge).text = "10"
    }

    private fun setupSchedule(view: View) {
        val sc1 = view.findViewById<View>(R.id.schedule1)
        sc1.findViewById<TextView>(R.id.timeRange).text = "08:30 AM - 09:30 AM"
        sc1.findViewById<TextView>(R.id.className).text = "Baby Class Blue"
        sc1.findViewById<TextView>(R.id.topicName).text = "Topic: Story Telling"
        sc1.findViewById<TextView>(R.id.roomName).text = "Room A1"
        
        val sc2 = view.findViewById<View>(R.id.schedule2)
        sc2.findViewById<TextView>(R.id.timeRange).text = "10:00 AM - 11:00 AM"
        sc2.findViewById<TextView>(R.id.className).text = "P1 Red"
        sc2.findViewById<TextView>(R.id.topicName).text = "Topic: Phonics"
        sc2.findViewById<TextView>(R.id.roomName).text = "Room B3"

        val sc3 = view.findViewById<View>(R.id.schedule3)
        sc3.findViewById<TextView>(R.id.timeRange).text = "11:30 AM - 12:30 PM"
        sc3.findViewById<TextView>(R.id.className).text = "Middle Class"
        sc3.findViewById<TextView>(R.id.topicName).text = "Topic: Outdoor Play"
        sc3.findViewById<TextView>(R.id.roomName).text = "Field 1"
    }
}