package com.dailyto_do.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dailyto_do.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mAuth = FirebaseAuth.getInstance()
        checkUserStatus()
    }
    private fun checkUserStatus(){
        Handler().postDelayed({
            val currentUser = mAuth.currentUser
            if (currentUser != null){
                mainActivity()
            }else {
                registerActivity()
            }
        },1200)
    }

    private fun mainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun registerActivity(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
