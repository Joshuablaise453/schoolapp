package com.electrohub.schoolmanagementapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electrohub.schoolmanagementapp.R
import com.electrohub.schoolmanagementapp.TeacherDashboard
import com.google.android.material.button.MaterialButton

class TeacherQuizzesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_teacher_quizzes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews(view)

        view.findViewById<MaterialButton>(R.id.btnCreateQuiz).setOnClickListener {
            (activity as? TeacherDashboard)?.switchToCreateQuiz()
        }
    }

    private fun setupRecyclerViews(view: View) {
        val upcomingRv = view.findViewById<RecyclerView>(R.id.upcomingQuizzesRecyclerView)
        upcomingRv.layoutManager = LinearLayoutManager(requireContext())
        upcomingRv.adapter = UpcomingQuizAdapter(listOf(
            QuizUpcoming("Linear Equations Quiz", "P4 - Mathematics", "20 Aug, 2025 • 10:00 AM", "2"),
            QuizUpcoming("Algebraic Expressions Quiz", "P5 - Mathematics", "22 Aug, 2025 • 02:00 PM", "4")
        ))

        val completedRv = view.findViewById<RecyclerView>(R.id.completedQuizzesRecyclerView)
        completedRv.layoutManager = LinearLayoutManager(requireContext())
        completedRv.adapter = CompletedQuizAdapter(listOf(
            QuizCompleted("Word Problems Quiz", "P3 - Mathematics", "Completed on 15 Aug, 2025", "90%"),
            QuizCompleted("Revision Exercise Quiz", "P2 - Mathematics", "Completed on 10 Aug, 2025", "90%"),
            QuizCompleted("Addition and Subtraction Quiz", "P1 - Mathematics", "Completed on 05 Aug, 2025", "85%"),
            QuizCompleted("Shapes and Patterns Quiz", "Baby - Mathematics", "Completed on 01 Aug, 2025", "80%")
        ))
    }

    data class QuizUpcoming(val title: String, val classSubject: String, val dueDate: String, val daysLeft: String)
    data class QuizCompleted(val title: String, val classSubject: String, val completedDate: String, val score: String)

    inner class UpcomingQuizAdapter(private val items: List<QuizUpcoming>) :
        RecyclerView.Adapter<UpcomingQuizAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.quizTitle)
            val classSubject: TextView = view.findViewById(R.id.quizClassSubject)
            val dueDate: TextView = view.findViewById(R.id.quizDueDate)
            val daysLeft: TextView = view.findViewById(R.id.daysLeft)
        }
        override fun onCreateViewHolder(p: ViewGroup, t: Int) = ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.item_teacher_quiz_upcoming, p, false))
        override fun onBindViewHolder(h: ViewHolder, p: Int) {
            val item = items[p]
            h.title.text = item.title
            h.classSubject.text = item.classSubject
            h.dueDate.text = item.dueDate
            h.daysLeft.text = item.daysLeft
        }
        override fun getItemCount() = items.size
    }

    inner class CompletedQuizAdapter(private val items: List<QuizCompleted>) :
        RecyclerView.Adapter<CompletedQuizAdapter.ViewHolder>() {
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.quizTitle)
            val classSubject: TextView = view.findViewById(R.id.quizClassSubject)
            val completedDate: TextView = view.findViewById(R.id.quizCompletedDate)
            val score: TextView = view.findViewById(R.id.scoreBadge)
        }
        override fun onCreateViewHolder(p: ViewGroup, t: Int) = ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.item_teacher_quiz_completed, p, false))
        override fun onBindViewHolder(h: ViewHolder, p: Int) {
            val item = items[p]
            h.title.text = item.title
            h.classSubject.text = item.classSubject
            h.completedDate.text = item.completedDate
            h.score.text = item.score
        }
        override fun getItemCount() = items.size
    }
}