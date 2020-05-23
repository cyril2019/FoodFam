package com.internshala.foodfam.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodfam.R
import com.internshala.foodfam.activity.menuActivity
import com.internshala.foodfam.database.RestaurantDatabase
import com.internshala.foodfam.database.RestaurantEntity
import com.internshala.foodfam.model.Restaurants
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context:Context,val itemList:ArrayList<Restaurants>):RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_view_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val book=itemList[position]
        holder.resName.text=book.resName
        holder.resPrice.text=book.resPrice
        holder.resRating.text=book.resRating
        Picasso.get().load(book.resImage).error(R.drawable.logo).into(holder.resImage)
        holder.llcontent.setOnClickListener{
            val intent=Intent(context,menuActivity::class.java)
            intent.putExtra("ResId",book.resId)
            intent.putExtra("ResName",book.resName)
            context.startActivity(intent)

        }
        val restaurantEntity = RestaurantEntity(
            book.resId,
            book.resName,
            book.resPrice,
            book.resRating,
            book.resImage
        )

        val checkFav = DBAsyncTask(context, restaurantEntity , 1).execute()
        val isFav = checkFav.get()

        if (isFav) {
            holder.notFav.visibility=View.INVISIBLE
            holder.isFav.visibility=View.VISIBLE
        } else {
            holder.notFav.visibility=View.VISIBLE
            holder.isFav.visibility=View.INVISIBLE
        }
        holder.fav.setOnClickListener{
            if(!DBAsyncTask(context,restaurantEntity,1).execute().get()){
                val async=DBAsyncTask(context,restaurantEntity,2).execute()
                val result=async.get()
                if(result){
                    Toast.makeText(
                        context,
                        "${book.resName} added to favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.notFav.visibility=View.INVISIBLE
                    holder.isFav.visibility=View.VISIBLE
                }
                else{
                    Toast.makeText(
                        context,
                        "some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else{
                val async=DBAsyncTask(context,restaurantEntity,3).execute()
                val result=async.get()
                if(result){
                    Toast.makeText(
                        context,
                        "${book.resName} removed from favourites",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.notFav.visibility=View.VISIBLE
                    holder.isFav.visibility=View.INVISIBLE
                }
                else{
                    Toast.makeText(
                        context,
                        "some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }

    class HomeViewHolder(view: View):RecyclerView.ViewHolder(view){
        val resImage:ImageView=view.findViewById(R.id.imgResImage)
        val resName:TextView=view.findViewById(R.id.txtResName)
        val resPrice:TextView=view.findViewById(R.id.txtResPrice)
        val resRating:TextView=view.findViewById(R.id.txtResRating)
        val llcontent:LinearLayout=view.findViewById(R.id.llContent)
        val fav:RelativeLayout=view.findViewById(R.id.fav)
        val isFav:ImageView=view.findViewById(R.id.imgIsFav)
        val notFav:ImageView=view.findViewById(R.id.imgNotFav)


    }
    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        * */

        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

                    // Check DB if the book is favourite or not
                    val book: RestaurantEntity? = db.restaurantDao().getResById(restaurantEntity.res_id.toString())
                    db.close()
                    return book != null

                }

                2 -> {

                    // Save the book into DB as favourite
                    db.restaurantDao().insertRes(restaurantEntity)
                    db.close()
                    return true

                }

                3 -> {

                    // Remove the favourite book
                    db.restaurantDao().deleteRes(restaurantEntity)
                    db.close()
                    return true

                }
            }
            return false
        }

    }
}