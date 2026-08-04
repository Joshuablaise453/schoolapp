package com.electrohub.schoolmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class StudentProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_profile, container, false)

        setupData(view)

        view.findViewById<MaterialButton>(R.id.btnLogOut).setOnClickListener {
            (activity as? StudentDashboard)?.logout()
        }

        return view
    }

    private fun setupData(view: View) {
        val userData = (activity as? StudentDashboard)?.getUserData() ?: return
        
        val name = userData["fullName"] as? String ?: "User"
        val email = userData["email"] as? String ?: ""
        val phone = userData["phone"] as? String ?: ""
        val gender = userData["gender"] as? String ?: ""
        val role = userData["role"] as? String ?: "Student"
        
        // Header
        view.findViewById<TextView>(R.id.profileName).text = name
        view.findViewById<TextView>(R.id.profileRole).text = role
        view.findViewById<TextView>(R.id.profileClass).text = "Primary 4 Green" // Static for now or fetch from class field

        // Personal Info
        setRowData(view.findViewById(R.id.rowFullName), "Full Name", name)
        setRowData(view.findViewById(R.id.rowClass), "Class", "Primary 4 Green")
        setRowData(view.findViewById(R.id.rowRollNumber), "Roll Number", "23")
        setRowData(view.findViewById(R.id.rowDOB), "Date of Birth", "12th March 2014")
        setRowData(view.findViewById(R.id.rowGender), "Gender", gender)

        // Account Info
        setRowData(view.findViewById(R.id.rowUsername), "Phone", phone)
        setRowData(view.findViewById(R.id.rowEmail), "Email", email)
        setRowData(view.findViewById(R.id.rowPassword), "Password", "••••••••", hasChevron = true)
        
        // Next of Kin Info
        setRowData(view.findViewById(R.id.rowKinName), "Next of Kin", userData["kinName"] as? String ?: "Not Set")
        setRowData(view.findViewById(R.id.rowKinPhone), "Kin Contact", userData["kinPhone"] as? String ?: "Not Set")
        setRowData(view.findViewById(R.id.rowKinRelation), "Relationship", "Guardian")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData(view)
    }

    private fun setRowData(row: View, label: String, value: String, hasChevron: Boolean = false) {
        row.findViewById<TextView>(R.id.rowLabel).text = label
        row.findViewById<TextView>(R.id.rowValue).text = value
        if (hasChevron) {
            row.findViewById<ImageView>(R.id.rowChevron).visibility = View.VISIBLE
        }
    }
}