package com.github.goutarouh.mediaplayer

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fromN = intent.getBooleanExtra("fromN", false)

        if (fromN) {
            val btPlay = findViewById<Button>(R.id.btPlay)
            val btStop = findViewById<Button>(R.id.btStop)
            btPlay.isEnabled = false
            btStop.isEnabled = true
        }

        findViewById<Button>(R.id.btPlay).setOnClickListener {
            onPlayButtonClick(it)
        }

        findViewById<Button>(R.id.btStop).setOnClickListener {
            onStopButton(it)
        }

    }

    fun onPlayButtonClick(view: View) {
        val intent = Intent(applicationContext, MyService::class.java)

        startService(intent)

        val btPlay = findViewById<Button>(R.id.btPlay)
        val btStop = findViewById<Button>(R.id.btStop)

        btPlay.isEnabled = false
        btStop.isEnabled = true
    }

    fun onStopButton(view: View) {
        val intent = Intent(applicationContext, MyService::class.java)
        stopService(intent)

        val btPlay = findViewById<Button>(R.id.btPlay)
        val btStop = findViewById<Button>(R.id.btStop)

        btPlay.isEnabled = true
        btStop.isEnabled = false
    }
}