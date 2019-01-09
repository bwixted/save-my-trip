package com.openclassrooms.savemytripkt.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

import com.openclassrooms.savemytripkt.repositories.ItemDataRepository
import com.openclassrooms.savemytripkt.repositories.UserDataRepository
import com.openclassrooms.savemytripkt.todolist.ItemViewModel

import java.util.concurrent.Executor

/**
 * Created by Philippe on 27/02/2018.
 */


class ViewModelFactory(private val itemDataSource: ItemDataRepository,
                       private val userDataSource: UserDataRepository,
                       private val executor: Executor) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            ItemViewModel(itemDataSource, userDataSource, executor) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}