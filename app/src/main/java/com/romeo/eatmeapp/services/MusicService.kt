package com.romeo.eatmeapp.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.romeo.eatmeapp.R

class MusicService : Service() {

    companion object {
        var isRunning = false
            private set
    }

    private var mediaPlayer: MediaPlayer? = null

    // Binder для привязки
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? = binder

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.music_contex)
        mediaPlayer?.isLooping = true
        isRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!mediaPlayer?.isPlaying!!) {
            mediaPlayer?.start()
        }
        return START_STICKY
    }

    fun resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
            mediaPlayer?.start()
        }
    }

    fun pauseMusic() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
    }

    fun isMusicPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    override fun onDestroy() {
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        mediaPlayer = null
        isRunning = false
        super.onDestroy()
    }
}