package com.promecarus.mybroadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.promecarus.mybroadcastreceiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var downloadReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener {
            requestPermissionLauncher.launch(android.Manifest.permission.RECEIVE_SMS)
        }

        binding.btnDownload.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    val notifyFinishIntent = Intent().setAction(ACTION_DOWNLOAD_STATUS)
                    sendBroadcast(notifyFinishIntent)
                }, 3000
            )
        }

        downloadReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Toast.makeText(context, "Download Selesai", Toast.LENGTH_SHORT).show()
            }
        }
        val downloadIntentFilter = IntentFilter(ACTION_DOWNLOAD_STATUS)
        registerReceiver(downloadReceiver, downloadIntentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadReceiver)
    }

    private var requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) Toast.makeText(this, "Sms receiver permission diterima", Toast.LENGTH_SHORT)
            .show()
        else Toast.makeText(this, "Sms receiver permission ditolak", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ACTION_DOWNLOAD_STATUS = "download_status"
    }
}
