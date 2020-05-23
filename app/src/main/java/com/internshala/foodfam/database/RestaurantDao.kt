package com.internshala.foodfam.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.internshala.foodfam.model.Restaurants

@Dao
interface RestaurantDao {

    @Insert
    fun insertRes(resEntity: RestaurantEntity)

    @Delete
    fun deleteRes(resEntity: RestaurantEntity)

    @Query("SELECT*FROM restaurant")
    fun getAllBooks():List<RestaurantEntity>

    @Query("SELECT*FROM restaurant WHERE res_id=:resId")
    fun getResById(resId:String):RestaurantEntity


}