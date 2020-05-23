package com.internshala.foodfam.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodfam.R
import com.internshala.foodfam.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject
import java.lang.Exception
import java.security.cert.CertPathBuilderSpi

class RegistrationActivity : AppCompatActivity() {

    lateinit var name:EditText
    lateinit var email:EditText
    lateinit var mobile:EditText
    lateinit var delivery:EditText
    lateinit var pass:EditText
    lateinit var cpass:EditText
    lateinit var register:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        name=findViewById(R.id.etName)
        email=findViewById(R.id.etEmail)
        mobile=findViewById(R.id.etMobileNo)
        delivery=findViewById(R.id.etDeliveryAddress)
        pass=findViewById(R.id.etPassword)
        cpass=findViewById(R.id.etConfirmPassword)
        register=findViewById(R.id.btnRegister)
        sharedPreferences=this.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)




        btnRegister.setOnClickListener {
            if(pass.text.toString()!=cpass.text.toString()){
                Toast.makeText(this,"Passwords do nat match",Toast.LENGTH_LONG).show()
            }
            val url="http://13.235.250.119/v2/register/fetch_result"
            val queue= Volley.newRequestQueue(this)
            val jsonParams= JSONObject()
            jsonParams.put("name",name.text.toString())
            jsonParams.put("mobile_number",mobile.text.toString())
            jsonParams.put("password",pass.text.toString())
            jsonParams.put("address",delivery.text.toString())
            jsonParams.put("email",email.text.toString())

            if(ConnectionManager().checkConnectivity(this)){
                val jsonObjectRequest=object : JsonObjectRequest(
                    Request.Method.POST,url,jsonParams,
                    Response.Listener{
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
                                val intent= Intent(this,MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this,"${data.getString("errorMessage")}", Toast.LENGTH_SHORT).show()
                            }
                        }catch (e: Exception){
                            Toast.makeText(this,"catch error", Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener{
                        Toast.makeText(this,"error", Toast.LENGTH_SHORT).show()
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
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection is not Found")
                dialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }

                dialog.setNegativeButton("Exit") { text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create()
                dialog.show()
            }
        }

    }
}
