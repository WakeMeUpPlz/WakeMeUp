package com.example.wakemeup.ringAlarm

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.net.Uri


object RingtonePlay {

    var mediaPlayer: MediaPlayer? = null
    var isplayingAudio = false


    fun playAudio(c: Context?, uri: Uri) {
        mediaPlayer = MediaPlayer.create(c, uri)
        if (mediaPlayer != null) {
            isplayingAudio = true
            mediaPlayer!!.start()
        }
    }

    fun stopAudio() {
        isplayingAudio = false
        mediaPlayer!!.stop()
        mediaPlayer == null
    }

}