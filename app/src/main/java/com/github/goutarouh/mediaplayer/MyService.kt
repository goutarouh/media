package com.github.goutarouh.mediaplayer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.util.Log
import android.widget.Button
import androidx.core.app.NotificationCompat
import java.lang.Exception

class MyService : Service() {

    private var _player: MediaPlayer? = null

    override fun onCreate() {
        _player = MediaPlayer()

        val id = "testID"

        val name = "hello gouta"

        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(id, name, importance)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
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
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        _player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            _player = null
        }
    }

    private inner class PlayerPreparedListener: MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer) {
            mp.start()

            val builder = NotificationCompat.Builder(applicationContext, "testID").apply {
                setSmallIcon(android.R.drawable.ic_dialog_info)
                setContentTitle("music just started")
                setContentText("music just started (title)")
                setAutoCancel(true)
            }

            val intent = Intent(applicationContext, MainActivity::class.java)

            intent.putExtra("fromN", true)
            val stopServiceIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            builder.setContentIntent(stopServiceIntent)
            val notification = builder.build()

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1, notification)
        }
    }

    private inner class PlayerCompletionListener: MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer?) {
            val builder = NotificationCompat.Builder(applicationContext, "testID")
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            builder.setContentTitle("hello goutarouh!")
            builder.setContentText("good morning")
            val notification = builder.build()
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(0, notification)

            stopSelf()
        }
    }



    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}