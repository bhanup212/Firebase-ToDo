package com.dailyto_do.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dailyto_do.R
import com.dailyto_do.model.ToDo
import com.dailyto_do.utils.getCurrentDate
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot



class MainActivity : AppCompatActivity() {

    companion object{
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var todoRef:DatabaseReference
    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        todoRef  = database.getReference("ToDo").child(mAuth.currentUser?.uid!!)

        //addTodo()
        getAllTodo()

    }

    private fun addTodo(){
        todoRef.push().setValue(ToDo("Watch movies", getCurrentDate(),false,false,false,""))
    }
    private fun getAllTodo(){
        todoRef.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (todoSnapshot in dataSnapshot.children) {
                    val todo = todoSnapshot.getValue(ToDo::class.java)
                    Log.d(TAG,"All data:$todo")
                }
            }
        })
    }
}
