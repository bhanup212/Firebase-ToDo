package com.dailyto_do.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dailyto_do.R
import com.dailyto_do.model.Comment
import com.dailyto_do.model.ToDo


class CommentsAdapter( var list: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = list[position]
        holder.bindDate(comment)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val message: TextView = itemView.findViewById(R.id.comment_message_tv)
        private val date: TextView = itemView.findViewById(R.id.comment_date_tv)

        fun bindDate(comment: Comment) {
            message.text = comment.message
            date.text = comment.date
        }
    }

}