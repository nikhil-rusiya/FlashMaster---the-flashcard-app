package com.example.flashmaster

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.flashmaster.databinding.ActivityAddFlashCardBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Add_FlashCard : AppCompatActivity() {
    private lateinit var binding: ActivityAddFlashCardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFlashCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore
        val email = intent.extras?.getString("user") ?: "No message found"
        val refId = intent.extras?.getString("refid") ?: "No message found"
        val category = intent.extras?.getString("category") ?: "No message found"

        binding.editques.setText(intent.extras?.getString("question") ?: "")
        binding.editdesp.setText(intent.extras?.getString("description") ?: "")
        binding.editVideolink.setText(intent.extras?.getString("videolink") ?: "")
        binding.editAnswer.setText(intent.extras?.getString("answer") ?: "")
        binding.editAnsDesp.setText(intent.extras?.getString("answerdescription") ?: "")

        binding.setbtn.setOnClickListener {
            val user = hashMapOf(
                "question" to binding.editques.text.toString(),
                "description" to binding.editdesp.text.toString(),
                "videoLink" to binding.editVideolink.text.toString(),
                "answer" to binding.editAnswer.text.toString(),
                "backdescription" to binding.editAnsDesp.text.toString()
            )

// Add a new document with a generated ID
            val i = Intent(this, HomeScreen::class.java)
            db.collection(email)

                .document(refId)
                .collection(category)
                .add(user)
                .addOnSuccessListener { documentReference ->

                    Toast.makeText(this, "Successfully Added", Toast.LENGTH_LONG).show()
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }

            i.putExtra("user", email)
            i.putExtra("refid", refId)
            i.putExtra("category", category)
            startActivity(i)
            finish()
        }

        binding.setTime.setOnClickListener {
            val i = Intent(this, Set_Notification_Time::class.java)
            i.putExtra("user", email)
            i.putExtra("refid", refId)
            i.putExtra("category", category)
            startActivity(i)
            finish()
        }
    }
}