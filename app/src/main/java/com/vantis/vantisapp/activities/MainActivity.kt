package com.vantis.vantisapp.activities

import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.vantis.vantisapp.R
import com.vantis.vantisapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Bloquear orientacion a vertical
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onBackPressed() {
        SweetAlertDialog(this@MainActivity, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("¿Desea salir de la aplicación?")
            .setConfirmText("Salir")
            .setConfirmClickListener {
                    sDialog -> sDialog.dismissWithAnimation()
                finish()
            }
            .setCancelButton(
                "Cancelar"
            ) { sDialog -> sDialog.dismissWithAnimation() }
            .show()
    }
}