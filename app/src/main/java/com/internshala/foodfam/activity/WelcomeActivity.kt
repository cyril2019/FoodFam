package com.internshala.foodfam.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.internshala.foodfam.R

class WelcomeActivity : AppCompatActivity() {

    lateinit var handler: Handler
    lateinit var sharedPreferences:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)
        var loggedin=sharedPreferences.getBoolean("isLoggedin",false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        if(loggedin){
            handler=Handler()
            handler.postDelayed({
                val intent=Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }else{
            handler=Handler()
            handler.postDelayed({
                val intent=Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            },2000)
        }

    }
}
