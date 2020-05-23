package com.internshala.foodfam.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.internshala.foodfam.R
import com.internshala.foodfam.adapter.HomeRecyclerAdapter
import com.internshala.foodfam.model.Restaurants
import com.internshala.foodfam.util.ConnectionManager
import java.lang.Exception


class HomeFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter:HomeRecyclerAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view=inflater.inflate(R.layout.fragment_home, container, false)

        progressLayout=view.findViewById(R.id.favProgressLayout)
        progressBar=view.findViewById(R.id.favProgressBar)
        progressLayout.visibility=View.VISIBLE
        recyclerView=view.findViewById(R.id.favRecyclerView)
        layoutManager=LinearLayoutManager(activity)
        val queue=Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/"
        var resList= arrayListOf<Restaurants>()


        if(ConnectionManager().checkConnectivity(activity  as Context)){
            val jsonObjectRequest=object :JsonObjectRequest(
                Method.GET,url,null,
                Response.Listener{
                    try{
                    progressLayout.visibility=View.INVISIBLE
                        val data=it.getJSONObject("data")
                        val success=data.getBoolean("success")
                        if(success){
                            val details=data.getJSONArray("data")
                            for(i in 0 until details.length()){
                                val resJsonObject=details.getJSONObject(i)
                                val resObject=Restaurants(
                                    resJsonObject.getString("id"),
                                    resJsonObject.getString("name"),
                                    resJsonObject.getString("rating"),
                                    resJsonObject.getString("cost_for_one"),
                                    resJsonObject.getString("image_url")
                                )
                                resList.add(resObject)
                                recyclerAdapter= HomeRecyclerAdapter(activity as Context,resList)
                                recyclerView.adapter=recyclerAdapter
                                recyclerView.layoutManager=layoutManager
                            }

                        }
                        else{
                            Toast.makeText(activity  as Context,"Api send error message", Toast.LENGTH_SHORT).show()
                        }
                    }catch (e:Exception){
                        Toast.makeText(activity  as Context,"catch error", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener{
                    Toast.makeText(activity  as Context,"Error Listener reply", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(activity as Context,"No internet", Toast.LENGTH_SHORT).show()
        }







        return  view
    }


}
