package com.example.guitarpawat.alarmapp

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class RingService: Service() {

    lateinit var mediaPlayer: MediaPlayer

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(this, R.raw.solo_cut)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        super.onDestroy()
    }
}