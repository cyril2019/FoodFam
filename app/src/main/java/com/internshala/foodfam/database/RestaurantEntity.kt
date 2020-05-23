package com.internshala.foodfam.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class RestaurantEntity(
    @PrimaryKey val res_id: String,
    @ColumnInfo(name = "res_name") val resName: String,
    @ColumnInfo(name = "res_price") val resPrice: String,
    @ColumnInfo(name = "book_rating") val resRating: String,
    @ColumnInfo(name = "res_image") val resImage: String
)
