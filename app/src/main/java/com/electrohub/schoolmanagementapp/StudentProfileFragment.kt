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
        // Personal Info
        setRowData(view.findViewById(R.id.rowFullName), "Full Name", "John Mwangi")
        setRowData(view.findViewById(R.id.rowClass), "Class", "Primary 4 Green")
        setRowData(view.findViewById(R.id.rowRollNumber), "Roll Number", "23")
        setRowData(view.findViewById(R.id.rowDOB), "Date of Birth", "12th March 2014")
        setRowData(view.findViewById(R.id.rowGender), "Gender", "Male")

        // Account Info
        setRowData(view.findViewById(R.id.rowUsername), "Username", "john.mwangi")
        setRowData(view.findViewById(R.id.rowEmail), "Email", "john.mwangi@hillsidesch.naalya")
        setRowData(view.findViewById(R.id.rowPassword), "Password", "••••••••", hasChevron = true)
    }

    private fun setRowData(row: View, label: String, value: String, hasChevron: Boolean = false) {
        row.findViewById<TextView>(R.id.rowLabel).text = label
        row.findViewById<TextView>(R.id.rowValue).text = value
        if (hasChevron) {
            row.findViewById<ImageView>(R.id.rowChevron).visibility = View.VISIBLE
        }
    }
}