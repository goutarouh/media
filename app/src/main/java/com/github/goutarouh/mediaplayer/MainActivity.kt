package com.github.goutarouh.mediaplayer

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private var _player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        _player = MediaPlayer()

        val uriStr = "android.resource://${packageName}/${R.raw.test}"

        val mediaFIleUri = Uri.parse(uriStr)

        try {
            _player?.setDataSource(applicationContext, mediaFIleUri)

            _player?.setOnPreparedListener(PlayerPreparedListener())

            _player?.setOnCompletionListener(PlayerCompletionListener())

            _player?.prepareAsync()
        } catch (ex: Exception) {
            Log.i("hasegawa", "catch all type exception.")
        }

        findViewById<Button>(R.id.stop_or_start).setOnClickListener {
            Log.i("hasegawa", "stop or start")
            onPlayButtonClick(it)
        }

        findViewById<Button>(R.id.prev).setOnClickListener {
            _player?.seekTo(0)
        }

        findViewById<Button>(R.id.next).setOnClickListener {
            _player?.let {
                val duration = it.duration
                it.seekTo(duration)
                if (!it.isPlaying) {
                    it.start()
                }
            }
        }

        findViewById<Button>(R.id.repeat).setOnClickListener {
            val old = _player?.isLooping ?: return@setOnClickListener

            _player?.isLooping = if (old) {
                (it as? Button)?.text = "repeat"
                false
            } else {
                (it as? Button)?.text = "not repeat"
                true
            }
        }

    }


    private inner class PlayerPreparedListener: MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer?) {
            findViewById<Button>(R.id.stop_or_start).isEnabled = true
            findViewById<Button>(R.id.next).isEnabled = true
            findViewById<Button>(R.id.prev).isEnabled = true
            Log.i("hasegawa", "onPrepared")
        }
    }


    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer?) {
            Log.i("hasegawa", "onCompletion")
            val btPlay = findViewById<Button>(R.id.stop_or_start)
            btPlay.text = "再生"
        }
    }

    fun onPlayButtonClick(view: View) {
        _player?.let {
            val btPlay = findViewById<Button>(R.id.stop_or_start)

            if (it.isPlaying) {
                it.pause()
                btPlay.text = "再生"
            } else {
                it.start()
                btPlay.text = "一時停止"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            _player = null
        }
    }
}