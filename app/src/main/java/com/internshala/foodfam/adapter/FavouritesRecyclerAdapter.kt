package com.internshala.foodfam.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodfam.R
import com.internshala.foodfam.activity.menuActivity
import com.internshala.foodfam.database.RestaurantDatabase
import com.internshala.foodfam.database.RestaurantEntity
import com.squareup.picasso.Picasso

class FavouritesRecyclerAdapter(val context:Context,val itemList:List<RestaurantEntity>) :RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouriteViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_view_single_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val res=itemList[position]
        holder.resName.text=res.resName
        holder.resPrice.text=res.resPrice
        holder.resRating.text=res.resRating
        Picasso.get().load(res.resImage).error(R.drawable.logo).into(holder.resImage)
        holder.llcontent.setOnClickListener{
            val intent= Intent(context, menuActivity::class.java)
            intent.putExtra("ResId",res.res_id)
            intent.putExtra("ResName",res.resName)
            context.startActivity(intent)

        }
        val restaurantEntity = RestaurantEntity(
            res.res_id,
            res.resName,
            res.resPrice,
            res.resRating,
            res.resImage
        )

        val checkFav = HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 1).execute()
        val isFav = checkFav.get()

        if (isFav) {
            holder.notFav.visibility=View.INVISIBLE
            holder.isFav.visibility=View.VISIBLE
        } else {
            holder.notFav.visibility=View.VISIBLE
            holder.isFav.visibility=View.INVISIBLE
        }
        holder.fav.setOnClickListener{
            if(!HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 1).execute().get()){
                val async= HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 2).execute()
                val result=async.get()
                if(result){
                    Toast.makeText(
                        context,
                        "${res.resName} added to favourites",
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
                val async= HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 3).execute()
                val result=async.get()
                if(result){
                    Toast.makeText(
                        context,
                        "${res.resName} removed from favourites",
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

    class FavouriteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val resImage: ImageView =view.findViewById(R.id.imgResImage)
        val resName: TextView =view.findViewById(R.id.txtResName)
        val resPrice: TextView =view.findViewById(R.id.txtResPrice)
        val resRating: TextView =view.findViewById(R.id.txtResRating)
        val llcontent: LinearLayout =view.findViewById(R.id.llContent)
        val fav: RelativeLayout =view.findViewById(R.id.fav)
        val isFav: ImageView =view.findViewById(R.id.imgIsFav)
        val notFav: ImageView =view.findViewById(R.id.imgNotFav)
    }
    class DBAsyncTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
        Mode 1 -> Check DB if the book is favourite or not
        Mode 2 -> Save the book into DB as favourite
        Mode 3 -> Remove the favourite book
        * */

        val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

                    // Check DB if the book is favourite or not
                    val book: RestaurantEntity? =
                        db.restaurantDao().getResById(restaurantEntity.res_id.toString())
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