package com.electrohub.schoolmanagementapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electrohub.schoolmanagementapp.R
import com.electrohub.schoolmanagementapp.TeacherDashboard
import com.google.android.material.button.MaterialButton

class TeacherAssignmentsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teacher_assignments, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.assignmentsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        
        val assignments = listOf(
            Assignment("Quadratic Equations", "P4 - Mathematics", "Due: 15 Aug, 2025 • 10:00 AM", "2"),
            Assignment("Algebraic Expressions", "P5 - Mathematics", "Due: 18 Aug, 2025 • 02:00 PM", "5"),
            Assignment("Word Problems", "P3 - Mathematics", "Due: 20 Aug, 2025 • 09:00 AM", "7"),
            Assignment("Revision Exercise", "P2 - Mathematics", "Due: 22 Aug, 2025 • 11:00 AM", "9"),
            Assignment("Fractions and Decimals", "P1 - Mathematics", "Due: 25 Aug, 2025 • 10:00 AM", "12")
        )

        recyclerView.adapter = AssignmentAdapter(assignments)

        view.findViewById<MaterialButton>(R.id.btnCreateAssignment).setOnClickListener {
            (activity as? TeacherDashboard)?.switchToCreateAssignment()
        }
    }

    data class Assignment(val title: String, val classSubject: String, val dueDate: String, val daysLeft: String)

    inner class AssignmentAdapter(private val assignments: List<Assignment>) :
        RecyclerView.Adapter<AssignmentAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.assignmentTitle)
            val classSubject: TextView = view.findViewById(R.id.assignmentClassSubject)
            val dueDate: TextView = view.findViewById(R.id.assignmentDueDate)
            val daysLeft: TextView = view.findViewById(R.id.daysLeft)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_teacher_assignment, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val assignment = assignments[position]
            holder.title.text = assignment.title
            holder.classSubject.text = assignment.classSubject
            holder.dueDate.text = assignment.dueDate
            holder.daysLeft.text = assignment.daysLeft
        }

        override fun getItemCount() = assignments.size
    }
}