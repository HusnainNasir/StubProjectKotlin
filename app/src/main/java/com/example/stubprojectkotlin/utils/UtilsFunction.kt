package com.example.stubprojectkotlin.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Base64
import android.util.TypedValue
import com.example.stubprojectkotlin.callbacks.NetworkCallback
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object UtilsFunction {

    // ENCODE BITMAP TO BASE 64 STRING
    fun compressBitmap(bitmap: Bitmap): String {
        val bOut = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut)
        return Base64.encodeToString(bOut.toByteArray(), Base64.NO_WRAP)
    }


    fun convertToDip(value: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP , value.toFloat() ,Resources.getSystem().displayMetrics).toInt()
    }

    // ENCODE FILE TO BASE 64
    fun base64ToFile(file: File): String {
        val bm = BitmapFactory.decodeFile(file.path)
        val bOut = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bOut)
        return Base64.encodeToString(bOut.toByteArray(), Base64.NO_WRAP)
    }

    // DECODE BASE 64 STRING to BITMAP
    fun base64ToBitmapDecode(base64: String): Bitmap? {
        var bitmap: Bitmap? = null
        val imageAsBytes = Base64.decode(base64.toByteArray(), Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
        return bitmap
    }

    // DATE TO STRING
    fun convertDateToString(date: Date?, dateFormatString: String?): String {
        @SuppressLint("SimpleDateFormat") val dateFormat: DateFormat =
            SimpleDateFormat(dateFormatString) // "yyyy-MM-dd hh:mm:ss"
        return dateFormat.format(date)
    }

    // STRING TO DATE
    fun convertStringToDate(stringDate: String?, dateFormatString: String?): Date? {
        @SuppressLint("SimpleDateFormat") val formatter =
            SimpleDateFormat(dateFormatString) // "dd-MMM-yyyy HH:mm:ss"
        try {
            return formatter.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    // DATE FORMAT CHANGE
    fun changeDateFormatFromAnother(date: String?, outputFormatString: String?): Date? {

//        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat(inputFormatString); // "yyyy-MM-dd'T'HH:mm:ss.SSS"
        @SuppressLint("SimpleDateFormat") val outputFormat: DateFormat =
            SimpleDateFormat(outputFormatString) // "yyyy-MM-dd"
        var resultDate: Date? = null
        try {
            resultDate = outputFormat.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return resultDate
    }

    // DAYS AGO FUNCTION
    fun getDaysAgo(date: Date): String {
        val days = (Date().time - date.time) / 86400000
        return if (days == 0L) "Today" else if (days == 1L) "Yesterday" else "$days days ago"
    }

    // CHECK INTERNET CONNECTIVITY
    fun isNetworkConnected(networkCallback: NetworkCallback) {

        val thread: Thread = object : Thread() {
            override fun run() {
                Looper.prepare() //Call looper.prepare()
                try {
                    // Connect to Google DNS to check for connection
                    val timeoutMs = 1500
                    val socket = Socket()
                    val socketAddress = InetSocketAddress("8.8.8.8", 53)
                    socket.connect(socketAddress, timeoutMs)
                    socket.close()
                    networkCallback.networkCallback(true)
                } catch (e: IOException) {
                    networkCallback.networkCallback(false)
                }
                Looper.loop()
            }
        }
        thread.start()
    }
}