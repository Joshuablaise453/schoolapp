package com.electrohub.schoolmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class AdminClassesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_classes, container, false)

        val containerList = view.findViewById<LinearLayout>(R.id.classesListContainer)
        setupSampleData(containerList)

        return view
    }

    private fun setupSampleData(container: LinearLayout) {
        val classes = listOf(
            ClassData("S1", "Baby Class", "Nursery School", "Students: 38", "Teacher: Mr. Okello James"),
            ClassData("S1", "Primary 1 Red", "Primary School", "Students: 37", "Teacher: Ms. Nambooze Ruth"),
            ClassData("S2", "Middle Class", "Nursery School", "Students: 40", "Teacher: Mr. Ssempala Ivan"),
            ClassData("S2", "Primary  2 Red", "Primary School", "Students: 39", "Teacher: Ms. Atukunda Peace"),
            ClassData("S3", "Primary 3 Green", "Primary School", "Students: 36", "Teacher: Mr. Bbosa Charles"),
            ClassData("S3", "Middle Class", "Nursery School", "Students: 35", "Teacher: Ms. Nakyeeyune Gloria"),
            ClassData("P7", "Primary 7", "Primary School", "Students: 37", "Teacher: Mrs. Nalule Sarah")
        )

        container.removeAllViews()
        for (item in classes) {
            val itemView = layoutInflater.inflate(R.layout.admin_class_item, container, false)
            itemView.findViewById<TextView>(R.id.classShortName).text = item.shortName
            itemView.findViewById<TextView>(R.id.className).text = item.name
            itemView.findViewById<TextView>(R.id.classLevelBadge).text = item.level
            itemView.findViewById<TextView>(R.id.studentCount).text = item.students
            itemView.findViewById<TextView>(R.id.teacherName).text = item.teacher
            container.addView(itemView)
        }
    }

    data class ClassData(
        val shortName: String,
        val name: String,
        val level: String,
        val students: String,
        val teacher: String
    )
}