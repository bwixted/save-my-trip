package com.openclassrooms.savemytripkt.repositories

import android.arch.lifecycle.LiveData

import com.openclassrooms.savemytripkt.database.UserDao
import com.openclassrooms.savemytripkt.models.User

/**
 * Created by Philippe on 28/02/2018.
 */

class UserDataRepository(private val userDao: UserDao) {

    // --- GET USER ---
    fun getUser(userId: Long): LiveData<User> {
        return this.userDao.getUser(userId)
    }
}
