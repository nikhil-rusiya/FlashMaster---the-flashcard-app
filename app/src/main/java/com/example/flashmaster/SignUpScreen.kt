package com.example.flashmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.flashmaster.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {
            val email = binding.editEmailAddress.text.toString()
            val username = binding.editUsername.text.toString()
            val pass = binding.editPassword.text.toString()
            val re_pass = binding.editRepeatPassword.text.toString()
            //check if any field is blank
            if (email.isBlank() || username.isBlank() || pass.isBlank() || re_pass.isBlank()) {
                Toast.makeText(this, "Please Fill All The Fields", Toast.LENGTH_LONG).show()
            } else if (pass != re_pass) {
                Toast.makeText(this, "Password and Repeat Password must be same", Toast.LENGTH_LONG)
                    .show()
            } else {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this, LoginScreen::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this, "Registration Failed : ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
    }
}