package com.example.wakemeup.ringAlarm

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log


class RingtonePlay {

    companion object {

        var mediaPlayer: MediaPlayer ? =null
        var isplayingAudio : Boolean ? = false

        fun playAudio(c: Context?, uri: Uri) : MediaPlayer {
            if(mediaPlayer == null) {
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer.create(c,uri)
                mediaPlayer!!.start()
            }
            Log.d("sound", "sound is created : " + mediaPlayer!!.isPlaying.toString())
            return mediaPlayer!!
        }

        fun stopAudio(mediaPlayer: MediaPlayer) {
            Log.d("sound", "sound is stopped : " + isplayingAudio.toString())
            try {
                if (this.mediaPlayer != null && this.mediaPlayer!!.isPlaying()) {
                    Log.d("sound", "sound is stopTrying : " + this.isplayingAudio.toString())
                    isplayingAudio = false
                    mediaPlayer?.stop()
                    mediaPlayer?.release()

                } else {
                    mediaPlayer?.release()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("sound",e.message.toString())
            }
        }

    }



}