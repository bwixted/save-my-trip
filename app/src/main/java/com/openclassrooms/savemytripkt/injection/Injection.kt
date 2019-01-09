package com.openclassrooms.savemytripkt.injection

import android.content.Context

import com.openclassrooms.savemytripkt.database.SaveMyTripDatabase
import com.openclassrooms.savemytripkt.repositories.ItemDataRepository
import com.openclassrooms.savemytripkt.repositories.UserDataRepository

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * Created by Philippe on 27/02/2018.
 */

object Injection {

    private fun provideItemDataSource(context: Context): ItemDataRepository {
        val database = SaveMyTripDatabase.getInstance(context)
        return ItemDataRepository(database!!.itemDao())
    }

    private fun provideUserDataSource(context: Context): UserDataRepository {
        val database = SaveMyTripDatabase.getInstance(context)
        return UserDataRepository(database!!.userDao())
    }

    private fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val dataSourceItem = provideItemDataSource(context)
        val dataSourceUser = provideUserDataSource(context)
        val executor = provideExecutor()
        return ViewModelFactory(dataSourceItem, dataSourceUser, executor)
    }
}
