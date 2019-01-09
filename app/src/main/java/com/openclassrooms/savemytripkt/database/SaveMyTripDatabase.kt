package com.openclassrooms.savemytripkt.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.ContentValues
import android.content.Context

import com.openclassrooms.savemytripkt.models.Item
import com.openclassrooms.savemytripkt.models.User

/**
 * Created by Philippe on 27/02/2018.
 */

@Database(entities = arrayOf(Item::class, User::class), version = 1, exportSchema = false)
abstract class SaveMyTripDatabase : RoomDatabase() {

    // --- DAO ---
    abstract fun itemDao(): ItemDao

    abstract fun userDao(): UserDao

    companion object {

        // --- SINGLETON ---
        @Volatile
        private var INSTANCE: SaveMyTripDatabase? = null

        // --- INSTANCE ---
        fun getInstance(context: Context): SaveMyTripDatabase? {
            if (INSTANCE == null) {
                synchronized(SaveMyTripDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                SaveMyTripDatabase::class.java, "MyDatabase.db")
                                .addCallback(prepopulateDatabase())
                                .build()
                    }
                }
            }
            return INSTANCE
        }

        // ---

        private fun prepopulateDatabase(): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {

                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val contentValues = ContentValues()
                    contentValues.put("id", 1)
                    contentValues.put("username", "Bill")
                    contentValues.put("urlPicture", "http://effinitytech.com/billwixted_small.jpg")
                    db.insert("User", OnConflictStrategy.IGNORE, contentValues)
                }
            }
        }
    }
}


