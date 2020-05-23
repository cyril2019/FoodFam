package com.internshala.foodfam.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.internshala.foodfam.R
import com.internshala.foodfam.activity.menuActivity
import com.internshala.foodfam.database.CartDatabase
import com.internshala.foodfam.database.CartEntity
import com.internshala.foodfam.database.RestaurantDatabase
import com.internshala.foodfam.database.RestaurantEntity
import com.internshala.foodfam.model.Dish

class MenuRecyclerAdapter(val context:Context, val dishList:ArrayList<Dish>):RecyclerView.Adapter<MenuRecyclerAdapter.RestaurantDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.menu_single_row, parent, false)
        return RestaurantDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dishList.size
    }

    override fun onBindViewHolder(holder: RestaurantDetailViewHolder, position: Int) {
        val Dish = dishList[position]
        holder.dishNo.text = (position + 1).toString()
        holder.dishName.text = Dish.dishName
        holder.dishPrice.text = Dish.dishPrice
        val cartEntity = CartEntity(
            (position + 1).toString(),
            Dish.dishName,
            Dish.dishPrice
        )
        val checkFav = DBAsyncTask(context, cartEntity, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.add.text = "REMOVE"
            val addedColor = ContextCompat.getColor(context, R.color.black)
            holder.add.setBackgroundColor(addedColor)
        } else {
            holder.add.text = "ADD"
            val addedColor = ContextCompat.getColor(context, R.color.colorPrimary)
            holder.add.setBackgroundColor(addedColor)
        }
        holder.add.setOnClickListener {
            if (!DBAsyncTask(context, cartEntity, 1).execute().get()) {
                val async = DBAsyncTask(context, cartEntity, 2).execute()
                val result = async.get()
                if (result) {
                    holder.add.text = "REMOVE"
                    val addedColor = ContextCompat.getColor(context, R.color.black)
                    holder.add.setBackgroundColor(addedColor)
                } else {
                    Toast.makeText(
                        context,
                        "some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val async = DBAsyncTask(context, cartEntity, 3).execute()
                val result = async.get()
                if (result) {
                    holder.add.text = "ADD"
                    val addedColor = ContextCompat.getColor(context, R.color.colorPrimary)
                    holder.add.setBackgroundColor(addedColor)
                } else {
                    Toast.makeText(
                        context,
                        "some error occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    class RestaurantDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dishNo: TextView = view.findViewById(R.id.txtDishNo)
        val dishName: TextView = view.findViewById(R.id.txtDishName)
        val dishPrice: TextView = view.findViewById(R.id.txtDishPrice)
        val add: Button = view.findViewById(R.id.btnAdd)
    }


    class DBAsyncTask(val context: Context, val cartEntity: CartEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /*
    Mode 1 -> Check DB if the book is favourite or not
    Mode 2 -> Save the book into DB as favourite
    Mode 3 -> Remove the favourite book
    * */

        val db =
            Room.databaseBuilder(context, CartDatabase::class.java, "cart-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {

            when (mode) {

                1 -> {

                    // Check DB if the book is favourite or not
                    val book: CartEntity? =
                        db.cartDao().getResById(cartEntity.dish_id.toString())
                    db.close()
                    return book != null

                }

                2 -> {

                    // Save the book into DB as favourite
                    db.cartDao().insertRes(cartEntity)
                    db.close()
                    return true

                }

                3 -> {

                    // Remove the favourite book
                    db.cartDao().deleteRes(cartEntity)
                    db.close()
                    return true

                }
            }
            return false
        }

    }
}