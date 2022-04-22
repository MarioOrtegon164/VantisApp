package com.concredito.concreditoapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog


class Utils : AppCompatActivity() {

    lateinit var dialog :AlertDialog
    lateinit var  pDialog:SweetAlertDialog

    fun hideDialog(context: Activity) {
        dialog.dismiss()
    }

    fun showDialogLoading(activity: Activity) {
        pDialog = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog.titleText = "Cargando ..."
        pDialog.setCancelable(true)
        pDialog.show()
    }

    fun showErrorDialog(activity: Activity,message:String) {
        SweetAlertDialog(activity, SweetAlertDialog.ERROR_TYPE)
            .setTitleText("Error")
            .setContentText(message)
            .setConfirmText("Aceptar")
            .setConfirmClickListener {
                activity.finish()
            }.show()
    }
    fun showSuccessFinalDialog(activity: Activity,message:String){
        SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Exito!")
            .setContentText(message)
            .setConfirmClickListener {
                it.dismissWithAnimation()
                activity.finish()
            }
            .show()
    }

    fun hideDialogLoading(){
        pDialog.dismissWithAnimation()
    }

}