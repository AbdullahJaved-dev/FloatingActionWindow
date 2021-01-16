package com.logicielhouse.floatingactionwindowdemo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.logicielhouse.floatingactionwindowdemo.service.FloatingViewService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            askPermission()
        }*/
        btnWidget.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                Settings.canDrawOverlays(this) -> {
                    startService()
                }
                else -> {
                    askPermission()
                    Toast.makeText(
                        this,
                        "You need System Alert Window Permission to do this",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            startService()
        }
    }

    fun startService() {
        startService(Intent(this@MainActivity, FloatingViewService::class.java))
        finish()
    }

    @SuppressLint("InlinedApi")
    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            startService()
        } else {
            Toast.makeText(
                this,
                "Draw over other app permission not available. Closing the application",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}