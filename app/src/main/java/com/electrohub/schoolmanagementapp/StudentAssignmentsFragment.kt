package com.electrohub.schoolmanagementapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class StudentAssignmentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_assignments, container, false)

        setupPendingAssignments(view.findViewById(R.id.pendingContainer))
        setupSubmittedAssignments(view.findViewById(R.id.submittedContainer))

        return view
    }

    private fun setupPendingAssignments(container: LinearLayout) {
        val items = listOf(
            AssignmentData("Mathematics - Worksheet 4", "Due: Tomorrow, 11:59 PM", "Solve questions 1-20 in your Mathematics workbook.", "Pending", R.drawable.ic_math, "#DCF2E6", "#086B38"),
            AssignmentData("Science - Plants and Animals", "Due: 24th May 2024, 11:59 PM", "Draw and label parts of a plant and write their functions.", "Pending", R.drawable.ic_science, "#F0F7FF", "#3399FF"),
            AssignmentData("English - Comprehension", "Due: 25th May 2024, 11:59 PM", "Read the passage and answer the questions that follow.", "Pending", R.drawable.ic_library, "#FFF5F5", "#E61214")
        )

        populateContainer(container, items)
    }

    private fun setupSubmittedAssignments(container: LinearLayout) {
        val items = listOf(
            AssignmentData("Computer Studies - Typing Test", "Submitted: 19th May 2024", null, "Submitted", R.drawable.ic_computer, "#F0F7FF", "#3399FF"),
            AssignmentData("Art - Drawing and Colouring", "Submitted: 18th May 2024", null, "Submitted", R.drawable.ic_art, "#FFF9E6", "#F2A93B")
        )

        populateContainer(container, items)
    }

    private fun populateContainer(container: LinearLayout, items: List<AssignmentData>) {
        container.removeAllViews()
        for (data in items) {
            val itemView = layoutInflater.inflate(R.layout.item_student_assignment, container, false)
            
            val icon = itemView.findViewById<ImageView>(R.id.assignmentIcon)
            val title = itemView.findViewById<TextView>(R.id.assignmentTitle)
            val date = itemView.findViewById<TextView>(R.id.assignmentDate)
            val desc = itemView.findViewById<TextView>(R.id.assignmentDesc)
            val status = itemView.findViewById<TextView>(R.id.assignmentStatus)

            icon.setImageResource(data.iconRes)
            icon.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(data.bgColor)))
            icon.imageTintList = ColorStateList.valueOf(Color.parseColor(data.iconTint))

            title.text = data.title
            date.text = data.date
            
            if (data.desc != null) {
                desc.text = data.desc
                desc.visibility = View.VISIBLE
            } else {
                desc.visibility = View.GONE
            }

            status.text = data.status
            if (data.status == "Submitted") {
                status.setBackgroundResource(R.drawable.badge_bg_active)
                status.setTextColor(Color.parseColor("#086B38"))
                date.setTextColor(Color.parseColor("#999999"))
            }

            container.addView(itemView)
        }
    }

    data class AssignmentData(
        val title: String,
        val date: String,
        val desc: String?,
        val status: String,
        val iconRes: Int,
        val bgColor: String,
        val iconTint: String
    )
}