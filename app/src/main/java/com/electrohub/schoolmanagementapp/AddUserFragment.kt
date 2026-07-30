package com.electrohub.schoolmanagementapp

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddUserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_user, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setupSpinners(view)
        setupPasswordToggles(view)

        view.findViewById<MaterialButton>(R.id.btnCancel).setOnClickListener {
            (activity as? AdminDashboard)?.onBackPressedDispatcher?.onBackPressed()
        }

        view.findViewById<MaterialButton>(R.id.btnCreateAccount).setOnClickListener {
            performRegistration(view)
        }

        return view
    }

    private fun setupSpinners(view: View) {
        // Gender Spinner
        val spinnerGender = view.findViewById<Spinner>(R.id.spinnerGender)
        val genders = arrayOf("Select gender", "Male", "Female", "Other")
        val genderAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGender.adapter = genderAdapter

        // Role Spinner
        val spinnerRole = view.findViewById<Spinner>(R.id.spinnerRole)
        val roles = arrayOf("Select user role", "Admin", "Teacher", "Student", "Parent", "Bursar", "Librarian")
        val roleAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRole.adapter = roleAdapter

        // Country Code Spinner
        val spinnerCountry = view.findViewById<Spinner>(R.id.spinnerCountryCode)
        val countries = arrayOf("🇺🇬 +256", "🇰🇪 +254", "🇹🇿 +255", "🇷🇼 +250")
        val countryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countryAdapter
    }

    private fun setupPasswordToggles(view: View) {
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val ivShowPassword = view.findViewById<ImageView>(R.id.ivShowPassword)
        val etConfirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword)
        val ivShowConfirmPassword = view.findViewById<ImageView>(R.id.ivShowConfirmPassword)

        ivShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivShowPassword.setImageResource(R.drawable.ic_person_check) // Use ic_person_check as eye open
            } else {
                etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivShowPassword.setImageResource(R.drawable.ic_person_off) // Use ic_person_off as eye closed
            }
            etPassword.setSelection(etPassword.text.length)
        }

        ivShowConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                etConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                ivShowConfirmPassword.setImageResource(R.drawable.ic_person_check)
            } else {
                etConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                ivShowConfirmPassword.setImageResource(R.drawable.ic_person_off)
            }
            etConfirmPassword.setSelection(etConfirmPassword.text.length)
        }
    }

    private fun performRegistration(view: View) {
        val fullName = view.findViewById<EditText>(R.id.etFullName).text.toString().trim()
        val email = view.findViewById<EditText>(R.id.etEmail).text.toString().trim()
        val gender = view.findViewById<Spinner>(R.id.spinnerGender).selectedItem.toString()
        val role = view.findViewById<Spinner>(R.id.spinnerRole).selectedItem.toString()
        val countryCode = view.findViewById<Spinner>(R.id.spinnerCountryCode).selectedItem.toString().split(" ").last()
        val phone = view.findViewById<EditText>(R.id.etPhone).text.toString().trim()
        val password = view.findViewById<EditText>(R.id.etPassword).text.toString()
        val confirmPassword = view.findViewById<EditText>(R.id.etConfirmPassword).text.toString()

        val btnCreate = view.findViewById<MaterialButton>(R.id.btnCreateAccount)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (gender == "Select gender" || role == "Select user role") {
            Toast.makeText(context, "Please select gender and role", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
            return
        }

        btnCreate.text = ""
        btnCreate.isEnabled = false
        progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userMap = hashMapOf(
                            "uid" to userId,
                            "fullName" to fullName,
                            "email" to email,
                            "gender" to gender,
                            "role" to role,
                            "phone" to "$countryCode$phone",
                            "registeredBy" to "Admin",
                            "createdAt" to com.google.firebase.Timestamp.now()
                        )

                        db.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Toast.makeText(context, "User Created Successfully!", Toast.LENGTH_SHORT).show()
                                (activity as? AdminDashboard)?.onBackPressedDispatcher?.onBackPressed()
                            }
                            .addOnFailureListener { e ->
                                resetUI(btnCreate, progressBar)
                                Toast.makeText(context, "Firestore Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    resetUI(btnCreate, progressBar)
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun resetUI(btn: MaterialButton, progress: ProgressBar) {
        btn.text = "Create Account"
        btn.isEnabled = true
        progress.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        (activity as? AdminDashboard)?.updateTopBar(showBack = true)
    }

    override fun onPause() {
        super.onPause()
        (activity as? AdminDashboard)?.updateTopBar(showBack = false)
    }
}