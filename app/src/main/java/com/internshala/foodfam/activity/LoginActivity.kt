package com.internshala.foodfam.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.session.MediaSessionManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.se.omapi.Session
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodfam.R
import com.internshala.foodfam.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception
import java.sql.Connection

class LoginActivity : AppCompatActivity() {

    lateinit var btnLogin:Button
    lateinit var txtForgetPass:TextView
    lateinit var txtRegister:TextView
    lateinit var etMobileNo:EditText
    lateinit var etPassword:EditText
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        sharedPreferences=this.getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)


        setContentView(R.layout.activity_login)

        btnLogin=findViewById(R.id.btnLogin)
        txtForgetPass=findViewById(R.id.txtForgetPass)
        txtRegister=findViewById(R.id.txtRegister)
        etMobileNo=findViewById(R.id.etMobileNo)
        etPassword=findViewById(R.id.etPassword)


        btnLogin.setOnClickListener {
            val url="http://13.235.250.119/v2/login/fetch_result"
            val queue=Volley.newRequestQueue(this@LoginActivity)
            val jsonParams=JSONObject()
            jsonParams.put("mobile_number",etMobileNo.text.toString())
            jsonParams.put("password",etPassword.text.toString())
            if(ConnectionManager().checkConnectivity(this@LoginActivity)){
                val jsonObjectRequest=object :JsonObjectRequest(Request.Method.POST,url,jsonParams,
                    Response.Listener{
                        print("response is $it")
                        try{
                            val data=it.getJSONObject("data")
                            val success=data.getBoolean("success")
                            print("Success=$success")
                            if(success){
                                val detail=data.getJSONObject("data")
                                sharedPreferences.edit().putBoolean("isLoggedin",true).apply()
                                sharedPreferences.edit().putString("name",detail.getString("name")).apply()
                                sharedPreferences.edit().putString("mobile",detail.getString("mobile_number")).apply()
                                sharedPreferences.edit().putString("email",detail.getString("email")).apply()
                                sharedPreferences.edit().putString("address",detail.getString("address")).apply()
                                val intent=Intent(this,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this, data.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                            }
                        }catch (e:Exception){
                            Toast.makeText(this,"catch error",Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener{
                        print("Error is $it")
                        Toast.makeText(this,"Error Listener reply",Toast.LENGTH_SHORT).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "65d0721832de60"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }
            else{
                val dialog = AlertDialog.Builder(this@LoginActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }

                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }
                dialog.create()
                dialog.show()
            }
        }

        txtRegister.setOnClickListener{
            val intent=Intent(this ,RegistrationActivity::class.java)
            startActivity(intent)
        }
        txtForgetPass.setOnClickListener{
            val intent=Intent(this ,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}
