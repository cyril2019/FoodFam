package com.internshala.foodfam.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodfam.R
import com.internshala.foodfam.adapter.MenuRecyclerAdapter
import com.internshala.foodfam.database.CartDatabase
import com.internshala.foodfam.database.CartEntity
import com.internshala.foodfam.database.RestaurantEntity
import com.internshala.foodfam.model.Dish
import com.internshala.foodfam.util.ConnectionManager
import java.lang.Exception
import java.util.*

class menuActivity : AppCompatActivity() {

    lateinit var recyclerAdapter: MenuRecyclerAdapter
    lateinit var recyclerView:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var toolbar:Toolbar
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var proceedButton: Button
    var cartList= listOf<CartEntity>()
    var resNo:String?="5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        if(intent!=null){
            resNo=intent.getStringExtra("ResId")
        }
        else{
            Toast.makeText(this,"Didnt recieve intent data",Toast.LENGTH_SHORT).show()
        }

        recyclerView=findViewById(R.id.Restaurant_detail_RecyclerView)
        layoutManager=LinearLayoutManager(this)
        toolbar=findViewById(R.id.toolbar)
        progressLayout=findViewById(R.id.favProgressLayout)
        progressBar=findViewById(R.id.favProgressBar)
        progressLayout.visibility= View.VISIBLE
        proceedButton=findViewById(R.id.btnProceed)
        setUpActionBar()

        val url="http://13.235.250.119/v2/restaurants/fetch_result/"
        val queue=Volley.newRequestQueue(this)
        var dishList= arrayListOf<Dish>()


        cartList=buttonCheck(this).execute().get()
         if(cartList.isEmpty()){
            proceedButton.visibility=View.INVISIBLE
        }
        else{
            proceedButton.visibility=View.VISIBLE
        }



        if(ConnectionManager().checkConnectivity(this)){
            val jsonObjectRequest=object :JsonObjectRequest(
                Method.GET,url+resNo,null,
                Response.Listener{
                    try{
                        progressLayout.visibility=View.INVISIBLE
                        val data=it.getJSONObject("data")
                        val success=data.getBoolean("success")
                        if(success){
                            val details=data.getJSONArray("data")
                            for(i in 0 until details.length()){
                                val resJsonObject=details.getJSONObject(i)
                                val resObject= Dish(
                                    resJsonObject.getString("id"),
                                    resJsonObject.getString("name"),
                                    resJsonObject.getString("cost_for_one"),
                                    resJsonObject.getString("restaurant_id")

                                )
                                dishList.add(resObject)
                                recyclerAdapter= MenuRecyclerAdapter(this,dishList)
                                recyclerView.adapter=recyclerAdapter
                                recyclerView.layoutManager=layoutManager
                            }

                        }
                        else{
                            Toast.makeText(this,"Api send error message", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e: Exception){
                        Toast.makeText(this,"catch error", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener{
                    Toast.makeText(this,"Error Listener reply", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this,"No internet", Toast.LENGTH_SHORT).show()
        }



    }
    fun setUpActionBar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title=intent.getStringExtra("ResName")
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
           val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    class buttonCheck(val context:Context):AsyncTask<Void,Void,List<CartEntity>>(){
        val db=Room.databaseBuilder(context,CartDatabase::class.java,"cart-db").build()
        override fun doInBackground(vararg params: Void?): List<CartEntity> {
            return db.cartDao().getAllBooks()
        }
    }
}
