package com.dailyto_do.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dailyto_do.R
import com.dailyto_do.model.ToDo


class TodoAdapter(val context: Context,var list:List<ToDo>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {

    private var todoListener:ToDoListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_row,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = list[position]
        holder.bindDate(todo)

        holder.completedTv.setOnClickListener {
            todoListener?.markAsComplete(todo)
        }
        holder.commentsTv.setOnClickListener {
            todoListener?.onCommentsClick(todo)
        }
    }
    fun setTodoListener(listener: ToDoListener){
        todoListener = listener
    }
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val taskTv:TextView = itemView.findViewById(R.id.todo_task_tv)
        val completedTv:TextView = itemView.findViewById(R.id.task_completed_tv)
        val commentsTv:TextView = itemView.findViewById(R.id.task_comments_tv)

        fun bindDate(todo:ToDo){
            taskTv.text = todo.name
            completedTv.text = if (todo.isCompleted)"Completed" else "Mark Complete"
        }
    }

    interface ToDoListener{
        fun markAsComplete(todo: ToDo)
        fun onCommentsClick(todo: ToDo)
    }
}


