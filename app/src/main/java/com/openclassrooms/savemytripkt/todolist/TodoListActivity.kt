package com.openclassrooms.savemytripkt.todolist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.savemytripkt.R
import com.openclassrooms.savemytripkt.injection.Injection
import com.openclassrooms.savemytripkt.models.Item
import com.openclassrooms.savemytripkt.models.User
import com.openclassrooms.savemytripkt.utils.ItemClickSupport

class TodoListActivity : AppCompatActivity(), ItemAdapter.Listener, ItemClickSupport.OnItemClickListener {

    // FOR UI
    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner
    private lateinit var editText: EditText
    private lateinit var profileImage: ImageView
    private lateinit var profileText: TextView
    private lateinit var addButton: Button

    // FOR DATA
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var itemAdapter: ItemAdapter
    private val USER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_todo_list)

        editText = findViewById(R.id.todo_list_activity_edit_text)
        profileImage = findViewById(R.id.todo_list_activity_header_profile_image)
        profileText = findViewById(R.id.todo_list_activity_header_profile_text)

        addButton = findViewById(R.id.todo_list_activity_button_add)
        addButton.setOnClickListener{ createItem() }

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)

        configureSpinner()

        // 8 - Configure RecyclerView & ViewModel
        configureRecyclerView()
        configureViewModel()

        // 9 - Get current user & items from Database
        getCurrentUser(USER_ID)
        getItems(USER_ID)
    }

    // -------------------
    // ACTIONS
    // -------------------

    override fun onClickDeleteButton(position: Int) {
        deleteItem(itemAdapter.getItem(position))
    }

    // -------------------
    // DATA
    // -------------------

    private fun configureViewModel() {
        val mViewModelFactory = Injection.provideViewModelFactory(this)
        itemViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ItemViewModel::class.java)
        itemViewModel.init(USER_ID.toLong())
    }

    // ---

    private fun getCurrentUser(userId: Int) {
        itemViewModel.getUser(userId.toLong())!!.observe(this, Observer { this.updateHeader(it) })
    }

    // ---

    private fun getItems(userId: Int) {
        itemViewModel.getItems(userId.toLong()).observe(this, Observer { this.updateItemsList(it) })
    }

    private fun createItem() {
        val item = Item(this.editText.text.toString(), this.spinner.selectedItemPosition, USER_ID.toLong(), false)
        editText.setText("")
        itemViewModel.createItem(item)
    }

    private fun deleteItem(item: Item) {
        itemViewModel.deleteItem(item.id)
    }

    private fun updateItem(item: Item) {
        item.selected = !item.selected
        itemViewModel.updateItem(item)
    }

    // -------------------
    // UI
    // -------------------

    private fun configureSpinner() {
        spinner = findViewById(R.id.todo_list_activity_spinner)
        val adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }



    private fun configureRecyclerView() {
        itemAdapter = ItemAdapter(this)
        recyclerView = findViewById(R.id.todo_list_activity_recycler_view)
        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val support: ItemClickSupport = ItemClickSupport.addTo(recyclerView, R.layout.activity_todo_list_item)
        support.setOnItemClickListener(this)
    }

    override fun onItemClicked(recyclerView: RecyclerView, position: Int, v: View) {
        updateItem(itemAdapter.getItem(position))
    }

    private fun updateHeader(user: User?) {
        profileText.text = user?.username
        Glide.with(this).load(user?.urlPicture).apply(RequestOptions.circleCropTransform()).into(profileImage)
    }

    private fun updateItemsList(items: List<Item>?) {
        itemAdapter.updateData(items!!)
    }





}
