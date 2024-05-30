package com.example.flashmaster

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.flashmaster.databinding.ActivityAddCategoryBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Add_Category : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore
        val email = intent.extras?.getString("user") ?: "No message found"

        binding.setCategory.setOnClickListener {

            val category = hashMapOf(
                "category" to binding.editCategory.text.toString()
            )

            val catecoryName = binding.editCategory.text.toString()
            val i = Intent(this, MainActivity::class.java)
            db.collection(email)
                .add(category).addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                    i.putExtra("refid", documentReference.id)
                }.addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }

            i.putExtra("user", email)
            startActivity(i)
            finish()
        }
    }
}
