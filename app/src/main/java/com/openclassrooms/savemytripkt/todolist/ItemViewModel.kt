package com.openclassrooms.savemytripkt.todolist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel

import com.openclassrooms.savemytripkt.models.Item
import com.openclassrooms.savemytripkt.models.User
import com.openclassrooms.savemytripkt.repositories.ItemDataRepository
import com.openclassrooms.savemytripkt.repositories.UserDataRepository
import java.util.concurrent.Executor

/**
 * Created by Philippe on 27/02/2018.
 */

class ItemViewModel(private val itemDataSource: ItemDataRepository,
                    private val userDataRepository: UserDataRepository,
                    private val executor: Executor) : ViewModel() {

    // DATA
    private var currentUser: LiveData<User>? = null

    fun init(userId: Long) {
        if (currentUser != null) {
            return
        }
        currentUser = userDataRepository.getUser(userId)
    }

    // -------------
    // FOR USER
    // -------------

    fun getUser(userId: Long): LiveData<User>? {
        return currentUser
    }

    // -------------
    // FOR ITEM
    // -------------

    fun getItems(userId: Long): LiveData<List<Item>> {
        return itemDataSource.getItems(userId)
    }

    fun createItem(item: Item) {
        executor.execute { itemDataSource.createItem(item) }
    }

    fun deleteItem(itemId: Long) {
        executor.execute { itemDataSource.deleteItem(itemId) }
    }

    fun updateItem(item: Item) {
        executor.execute { itemDataSource.updateItem(item) }
    }
}
