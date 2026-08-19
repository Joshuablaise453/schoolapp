package com.electrohub.schoolmanagementapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.electrohub.schoolmanagementapp.R
import com.google.android.material.button.MaterialButton

class AddQuizFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<MaterialButton>(R.id.btnCancelQuiz).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        setupSpinners(view)
    }

    private fun setupSpinners(view: View) {
        val classes = arrayOf("Select class", "Baby Class Blue", "P1 Red", "P2 Green", "P3 Yellow", "P4 Blue", "P5 Red")
        val subjects = arrayOf("Select subject", "Mathematics", "English", "Literacy", "Numeracy", "Art", "Outdoor Play")
        val durations = arrayOf("Select duration", "15 Minutes", "30 Minutes", "45 Minutes", "1 Hour", "1.5 Hours", "2 Hours")

        val classAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classes)
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.spinnerQuizClass).adapter = classAdapter

        val subjectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subjects)
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.spinnerQuizSubject).adapter = subjectAdapter
        
        val durationAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, durations)
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        view.findViewById<Spinner>(R.id.spinnerDuration).adapter = durationAdapter
    }
}