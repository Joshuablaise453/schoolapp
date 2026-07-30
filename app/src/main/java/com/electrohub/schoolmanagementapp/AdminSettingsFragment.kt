package com.electrohub.schoolmanagementapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class AdminSettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_settings, container, false)

        setupGeneralSettings(view)
        setupSystemSettings(view)
        setupSupportAbout(view)

        return view
    }

    private fun setupGeneralSettings(view: View) {
        setupRow(view.findViewById(R.id.rowProfile), R.drawable.ic_nav_profile, "Profile Settings", "Manage your personal information")
        setupRow(view.findViewById(R.id.rowPassword), R.drawable.ic_lock, "Change Password", "Update your account password")
        setupRow(view.findViewById(R.id.rowNotifications), R.drawable.ic_nav_notifications, "Notification Settings", "Manage your notification preferences")
        setupRow(view.findViewById(R.id.rowLanguage), R.drawable.ic_web, "Language", "Choose your preferred language", "English")
        setupRow(view.findViewById(R.id.rowTheme), R.drawable.ic_palette, "Theme", "Choose your preferred theme", "System Default", true)
    }

    private fun setupSystemSettings(view: View) {
        setupRow(view.findViewById(R.id.rowAcademicYear), R.drawable.ic_calendar, "Academic Year", null, "2024/2025")
        setupRow(view.findViewById(R.id.rowGrading), R.drawable.ic_grading, "Grading System", null, "Competency Based")
        setupRow(view.findViewById(R.id.rowAttendance), R.drawable.ic_attendance_check, "Attendance Settings", "Configure attendance options")
        setupRow(view.findViewById(R.id.rowBackup), R.drawable.ic_backup, "Backup & Restore", "Manage your data backups")
        setupRow(view.findViewById(R.id.rowData), R.drawable.ic_data, "Data Management", "Import, export or clear data")
        setupRow(view.findViewById(R.id.rowLogs), R.drawable.ic_logs, "System Logs", "View system activity logs")
    }

    private fun setupSupportAbout(view: View) {
        setupRow(view.findViewById(R.id.rowHelp), R.drawable.ic_help, "Help & Support", "Get help and support")
        setupRow(view.findViewById(R.id.rowAbout), R.drawable.ic_notice, "About Hillside Schools Naalya", "App version 1.0.0")
        
        val logoutRow = view.findViewById<View>(R.id.rowLogout)
        setupRow(logoutRow, android.R.drawable.ic_lock_power_off, "Logout", "Sign out from your account", isError = true)
        logoutRow.setOnClickListener {
            (activity as? AdminDashboard)?.logout()
        }
    }

    private fun setupRow(row: View, iconRes: Int, title: String, subtitle: String?, value: String? = null, isValueBadge: Boolean = false, isError: Boolean = false) {
        val icon = row.findViewById<ImageView>(R.id.rowIcon)
        val titleText = row.findViewById<TextView>(R.id.rowTitle)
        val subtitleText = row.findViewById<TextView>(R.id.rowSubtitle)
        val valueText = row.findViewById<TextView>(R.id.rowValue)

        icon.setImageResource(iconRes)
        titleText.text = title
        
        if (subtitle != null) {
            subtitleText.text = subtitle
            subtitleText.visibility = View.VISIBLE
        } else {
            subtitleText.visibility = View.GONE
        }

        if (value != null) {
            valueText.text = value
            valueText.visibility = View.VISIBLE
            if (isValueBadge) {
                valueText.setBackgroundResource(R.drawable.badge_bg_active)
                valueText.setPadding(24, 8, 24, 8)
            }
        } else {
            valueText.visibility = View.GONE
        }

        if (isError) {
            titleText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
            icon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.red))
            icon.setBackgroundColor(0x1AE61214) // Very light red
        }
    }
}