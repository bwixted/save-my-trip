package com.openclassrooms.savemytripkt.models

import android.arch.persistence.room.*
import android.content.ContentValues

/**
 * Created by Philippe on 08/03/2018.
 */

@Entity(foreignKeys = arrayOf(ForeignKey(entity = User::class, parentColumns = arrayOf("id"), childColumns = arrayOf("userId"))),
        indices = arrayOf(Index("userId")))
class Item (

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    var text: String = "",

    var category: Int = 0,

    var selected: Boolean = false,

    var userId: Long = 0

) {
    @Ignore
    constructor() : this(0) {}

    constructor(text: String, category: Int, userID: Long, selected: Boolean = false) : this(0) {
        this.text = text
        this.category = category
        this.userId = userID
        this.selected = selected
    }

    companion object {

        // --- UTILS ---
        fun fromContentValues(values: ContentValues): Item {
            val item = Item()
            if (values.containsKey("text")) item.text = values.getAsString("text")
            if (values.containsKey("category")) item.category = values.getAsInteger("category")
            if (values.containsKey("isSelected")) item.selected = values.getAsBoolean("isSelected")
            if (values.containsKey("userId")) item.userId = values.getAsLong("userId")
            return item
        }
    }
}