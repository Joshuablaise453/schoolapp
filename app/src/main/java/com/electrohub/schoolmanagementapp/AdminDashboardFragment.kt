package com.electrohub.schoolmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class AdminDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_dashboard, container, false)

        view.findViewById<CardView>(R.id.actionAddUser).setOnClickListener {
            (activity as? AdminDashboard)?.switchToAddUser()
        }

        view.findViewById<CardView>(R.id.actionAddStudent).setOnClickListener {
            val intent = Intent(requireContext(), AddStudentActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}