package com.example.flashmaster

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.flashmaster.databinding.FlashcardViewBinding
import com.google.firebase.firestore.FirebaseFirestore

class RvCardAdapter(var context: Context, var list: ArrayList<User>) :
    RecyclerView.Adapter<RvCardAdapter.ViewHolder>() {

    class ViewHolder(var binding: FlashcardViewBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FlashcardViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    val db = FirebaseFirestore.getInstance()
    fun deleteCard(email: String, refId: String, category: String, cardRefId: String) {
        db.collection("No message found")
            .document(refId)
            .collection(category)
            .document(cardRefId)
            .delete()
            .addOnSuccessListener { Log.d("tag", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("tag", "Error deleting document", e) }
    }

    fun updateCard(context: Context) {
        Toast.makeText(context, "Updating the card", Toast.LENGTH_LONG).show()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ques.text = list.get(position).question
        holder.binding.desp.text = list.get(position).description
        holder.binding.videolink.text = list.get(position).videoLink
        holder.binding.ans.text = list.get(position).answer
        holder.binding.backvideolink.text = list.get(position).videoLink
        holder.binding.Ansdesp.text = list.get(position).backdescription

        var isFront = true
        var scale = holder.binding.root.resources.displayMetrics.density

        var front_animation: AnimatorSet = AnimatorInflater.loadAnimator(
            holder.binding.root.context,
            R.animator.front_animator
        ) as AnimatorSet
        var back_animation: AnimatorSet = AnimatorInflater.loadAnimator(
            holder.binding.root.context,
            R.animator.back_animator
        ) as AnimatorSet
        holder.binding.frontCard.cameraDistance = 8000 * scale
        holder.binding.backCard.cameraDistance = 8000 * scale

        holder.binding.frontCard.setOnClickListener {
            Toast.makeText(holder.binding.root.context, "HEy Whats up back", Toast.LENGTH_LONG)
                .show()
            if (isFront) {
                front_animation.setTarget(holder.binding.frontCard);
                back_animation.setTarget(holder.binding.backCard);
                front_animation.start()
                back_animation.start()
                isFront = false
            } else {
                front_animation.setTarget(holder.binding.backCard)
                back_animation.setTarget(holder.binding.frontCard)
                back_animation.start()
                front_animation.start()
                isFront = true
            }
        }
    }
}