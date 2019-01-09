package com.openclassrooms.savemytripkt.todolist

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import com.openclassrooms.savemytripkt.R
import com.openclassrooms.savemytripkt.models.Item

import java.lang.ref.WeakReference

/**
 * Created by Philippe on 28/02/2018.
 */

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    var textView: TextView
    var imageView: ImageView
    var imageButton: ImageButton

    init {
        textView = itemView.findViewById<View>(R.id.activity_todo_list_item_text) as TextView
        imageView = itemView.findViewById<View>(R.id.activity_todo_list_item_image) as ImageView
        imageButton = itemView.findViewById<View>(R.id.activity_todo_list_item_remove) as ImageButton
    }

    // FOR DATA
    private var callbackWeakRef: WeakReference<ItemAdapter.Listener>? = null

    fun updateWithItem(item: Item, callback: ItemAdapter.Listener) {
        callbackWeakRef = WeakReference(callback)
        textView.text = item.text
        imageButton.setOnClickListener(this)
        when (item.category) {
            // TO VISIT
            0 -> imageView.setBackgroundResource(R.drawable.ic_room_black_24px)
            // IDEAS
            1 -> imageView.setBackgroundResource(R.drawable.ic_lightbulb_outline_black_24px)
            // RESTAURANTS
            2 -> imageView.setBackgroundResource(R.drawable.ic_local_cafe_black_24px)
        }
        if (item.selected) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onClick(view: View) {
        val callback = callbackWeakRef!!.get()
        callback?.onClickDeleteButton(adapterPosition)
    }
}