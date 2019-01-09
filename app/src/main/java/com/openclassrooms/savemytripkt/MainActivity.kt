package com.openclassrooms.savemytripkt

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

import com.openclassrooms.savemytripkt.todolist.TodoListActivity
import com.openclassrooms.savemytripkt.tripbook.TripBookActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tripBookButton: Button
    private lateinit var todoListButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_main)

        tripBookButton = findViewById(R.id.main_activity_button_trip_book)
        tripBookButton.setOnClickListener { _ ->
            val intent = Intent(this, TripBookActivity::class.java)
            startActivity(intent)
        }

        todoListButton = findViewById(R.id.main_activity_button_todo_list)
        todoListButton.setOnClickListener { _ ->
            val intent = Intent(this, TodoListActivity::class.java)
            startActivity(intent)
        }

    }

}
