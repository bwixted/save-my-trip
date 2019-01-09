package com.openclassrooms.savemytripkt.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast

import com.openclassrooms.savemytripkt.R
import java.io.*

/**
 * Created by Philippe on 05/03/2018.
 */

object StorageUtils {

    // ----------------------------------
    // EXTERNAL STORAGE
    // ----------------------------------

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    fun isExternalStorageReadable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
    }

    private fun createOrGetFile(destination: File, fileName: String, folderName: String): File {
        val folder = File(destination, folderName)
        return File(folder, fileName)
    }

    fun getTextFromStorage(rootDestination: File, context: Context, fileName: String, folderName: String): String? {
        val file = createOrGetFile(rootDestination, fileName, folderName)
        return readFile(context, file)
    }

    fun setTextInStorage(rootDestination: File, context: Context, fileName: String, folderName: String, text: String) {
        val file = createOrGetFile(rootDestination, fileName, folderName)
        writeFile(context, text, file)
    }

    fun getFileFromStorage(rootDestination: File, context: Context, fileName: String, folderName: String): File? {
        try {
            return createOrGetFile(rootDestination, fileName, folderName)
        }
        catch(e: NullPointerException) {
            Toast.makeText(context, context.getString(R.string.error_happened), Toast.LENGTH_LONG).show()
        }
        return null
    }


    // ----------------------------------
    // READ & WRITE ON STORAGE
    // ----------------------------------
    private fun readFile(context: Context, file: File): String {

        val sb = StringBuilder()

        if (file.exists()) {
            try {
                val bufferedReader = file.bufferedReader();

                bufferedReader.useLines { lines ->
                    lines.forEach {
                        sb.append(it)
                        sb.append("\n")
                    }
                }
            } catch (e: IOException) {
                Toast.makeText(context, context.getString(R.string.error_happened), Toast.LENGTH_LONG).show()
            }
        }

        return sb.toString()
    }


    // ---

    private fun writeFile(context: Context, text: String, file: File) {


        try {

            file.parentFile.mkdirs()

            file.bufferedWriter().use {
                out -> out.write(text)
            }


        } catch (e: IOException) {
            Toast.makeText(context, context.getString(R.string.error_happened), Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(context, context.getString(R.string.saved), Toast.LENGTH_LONG).show()

    }
}
