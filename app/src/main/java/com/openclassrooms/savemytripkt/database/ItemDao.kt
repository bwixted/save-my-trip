package com.openclassrooms.savemytripkt.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import android.database.Cursor

import com.openclassrooms.savemytripkt.models.Item

/**
 * Created by Philippe on 27/02/2018.
 */

@Dao
interface ItemDao {

    @Query("SELECT * FROM Item WHERE userId = :userId")
    fun getItems(userId: Long): LiveData<List<Item>>

    @Query("SELECT * FROM Item WHERE userId = :userId")
    fun getItemsWithCursor(userId: Long): Cursor

    @Insert
    fun insertItem(item: Item): Long

    @Update
    fun updateItem(item: Item): Int

    @Query("DELETE FROM Item WHERE id = :itemId")
    fun deleteItem(itemId: Long): Int
}
