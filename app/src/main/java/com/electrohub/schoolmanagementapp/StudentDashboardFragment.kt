package com.electrohub.schoolmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class StudentDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userData = (activity as? StudentDashboard)?.getUserData()
        if (userData != null) {
            view.findViewById<TextView>(R.id.dashboardUserName)?.text = userData["fullName"] as? String ?: "User"
        }
    }
}