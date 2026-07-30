package com.electrohub.schoolmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment

class AddClassFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_class, container, false)

        setupDropdowns(view)

        view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnCreateClass).setOnClickListener {
            // Logic to create class would go here
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun setupDropdowns(view: View) {
        val levels = arrayOf("Nursery School", "Primary School", "Secondary School")
        val adapterLevels = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, levels)
        view.findViewById<AutoCompleteTextView>(R.id.levelAutocomplete)?.setAdapter(adapterLevels)

        val teachers = arrayOf("Mr. Okello James", "Ms. Nambooze Ruth", "Mr. Ssempala Ivan", "Ms. Atukunda Peace")
        val adapterTeachers = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, teachers)
        view.findViewById<AutoCompleteTextView>(R.id.teacherAutocomplete)?.setAdapter(adapterTeachers)

        val statuses = arrayOf("Active", "Inactive")
        val adapterStatuses = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, statuses)
        view.findViewById<AutoCompleteTextView>(R.id.statusAutocomplete)?.setAdapter(adapterStatuses)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AdminDashboard)?.updateTopBar(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? AdminDashboard)?.updateTopBar(false)
    }
}
