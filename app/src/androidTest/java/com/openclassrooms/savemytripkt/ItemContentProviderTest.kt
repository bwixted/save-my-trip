package com.openclassrooms.savemytripkt

import android.arch.persistence.room.Room
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import com.openclassrooms.savemytripkt.database.SaveMyTripDatabase
import com.openclassrooms.savemytripkt.provider.ItemContentProvider

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertThat

/**
 * Created by Philippe on 01/03/2018.
 */

@RunWith(AndroidJUnit4::class)
class ItemContentProviderTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                SaveMyTripDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        mContentResolver = InstrumentationRegistry.getContext().contentResolver
    }

    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor = mContentResolver!!.query(ContentUris.withAppendedId(ItemContentProvider.URI_ITEM, USER_ID), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(0))
        cursor.close()
    }

    @Test
    fun insertAndGetItem() {
        // BEFORE : Adding demo item
        val userUri = mContentResolver!!.insert(ItemContentProvider.URI_ITEM, generateItem())
        // TEST
        val cursor = mContentResolver!!.query(ContentUris.withAppendedId(ItemContentProvider.URI_ITEM, USER_ID), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        assertThat(cursor.moveToFirst(), `is`(true))
        assertThat(cursor.getString(cursor.getColumnIndexOrThrow("text")), `is`("Visite cet endroit de rêve !"))
    }

    // ---

    private fun generateItem(): ContentValues {
        val values = ContentValues()
        values.put("text", "Visite cet endroit de rêve !")
        values.put("category", "0")
        values.put("isSelected", "false")
        values.put("userId", "1")
        return values
    }

    companion object {

        // DATA SET FOR TEST
        private val USER_ID: Long = 1
    }
}
