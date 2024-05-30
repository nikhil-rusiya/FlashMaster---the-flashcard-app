package com.example.flashmaster

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flashmaster.databinding.ActivityHomeScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class HomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var list = arrayListOf<User>()
        var rvAdapter = RvCardAdapter(this, list)
        binding.RV.layoutManager = LinearLayoutManager(this)
        binding.RV.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()

        val email = intent.extras?.getString("user") ?: "No message found"
        val refId = intent.extras?.getString("refid") ?: "No message found"
        val category = intent.extras?.getString("category") ?: "No message found"

        val db = Firebase.firestore
        db.collection(email).document(refId).collection(category).get()
            .addOnSuccessListener { result ->
                list.clear()
                for (document in result) {
                    // Log.d(TAG, "${document.id} => ${document.data}")
                    var user = document.toObject(User::class.java)
                    user.id = document.id
                    list.add(user)
                }
                rvAdapter.notifyDataSetChanged()
            }.addOnFailureListener { exception ->

            }

        var swipeGesture = object : SwipeGesture(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        AlertDialog.Builder(this@HomeScreen).setIcon(R.drawable.logo)
                            .setTitle("Delete FlashCard").setMessage(
                                "Do you want to delete FlashCard"
                            ).setPositiveButton("Yes") { _, _ ->
                                val cardRefId = list.get(viewHolder.adapterPosition).id.toString()

                                rvAdapter.deleteCard(email, refId, category, cardRefId)
                                list.removeAt(viewHolder.adapterPosition)
                                Toast.makeText(
                                    this@HomeScreen, "FlashCard Deleted", Toast.LENGTH_LONG
                                ).show()
                                rvAdapter.notifyDataSetChanged()
                            }.setNegativeButton("No") { _, _ ->
                                rvAdapter.notifyDataSetChanged()
                            }.show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        AlertDialog.Builder(this@HomeScreen).setIcon(R.drawable.logo)
                            .setTitle("Update FlashCard").setMessage(
                                "Do you want to update FlashCard"
                            ).setPositiveButton("Yes") { _, _ ->
                                var i = Intent(this@HomeScreen, Add_FlashCard::class.java)
                                i.putExtra(
                                    "question",
                                    list.get(viewHolder.adapterPosition).question.toString()
                                )
                                i.putExtra(
                                    "description",
                                    list.get(viewHolder.adapterPosition).description.toString()
                                )
                                i.putExtra(
                                    "videolink",
                                    list.get(viewHolder.adapterPosition).videoLink.toString()
                                )
                                i.putExtra(
                                    "answer", list.get(viewHolder.adapterPosition).answer.toString()
                                )
                                i.putExtra(
                                    "answerdescription",
                                    list.get(viewHolder.adapterPosition).backdescription.toString()
                                )
                                i.putExtra("user", email)
                                i.putExtra("refid", refId)
                                i.putExtra("category", category)
                                startActivity(i)

                                val cardRefId = list.get(viewHolder.adapterPosition).id.toString()

                                rvAdapter.deleteCard(email, refId, category, cardRefId)
                                list.removeAt(viewHolder.adapterPosition)
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

        binding.addBtn.setOnClickListener {
            val i = Intent(this, Add_FlashCard::class.java)
            i.putExtra("user", email)
            i.putExtra("refid", refId)
            i.putExtra("category", category)
            startActivity(i)
            finish()
        }
    }
}