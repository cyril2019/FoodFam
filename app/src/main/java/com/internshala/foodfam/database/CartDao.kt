package com.internshala.foodfam.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CartDao {
    @Insert
    fun insertRes(cartEntity: CartEntity)

    @Delete
    fun deleteRes(cartEntity: CartEntity)

    @Query("SELECT*FROM cart")
    fun getAllBooks():List<CartEntity>

    @Query("SELECT*FROM cart WHERE dish_id=:dishId")
    fun getResById(dishId:String):CartEntity

}