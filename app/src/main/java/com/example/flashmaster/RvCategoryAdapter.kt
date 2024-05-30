package com.example.flashmaster

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flashmaster.databinding.CardCategoryBinding
import com.google.firebase.firestore.FirebaseFirestore

class RvCategoryAdapter(var context: Context, var categoryList: ArrayList<Category>) :
    RecyclerView.Adapter<RvCategoryAdapter.ViewHolder>() {
    class ViewHolder(var binding: CardCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RvCategoryAdapter.ViewHolder {
        val binding = CardCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    val db = FirebaseFirestore.getInstance()
    fun deleteCard(email: String, refId: String, category: String) {
        db.collection("No message found")
            .document(refId)
            .delete()
            .addOnSuccessListener { Log.d("tag", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("tag", "Error deleting document", e) }

        db.collection(email)
            .document(refId)
            .delete()
            .addOnSuccessListener { Log.d("tag", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("tag", "Error deleting document", e) }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.categoryText.text = categoryList.get(position).category

        holder.itemView.setOnClickListener {
            val i = Intent(this.context, HomeScreen::class.java)
            i.putExtra("refid", categoryList.get(position).id)
            i.putExtra("category", categoryList.get(position).category)
            this.context.startActivity(i)
        }
    }
}