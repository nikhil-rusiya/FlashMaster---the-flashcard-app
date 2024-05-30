package com.example.flashmaster

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.flashmaster.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginScreenBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.loginbtn.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val pass = binding.editPass.text.toString()

            if (email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            val i = Intent(this@LoginScreen, MainActivity::class.java)
                            i.putExtra("user", email)
                            startActivity(i)
                            finish()

                        } else {
                            Toast.makeText(
                                this,
                                "Login Failed : ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        binding.signupbtn.setOnClickListener {
            startActivity(Intent(this, SignUpScreen::class.java))
        }
    }
}