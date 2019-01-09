package com.openclassrooms.savemytripkt.todolist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.openclassrooms.savemytripkt.R
import com.openclassrooms.savemytripkt.models.Item

import java.util.ArrayList

/**
 * Created by Philippe on 28/02/2018.
 */

class ItemAdapter(private val callback: Listener) : RecyclerView.Adapter<ItemViewHolder>() {

    // FOR DATA
    private var items: List<Item>

    // CALLBACK
    interface Listener {
        fun onClickDeleteButton(position: Int)
    }

    init {
        this.items = ArrayList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.activity_todo_list_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ItemViewHolder, position: Int) {
        viewHolder.updateWithItem(this.items[position], this.callback)
    }

    override fun getItemCount(): Int {
        return this.items.size
    }

    fun getItem(position: Int): Item {
        return this.items[position]
    }

    fun updateData(items: List<Item>) {
        this.items = items
        this.notifyDataSetChanged()
    }
}