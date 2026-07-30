package com.electrohub.schoolmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class EditClassFragment : Fragment() {

    companion object {
        fun newInstance(className: String, level: String, teacher: String, section: String = "", capacity: String = "", description: String = ""): EditClassFragment {
            val fragment = EditClassFragment()
            val args = Bundle()
            args.putString("CLASS_NAME", className)
            args.putString("LEVEL", level)
            args.putString("TEACHER", teacher)
            args.putString("SECTION", section)
            args.putString("CAPACITY", capacity)
            args.putString("DESCRIPTION", description)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_class, container, false)

        setupDropdowns(view)
        preFillData(view)

        view.findViewById<Button>(R.id.btnUpdateClass).setOnClickListener {
            Toast.makeText(requireContext(), "Class updated successfully", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnDeleteClass).setOnClickListener {
            Toast.makeText(requireContext(), "Class deleted successfully", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private fun preFillData(view: View) {
        val args = arguments ?: return
        view.findViewById<EditText>(R.id.etClassName).setText(args.getString("CLASS_NAME"))
        view.findViewById<AutoCompleteTextView>(R.id.levelAutocomplete).setText(args.getString("LEVEL"), false)
        view.findViewById<EditText>(R.id.etSection).setText(args.getString("SECTION"))
        view.findViewById<EditText>(R.id.etCapacity).setText(args.getString("CAPACITY"))
        view.findViewById<AutoCompleteTextView>(R.id.teacherAutocomplete).setText(args.getString("TEACHER"), false)
        view.findViewById<EditText>(R.id.etDescription).setText(args.getString("DESCRIPTION"))
    }

    private fun setupDropdowns(view: View) {
        val levels = arrayOf("Nursery School", "Primary School", "Secondary School")
        val adapterLevels = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, levels)
        view.findViewById<AutoCompleteTextView>(R.id.levelAutocomplete)?.setAdapter(adapterLevels)

        val teachers = arrayOf("Mr. Okello James", "Ms. Nambooze Ruth", "Mr. Ssempala Ivan", "Ms. Atukunda Peace", "Ms. Nakayiza Florence")
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
