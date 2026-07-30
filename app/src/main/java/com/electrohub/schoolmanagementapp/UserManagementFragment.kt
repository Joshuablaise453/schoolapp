package com.electrohub.schoolmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class UserManagementFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_management, container, false)

        view.findViewById<MaterialButton>(R.id.addNewUserBtn).setOnClickListener {
            (activity as? AdminDashboard)?.switchToAddUser()
        }

        return view
    }
}