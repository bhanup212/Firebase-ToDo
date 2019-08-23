package com.dailyto_do.views

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dailyto_do.R
import com.dailyto_do.adapter.TodoAdapter
import com.dailyto_do.model.ToDo
import com.dailyto_do.utils.*
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(),TodoAdapter.ToDoListener {
    override fun markAsComplete(todo: ToDo) {
        markAsCompleteFirebase(todo)
    }

    override fun onCommentsClick(todo: ToDo) {
        commentsActivity(todo)
    }

    companion object{
        val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var database: FirebaseDatabase
    private lateinit var todoRef:DatabaseReference
    private lateinit var mAuth:FirebaseAuth

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var todoRv:RecyclerView
    private var todoList:ArrayList<ToDo> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        todoRef  = database.getReference("ToDo").child(mAuth.currentUser?.uid!!)

        //addTodo()
        getAllTodo()

        init()
    }
    private fun init(){
        todoRv = findViewById(R.id.todo_rv)
        todoRv.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        todoRv.itemAnimator = DefaultItemAnimator()
        todoAdapter = TodoAdapter(this,todoList)
        todoAdapter.setTodoListener(this)
        todoRv.adapter = todoAdapter
        todoAdapter.notifyDataSetChanged()

        add_fab.setOnClickListener {
            showAddTodoDialog()
        }
    }

    private fun addTodo(todo:ToDo){
        todoRef.push().setValue(todo)
    }
    private fun getAllTodo(){
        todoRef.addChildEventListener(todoChildListener)
        todoRef.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                todoList.clear()
                for (todoSnapshot in dataSnapshot.children) {
                    val key = todoSnapshot.key
                    val todo = todoSnapshot.getValue(ToDo::class.java)
                    todo?.id = key?:""
                    Log.d(TAG,"All data:$todo")
                    todoList.add(todo!!)
                }
                todoAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun commentsActivity(todo: ToDo){
        val intent = Intent(this,CommentsActivity::class.java)
        intent.putExtra("commentId",todo.id)
        startActivity(intent)
    }

    private fun markAsCompleteFirebase(todo: ToDo){
        todo.isCompleted = true
        todoRef.child(todo.id).setValue(todo)
    }

    private val todoChildListener = object:ChildEventListener{
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {

        }

        override fun onChildAdded(todoSnapshot: DataSnapshot, p1: String?) {
            Log.d(TAG,"onChildAdded: ${todoSnapshot.value}")
            val key = todoSnapshot.key
            val todo = todoSnapshot.getValue(ToDo::class.java)
            todo?.id = key?:""
            Log.d(TAG,"All data:$todo")
            todoList.add(todo!!)
            todoAdapter.notifyDataSetChanged()
        }

        override fun onChildRemoved(p0: DataSnapshot) {

        }

    }

    private fun showAddTodoDialog(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.add_todo_layout)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val metrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(metrics)
        val screenWidth = metrics.widthPixels
        dialog.window!!.setLayout((0.85 * screenWidth).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.show()
        val taskNameTv = dialog.findViewById<TextView>(R.id.add_task_edt)
        val reminderRg = dialog.findViewById<RadioGroup>(R.id.reminder_todo_rg)
        val dateEdt = dialog.findViewById<EditText>(R.id.date_edt)
        val timeEdt = dialog.findViewById<EditText>(R.id.time_edt)

        val addBtn = dialog.findViewById<Button>(R.id.submit_todo_btn)

        dateEdt.setOnClickListener {

            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                val bDay = if(dayOfMonth.toString().length==1) "0$dayOfMonth" else dayOfMonth
                val bMonth = if(month.toString().length==1) "0$month" else month

                dateEdt.setText("$year-$bMonth-$bDay")
            },year,month,day)
            datePickerDialog.datePicker.minDate = System.currentTimeMillis()
            datePickerDialog.show()

        }
        timeEdt.setOnClickListener{
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                val bMinute = if (minute.toString().length == 1) "0$minute" else minute
                val bHour = if(hour.toString().length == 1) "0$hour" else hour

                timeEdt.setText("$bMinute:$bHour:00")
            }

            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        addBtn.setOnClickListener {
            if (taskNameTv.text.toString().isNullOrEmpty()){
                "Task name is required".show(this@MainActivity)
                return@setOnClickListener
            }
            val todo = ToDo(name = taskNameTv.text.toString(),date = getCurrentDate())
            if (reminderRg.checkedRadioButtonId != -1 ){
                when(reminderRg.checkedRadioButtonId){
                    R.id.daily_rb -> {
                        todo.isDailyReminder = true
                    }
                    R.id.once_rb ->{
                        todo.isOneTimeReminder = true
                    }
                }
            }

            if ((todo.isDailyReminder || todo.isOneTimeReminder) && dateEdt.text.toString().isNullOrEmpty() ||
                timeEdt.text.toString().isNullOrEmpty()){
                "Select Date time".show(this)
                return@setOnClickListener
            }else{
                todo.reminderTime = "${dateEdt.text} ${timeEdt.text}"
                val timeMills = getMillsFromDate(todo.reminderTime) - getCurrentTimeMills()
                if (todo.isDailyReminder){
                    setRepeatAlaram(this,todo.name,timeMills)
                }else if (todo.isOneTimeReminder){
                    setAlaramOnce(this,todo.name,timeMills)
                }
            }

            addTodo(todo)
            dialog.dismiss()

        }
    }
}
