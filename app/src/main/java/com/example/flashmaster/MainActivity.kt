package com.example.flashmaster

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashmaster.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list = arrayListOf<Category>()
        var rvAdapter = RvCategoryAdapter(this, list)
        binding.RV.layoutManager = LinearLayoutManager(this)
        binding.RV.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()

        val email = intent.extras?.getString("user") ?: "No message found"
        val refId = intent.extras?.getString("refid") ?: "No message found"
        val db = Firebase.firestore

        db.collection(email).get().addOnSuccessListener { result ->
            list.clear()
            for (document in result) {
                var category = document.toObject(Category::class.java)
                category.id = document.id
                list.add(category)
            }
            rvAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->

        }
        var swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(this@MainActivity).setIcon(R.drawable.logo)
                            .setTitle("Delete Category").setMessage(
                                "Do you want to delete this Category Collectiion"
                            ).setPositiveButton("Yes") { _, _ ->
                                val refId = list.get(viewHolder.adapterPosition).id.toString()
                                val category =
                                    list.get(viewHolder.adapterPosition).category.toString()

                                rvAdapter.deleteCard(email, refId, category)
                                list.removeAt(viewHolder.adapterPosition)
                                Toast.makeText(this@MainActivity, refId, Toast.LENGTH_LONG).show()
                                rvAdapter.notifyDataSetChanged()
                            }.setNegativeButton("No") { _, _ ->
                                rvAdapter.notifyDataSetChanged()
                            }.show()
                    }
                }
            }
        }
        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(binding.RV)

        binding.addCategoryBtn.setOnClickListener {
            val i = Intent(this, Add_Category::class.java)
            i.putExtra("user", email)
            startActivity(i)
            finish()
        }
        binding.RV.setOnClickListener {
            val i = Intent(this, HomeScreen::class.java)
            startActivity(i)
            finish()
        }
    }
}