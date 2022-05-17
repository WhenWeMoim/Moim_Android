package com.legends.moim.src.makeMoim.dialog

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.legends.moim.R

class SettingDialog(context : Context) {

    private val dialog = Dialog(context)

    private lateinit var tvTitle: TextView
    private lateinit var tvMessage: TextView
    private lateinit var btnOK: TextView

    var listener: SettingDialogClickListener? = null

    fun showSettingDialog() {
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_setting)
        dialog.setCancelable(false)

        tvTitle = dialog.findViewById(R.id.dialog_setting_title_tv)
//        tvTitle.text = title

        tvMessage = dialog.findViewById(R.id.dialog_setting_message_tv)
//        tvMessage.text = message

        btnOK = dialog.findViewById(R.id.dialog_setting_ok_btn_tv)
        btnOK.setOnClickListener {

            listener!!.onSettingDialogOKClicked()

            dialog.dismiss()
        }

        dialog.show()
    }

    interface SettingDialogClickListener {
        fun onSettingDialogOKClicked()
//        fun onCancelClicked()
    }
}