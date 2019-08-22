package com.dailyto_do.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dailyto_do.R
import com.dailyto_do.utils.isShow
import com.dailyto_do.utils.show
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    companion object{
        val TAG = RegisterActivity::class.java.simpleName
    }

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        mAuth = FirebaseAuth.getInstance()

        register_btn.setOnClickListener {
            if (checkValidations()){
                registerUser(register_email_edt.text.toString(),register_password_edt.text.toString())
            }
        }

        login_tv_register.setOnClickListener {
            loginActivity()
        }
    }
    private fun checkValidations():Boolean{
        if (register_email_edt.text.isNullOrEmpty()){
            "Email is required".show(this)
            return false
        }
        if (register_password_edt.text.toString().isNullOrEmpty()){
            "Password is required".show(this)
            return false
        }
        if (register_password_edt.text.toString() != confirm_password_edt.text.toString()){
            "Password didn't match".show(this)
            return false
        }

        return true
    }
    private fun registerUser(email:String,password:String){
        register_progressbar.isShow(true)
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this
            ) { task ->
                register_progressbar.isShow(false)
                if (task.isSuccessful){
                    mainActivity()
                }else{
                    Log.w(TAG,"register error:${task.exception?.message}")
                    task.exception?.message?.show(this@RegisterActivity)
                }
            }
    }
    private fun mainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
    private fun loginActivity(){
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
