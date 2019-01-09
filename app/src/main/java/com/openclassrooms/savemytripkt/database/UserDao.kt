package com.openclassrooms.savemytripkt.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import com.openclassrooms.savemytripkt.models.User

/**
 * Created by Philippe on 28/02/2018.
 */

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(user: User)

    @Query("SELECT * FROM User WHERE id = :userId")
    fun getUser(userId: Long): LiveData<User>
}
