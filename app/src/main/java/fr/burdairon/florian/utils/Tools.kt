package fr.burdairon.florian.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertDateToMillis(date: String): Long {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.parse(date)?.time ?: 0
}

fun saveCachedFileToStorage(context: Context, cacheFileUri: Uri): File? {
    return try {
        val destinationFilename = getFileNameFromUri(context, cacheFileUri)
        val destinationFile = File(context.filesDir, destinationFilename) // Or external storage with permissions

        context.contentResolver.openInputStream(cacheFileUri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        destinationFile
    } catch (e: IOException) {
        // Handle exceptions (e.g., log, show error message)
        Log.e("FileStorage", "Error saving file to storage", e)
        null
    }
}

// Helper function to get file name from Uri
fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName = "unknown"
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex)
                }
            }
        }
    } else if (uri.scheme == "file") {
        uri.path?.let { fileName = File(it).name }
    }
    return fileName
}