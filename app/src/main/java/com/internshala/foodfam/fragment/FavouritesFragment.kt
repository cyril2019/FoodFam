package com.internshala.foodfam.fragment


import android.content.Context
import android.os.AsyncTask
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
import androidx.room.Room

import com.internshala.foodfam.R
import com.internshala.foodfam.adapter.FavouritesRecyclerAdapter
import com.internshala.foodfam.database.RestaurantDatabase
import com.internshala.foodfam.database.RestaurantEntity

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    var dbResList= listOf<RestaurantEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_favourites, container, false)

        progressLayout=view.findViewById(R.id.favProgressLayout)
        progressBar=view.findViewById(R.id.favProgressBar)
        progressLayout.visibility=View.VISIBLE
        recyclerView=view.findViewById(R.id.favRecyclerView)
        layoutManager= LinearLayoutManager(activity)

        dbResList=RetrieveFavourites(activity as Context).execute().get()


        if (activity!=null){
            progressLayout.visibility=View.GONE
            recyclerAdapter= FavouritesRecyclerAdapter(activity as Context,dbResList)
            recyclerView.adapter=recyclerAdapter
            recyclerView.layoutManager=layoutManager
        }

        return  view
    }
    class RetrieveFavourites(val context: Context) : AsyncTask<Void, Void, List<RestaurantEntity>>() {

        override fun doInBackground(vararg p0: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()
            return db.restaurantDao().getAllBooks()
        }

    }

}
