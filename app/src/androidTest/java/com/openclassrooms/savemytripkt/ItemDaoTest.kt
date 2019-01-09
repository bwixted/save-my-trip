package com.openclassrooms.savemytripkt

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import com.openclassrooms.savemytripkt.database.SaveMyTripDatabase
import com.openclassrooms.savemytripkt.models.Item
import com.openclassrooms.savemytripkt.models.User

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.assertTrue

/**
 * Created by Philippe on 09/03/2018.
 */
@RunWith(AndroidJUnit4::class)
class ItemDaoTest {

    // FOR DATA
    private lateinit var database: SaveMyTripDatabase

    @Rule @JvmField
    public var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        try {
            this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                    SaveMyTripDatabase::class.java)
                    .allowMainThreadQueries()
                    .build()
        }
        catch(e: java.lang.Exception) {
            println("exception creating database")
            println(e.localizedMessage);
        }
        println("database created")
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database.close()
    }


    // DATA SET FOR TEST
    private val USER_ID: Long = 1
    private val USER_DEMO = User(USER_ID, "Philippe", "https://www.google.fr, ")
    private val NEW_ITEM_PLACE_TO_VISIT = Item("Visit this dream place !", 0, USER_ID)
    private val NEW_ITEM_IDEA = Item("We could do dog sledding ?", 1, USER_ID)
    private val NEW_ITEM_RESTAURANTS = Item("This restaurant looks nice", 2, USER_ID)


    @Test
    @Throws(InterruptedException::class)
    fun insertAndGetUser() {
        // BEFORE : Adding a new user
        this.database.userDao().createUser(USER_DEMO)
        // TEST
        val user = LiveDataTestUtil.getValue(this.database.userDao().getUser(USER_ID))
        assertTrue(user.username == USER_DEMO.username && user.id == USER_ID)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getItemsWhenNoItemInserted() {
        // TEST
        val items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID))
        assertTrue(items.isEmpty())
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndGetItems() {
        // BEFORE : Adding demo user & demo items

        this.database.userDao().createUser(USER_DEMO)
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT)
        this.database.itemDao().insertItem(NEW_ITEM_IDEA)
        this.database.itemDao().insertItem(NEW_ITEM_RESTAURANTS)

        // TEST
        val items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID))
        assertTrue(items.size == 3)
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndUpdateItem() {
        // BEFORE : Adding demo user & demo items. Next, update item added & re-save it
        this.database.userDao().createUser(USER_DEMO)
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT)
        val itemAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID))[0]
        itemAdded.selected = true
        this.database.itemDao().updateItem(itemAdded)

        //TEST
        val items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID))
        assertTrue(items.size == 1 && items[0].selected)
    }

    @Test
    @Throws(InterruptedException::class)
    fun insertAndDeleteItem() {
        // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
        this.database.userDao().createUser(USER_DEMO)
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT)
        val itemAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID))[0]
        this.database.itemDao().deleteItem(itemAdded.id)

        //TEST
        val items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID))
        assertTrue(items.isEmpty())
    }


}
