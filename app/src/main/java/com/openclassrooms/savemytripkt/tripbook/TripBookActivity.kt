package com.openclassrooms.savemytripkt.tripbook


import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.FileProvider
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast

import com.openclassrooms.savemytripkt.R
import com.openclassrooms.savemytripkt.utils.StorageUtils

import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.support.v7.app.AppCompatActivity

// PERMISSION PURPOSE
private const val RC_STORAGE_WRITE_PERMS = 100

class TripBookActivity : AppCompatActivity() {

    // FILE PURPOSE
    private val FILENAME = "tripBook.txt"
    private val FOLDERNAME = "bookTrip"
    private val AUTHORITY = "com.openclassrooms.savemytripkt.fileprovider"

    //FOR DESIGN
    private lateinit var linearLayoutExternal: LinearLayout
    private lateinit var linearLayoutInternal: LinearLayout

    private lateinit var radioButtonExternal: RadioButton
    private lateinit var radioButtonInternal: RadioButton

    private lateinit var radioButtonExternalPublic: RadioButton
    private lateinit var radioButtonExternalPrivate: RadioButton

    private lateinit var radioButtonInternalVolatile: RadioButton
    private lateinit var radioButtonInternalNormal: RadioButton

    private lateinit var editText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_trip_book)

        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)

        linearLayoutExternal = findViewById(R.id.trip_book_activity_external_choice)
        linearLayoutInternal = findViewById(R.id.trip_book_activity_internal_choice)

        radioButtonExternal = findViewById(R.id.trip_book_activity_radio_external)
        radioButtonInternal = findViewById(R.id.trip_book_activity_radio_internal)

        radioButtonExternalPublic = findViewById(R.id.trip_book_activity_radio_public)
        radioButtonExternalPrivate = findViewById(R.id.trip_book_activity_radio_private)

        radioButtonInternalVolatile = findViewById(R.id.trip_book_activity_radio_volatile)
        radioButtonInternalNormal = findViewById(R.id.trip_book_activity_radio_normal)

        editText = findViewById(R.id.trip_book_activity_edit_text)

        radioButtonInternal.setOnCheckedChangeListener{ buttonView, isChecked ->
            onClickRadioButton(buttonView,isChecked)
        }

        radioButtonExternal.setOnCheckedChangeListener{ buttonView, isChecked ->
            onClickRadioButton(buttonView,isChecked)
        }

        radioButtonInternalVolatile.setOnCheckedChangeListener{ buttonView, isChecked ->
            onClickRadioButton(buttonView,isChecked)
        }

        radioButtonInternalNormal.setOnCheckedChangeListener{ buttonView, isChecked ->
            onClickRadioButton(buttonView,isChecked)
        }

        radioButtonExternalPublic.setOnCheckedChangeListener{ buttonView, isChecked ->
            onClickRadioButton(buttonView,isChecked)
        }

        radioButtonExternalPrivate.setOnCheckedChangeListener{ buttonView, isChecked ->
            onClickRadioButton(buttonView,isChecked)
        }

        this.readFromStorage()

    }

    // ----------------------------------
    // ACTIONS
    // ----------------------------------

    fun onClickRadioButton(button: CompoundButton, isChecked: Boolean) {

        if (isChecked) {
            when (button.id) {
                R.id.trip_book_activity_radio_internal -> {
                    linearLayoutExternal.setVisibility(View.GONE)
                    linearLayoutInternal.setVisibility(View.VISIBLE)
                }
                R.id.trip_book_activity_radio_external -> {
                    linearLayoutExternal.setVisibility(View.VISIBLE)
                    linearLayoutInternal.setVisibility(View.GONE)
                }
            }
        }
        this.readFromStorage()

    }

    private fun save() {

        if (radioButtonExternal.isChecked) {
            writeExternalStorage() //Save on external storage
        } else {
            writeInternalStorage() //Save on internal storage
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_share -> {
                this.shareFile()
                return true
            }
            R.id.action_save -> {
                this.save()
                return true
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


    // ----------------------------------
    // UTILS - STORAGE
    // ----------------------------------


    @AfterPermissionGranted(RC_STORAGE_WRITE_PERMS)
    private fun readFromStorage() {

        // CHECK PERMISSION
        if (!EasyPermissions.hasPermissions(this, WRITE_EXTERNAL_STORAGE)) {
            EasyPermissions.requestPermissions(this, getString(R.string.title_permission), RC_STORAGE_WRITE_PERMS, WRITE_EXTERNAL_STORAGE)
            return
        }

        if (radioButtonExternal.isChecked) {

            if (StorageUtils.isExternalStorageReadable()) {
                // EXTERNAL
                if (radioButtonExternalPublic.isChecked) {
                    // External - Public
                    editText.setText(StorageUtils.getTextFromStorage(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), this, FILENAME, FOLDERNAME))
                } else {
                    // External - Privatex
                    editText.setText(StorageUtils.getTextFromStorage(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!, this, FILENAME, FOLDERNAME))
                }
            }

        } else {

            // INTERNAL
            if (radioButtonInternalVolatile.isChecked) {
                // Cache
                editText.setText(StorageUtils.getTextFromStorage(cacheDir, this, FILENAME, FOLDERNAME))
            } else {
                // Normal
                editText.setText(StorageUtils.getTextFromStorage(filesDir, this, FILENAME, FOLDERNAME))
            }

        }
    }

    private fun writeExternalStorage() {

        if (StorageUtils.isExternalStorageWritable()) {

            if (radioButtonExternalPublic.isChecked) {

                StorageUtils.setTextInStorage(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                        this,
                        FILENAME,
                        FOLDERNAME,
                        this.editText.text.toString())

            } else {

                StorageUtils.setTextInStorage(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
                        this,
                        FILENAME,
                        FOLDERNAME,
                        this.editText.text.toString())

            }
        } else {
            Toast.makeText(this, getString(R.string.external_storage_impossible_create_file), Toast.LENGTH_LONG).show()
        }
    }

    private fun writeInternalStorage() {

        if (radioButtonInternalVolatile.isChecked) {

            StorageUtils.setTextInStorage(cacheDir,
                    this,
                    FILENAME,
                    FOLDERNAME,
                    editText.text.toString())

        } else {

            StorageUtils.setTextInStorage(filesDir,
                    this,
                    FILENAME,
                    FOLDERNAME,
                    editText.text.toString())

        }
    }

    // ----------------------------------
    // SHARE FILE
    // ----------------------------------

    private fun shareFile() {

        val internalFile = StorageUtils.getFileFromStorage(filesDir, this, FILENAME, FOLDERNAME)
        val contentUri = FileProvider.getUriForFile(applicationContext, AUTHORITY, internalFile!!)

        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/*"
        sharingIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.trip_book_share)))

    }




}