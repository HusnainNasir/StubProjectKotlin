package com.example.stubprojectkotlin.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.example.stubprojectkotlin.R
import com.example.stubprojectkotlin.callbacks.OnClickCallback

object AlertDialogs {

    // CUSTOM PROGRESS DIALOG
    fun getProgressDialog(context: Context?, cancelable: Boolean?): AlertDialog {

        val dialogBuilder = AlertDialog.Builder(context)

        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val dialogView = inflater.inflate(R.layout.custom_progress_dialog_layout, null)
        dialogBuilder.setView(dialogView)


        val dialog = dialogBuilder.create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(cancelable!!)


        val width = (context.resources.displayMetrics.widthPixels * 0.17f).toInt()
        dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    // CUSTOM ALERT DIALOG
    fun showAlertDialog(
        context: Context?,
        headingText: String?,
        subHeadingText: String?,
        positiveText: String?,
        negativeText: String?,
        hideButton: Boolean,
        onClickCallback: OnClickCallback
    ) {

        if (context == null) return

        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogView = inflater.inflate(R.layout.custom_alert_dialog_layout, null)
        dialogBuilder.setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        val heading = dialogView.findViewById<TextView>(R.id.heading)
        val subheading = dialogView.findViewById<TextView>(R.id.subHeading)
        val clickPositive = dialogView.findViewById<Button>(R.id.yesButton)
        val clickNegative = dialogView.findViewById<Button>(R.id.noButton)

        heading.text = headingText
        subheading.text = subHeadingText
        clickPositive.text = positiveText
        clickNegative.text = negativeText

        clickPositive.setOnClickListener {
            dialog.dismiss()
            onClickCallback.onclick(Constants.CLICK_POSITIVE)
        }
        clickNegative.setOnClickListener {
            dialog.dismiss()
            onClickCallback.onclick(Constants.CLICK_NEGATIVE)
        }
        if (hideButton) clickPositive.visibility = View.GONE

        val width = (context.resources.displayMetrics.widthPixels * 0.80f).toInt()
        dialog.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}

