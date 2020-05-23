package com.internshala.foodfam.activity

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.internshala.foodfam.R

class HeaderActivity : AppCompatActivity() {
    lateinit var sharedPreference: SharedPreferences
    lateinit var headerName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreference=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_header)

        headerName=findViewById<TextView>(R.id.txtHeaderName)
        headerName.setText(sharedPreference.getString("name","User")).toString()
    }
}
