package com.dailyto_do.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailyto_do.R
import com.dailyto_do.adapter.CommentsAdapter
import com.dailyto_do.model.Comment
import com.dailyto_do.utils.getCurrentDate
import com.dailyto_do.utils.show
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_comments.*

class CommentsActivity : AppCompatActivity() {

    companion object{
        val TAG = CommentsActivity::class.java.simpleName
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var commentRef: DatabaseReference
    private lateinit var mAuth: FirebaseAuth

    private lateinit var commentsRv:RecyclerView
    private lateinit var commentsAdapter: CommentsAdapter
    private var commentList:ArrayList<Comment> = ArrayList()

    private lateinit var commentId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        commentId = intent.getStringExtra("commentId")
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        commentRef  = database.getReference("Comments").child(mAuth.currentUser?.uid!!).child(commentId)


        init()
        getAllComments()
    }
    private fun init(){
        commentsRv = findViewById(R.id.comment_rv)
        commentsRv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        commentsRv.itemAnimator = DefaultItemAnimator()
        commentsAdapter = CommentsAdapter(commentList)

        commentsRv.adapter = commentsAdapter
        commentsAdapter.notifyDataSetChanged()

        add_comment_btn.setOnClickListener {
            if (comment_edt.text.toString().isNullOrEmpty()){
                "Enter comment".show(this)
            }else{
                addComment(Comment(comment_edt.text.toString(), getCurrentDate()))
                comment_edt.setText("")
            }
        }
    }
    private fun addComment(comment:Comment){
        commentRef.push().setValue(comment)
    }
    private fun getAllComments(){
        commentRef.addChildEventListener(commentChildListener)
        commentRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                commentList.clear()
                for (todoSnapshot in dataSnapshot.children) {
                    val key = todoSnapshot.key
                    val comment = todoSnapshot.getValue(Comment::class.java)

                    commentList.add(comment!!)
                }
                commentsAdapter.notifyDataSetChanged()
            }
        })
    }

    private val commentChildListener = object:ChildEventListener{
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildAdded(todoSnapshot: DataSnapshot, p1: String?) {
            Log.d(TAG,"onChildAdded: ${todoSnapshot.value}")
            val key = todoSnapshot.key
            val comment = todoSnapshot.getValue(Comment::class.java)
            commentList.add(comment!!)
            commentsAdapter.notifyDataSetChanged()
        }

        override fun onChildRemoved(p0: DataSnapshot) {

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
