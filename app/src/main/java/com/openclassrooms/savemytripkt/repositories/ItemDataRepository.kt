package com.openclassrooms.savemytripkt.repositories

import android.arch.lifecycle.LiveData

import com.openclassrooms.savemytripkt.database.ItemDao
import com.openclassrooms.savemytripkt.models.Item

/**
 * Created by Philippe on 27/02/2018.
 */

class ItemDataRepository(private val itemDao: ItemDao) {

    // --- GET ---

    fun getItems(userId: Long): LiveData<List<Item>> {
        return this.itemDao.getItems(userId)
    }

    // --- CREATE ---

    fun createItem(item: Item) {
        itemDao.insertItem(item)
    }

    // --- DELETE ---
    fun deleteItem(itemId: Long) {
        itemDao.deleteItem(itemId)
    }

    // --- UPDATE ---
    fun updateItem(item: Item) {
        itemDao.updateItem(item)
    }

}
