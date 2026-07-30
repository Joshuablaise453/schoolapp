package com.electrohub.schoolmanagementapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddStudentActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var fullNameInput: EditText
    private lateinit var addressInput: EditText
    private lateinit var nokNameInput: EditText
    private lateinit var nokContactInput: EditText
    private lateinit var genderSpinner: Spinner
    private lateinit var classSpinner: Spinner
    private lateinit var generatedEmailText: TextView
    private lateinit var generatedPasswordText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        db = FirebaseFirestore.getInstance()

        // Initialize Views
        fullNameInput = findViewById(R.id.fullNameInput)
        addressInput = findViewById(R.id.addressInput)
        nokNameInput = findViewById(R.id.nokNameInput)
        nokContactInput = findViewById(R.id.nokContactInput)
        genderSpinner = findViewById(R.id.genderSpinner)
        classSpinner = findViewById(R.id.classSpinner)
        generatedEmailText = findViewById(R.id.generatedEmail)
        generatedPasswordText = findViewById(R.id.generatedPassword)

        setupSpinners()

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<Button>(R.id.cancelButton).setOnClickListener { finish() }
        findViewById<Button>(R.id.resetButton).setOnClickListener { resetForm() }

        findViewById<Button>(R.id.generateButton).setOnClickListener {
            generateCredentials()
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveStudent()
        }
    }

    private fun setupSpinners() {
        val genders = arrayOf("Select gender", "Male", "Female")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        val classes = arrayOf("Select class", "Baby","Middle", "Top", "P.1", "P.2", "P.3", "P.4", "P.5", "P.6", "P.7")
        val classAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        classSpinner.adapter = classAdapter
    }

    private fun generateCredentials() {
        val name = fullNameInput.text.toString().trim()
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter full name first", Toast.LENGTH_SHORT).show()
            return
        }

        val emailPart = name.lowercase().replace(" ", "")
        val email = "$emailPart@hillside.com"
        generatedEmailText.text = email

        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*"
        val password = (1..10).map { chars.random() }.joinToString("")
        generatedPasswordText.text = "HpsN2025!$password".take(12)
    }

    private fun saveStudent() {
        val fullName = fullNameInput.text.toString().trim()
        val gender = genderSpinner.selectedItem.toString()
        val studentClass = classSpinner.selectedItem.toString()
        val address = addressInput.text.toString().trim()
        val nokName = nokNameInput.text.toString().trim()
        val nokContact = nokContactInput.text.toString().trim()
        val email = generatedEmailText.text.toString()
        val password = generatedPasswordText.text.toString()

        if (fullName.isEmpty() || gender == "Select gender" || studentClass == "Select class" || address.isEmpty() || nokName.isEmpty() || nokContact.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        val studentData = hashMapOf(
            "fullName" to fullName,
            "gender" to gender,
            "role" to "Student",
            "class" to studentClass,
            "address" to address,
            "nextOfKin" to nokName,
            "nextOfKinContact" to nokContact,
            "email" to email,
            "password" to password,
            "createdAt" to com.google.firebase.Timestamp.now()
        )

        findViewById<Button>(R.id.saveButton).isEnabled = false
        findViewById<Button>(R.id.saveButton).text = "SAVING..."

        db.collection("students").add(studentData)
            .addOnSuccessListener {
                Toast.makeText(this, "Student added successfully", Toast.LENGTH_LONG).show()
                finish()
            }
            .addOnFailureListener { e ->
                findViewById<Button>(R.id.saveButton).isEnabled = true
                findViewById<Button>(R.id.saveButton).text = "SAVE STUDENT"
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun resetForm() {
        fullNameInput.text.clear()
        addressInput.text.clear()
        nokNameInput.text.clear()
        nokContactInput.text.clear()
        genderSpinner.setSelection(0)
        classSpinner.setSelection(0)
        generatedEmailText.text = ""
        generatedPasswordText.text = ""
    }
}