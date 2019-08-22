package com.dailyto_do.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dailyto_do.R
import com.dailyto_do.utils.isShow
import com.dailyto_do.utils.show
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object{
        val TAG = LoginActivity::class.java.simpleName
    }

    private lateinit var mAuth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        signin_btn.setOnClickListener {
            if (checkValidation()){
                login(email_edt.text.toString(),password_edt.text.toString())
            }
        }

        register_tv_login.setOnClickListener {
            finish()
        }
    }
    private fun checkValidation():Boolean{
        if (email_edt.text.isNullOrEmpty()){
            "Email is required".show(this)
            return false
        }
        if (password_edt.text.isNullOrEmpty()){
            "Password is required".show(this)
            return false
        }

        return true
    }

    private fun login(email:String,pwd:String){
        progressbar_signin.isShow(true)
        mAuth.signInWithEmailAndPassword(email,pwd)
            .addOnCompleteListener { task ->
                progressbar_signin.isShow(false)
                if (task.isSuccessful){
                    mainActivity()
                }else{
                    val message = task.exception?.message
                    message?.show(this@LoginActivity)
                    Log.w(TAG,"login error: $message")
                }
            }
    }
    private fun mainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
