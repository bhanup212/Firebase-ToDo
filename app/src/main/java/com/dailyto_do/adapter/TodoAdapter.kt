package com.dailyto_do.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dailyto_do.R
import com.dailyto_do.model.ToDo


class TodoAdapter(var list:List<ToDo>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resp = list.get(position)
        holder.bindDate(resp)
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        fun bindDate(res:ToDo){

        }
    }
}


