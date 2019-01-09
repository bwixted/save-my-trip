package com.openclassrooms.savemytripkt.provider

import android.arch.persistence.room.RoomMasterTable.TABLE_NAME
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

import com.openclassrooms.savemytripkt.database.SaveMyTripDatabase
import com.openclassrooms.savemytripkt.models.Item

/**
 * Created by Philippe on 14/03/2018.
 */

class ItemContentProvider : ContentProvider() {


    companion object {
        // FOR DATA
        val AUTHORITY = "com.openclassrooms.savemytripkt.provider"
        val TABLE_NAME = Item::class.java.simpleName
        val URI_ITEM = Uri.parse("content://$AUTHORITY/$TABLE_NAME")
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?,
                       selection: String?,
                       selectionArgs: Array<String>?,
                       sortOrder: String?): Cursor? {

        if (context != null) {
            val userId = ContentUris.parseId(uri)
            val db = SaveMyTripDatabase.getInstance(context)
            val cursor = db!!.itemDao().getItemsWithCursor(userId)
            cursor.setNotificationUri(context.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {

        if (context != null) {
            val db = SaveMyTripDatabase.getInstance(context)
            val id = db!!.itemDao().insertItem(Item.fromContentValues(contentValues!!))
            if (id != 0L) {
                context.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }

        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        if (context != null) {
            val db = SaveMyTripDatabase.getInstance(context)
            val count = db!!.itemDao().deleteItem(ContentUris.parseId(uri))
            context.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to delete row into $uri")
    }

    override fun update(uri: Uri, contentValues: ContentValues?, s: String?, strings: Array<String>?): Int {
        if (context != null) {
            val db = SaveMyTripDatabase.getInstance(context)
            val count = db!!.itemDao().updateItem(Item.fromContentValues(contentValues!!))
            context.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }

}
