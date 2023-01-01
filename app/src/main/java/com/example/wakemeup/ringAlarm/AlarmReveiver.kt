package com.example.wakemeup.ringAlarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.AlarmListActivity
import com.example.wakemeup.R
import java.io.Serializable
import java.util.*


class AlarmReveiver : BroadcastReceiver() {

    var APP_NAME = com.example.wakemeup.R.string.app_name
    var manager: NotificationManager? = null
    var builder: NotificationCompat.Builder? = null
    var CHANNEL_ID = "Alarm"
    var CHANNEL_NAME = R.string.app_name.toString()
    var cal: Calendar = Calendar.getInstance()


    override fun onReceive(context: Context?, intent: Intent?) {

        val am = context!!.getSystemService(AppCompatActivity.ALARM_SERVICE)
        builder = null
        manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 필요한 적용값
        var data = intent!!.getParcelableExtra<AlarmDataModel>("data") as AlarmDataModel
        var ring = intent!!.getParcelableExtra<Uri>("ringtone")

        var title = data.title
        var dates = data.dates
        var isActivated = data.isActivated
        var sound = ring as Uri

        var mbuilder : NotificationCompat.Builder

        Log.d("alarm Receiver", data.toString())
        var audio : AudioAttributes = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_ALARM).build()
        Log.d("alarm Receiver", sound.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = title
            mChannel.enableLights(true)
            mChannel.setSound(sound,audio)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
            mbuilder = NotificationCompat.Builder(context,CHANNEL_ID)

        } else {
            mbuilder = NotificationCompat.Builder(context)
        }


        // alarm mission open -> 알람창 클릭시 알람 오픈
        val intent2 = Intent(context, popRingActivity::class.java)
        intent2.apply {
            putExtra( "alarmModel", data as Serializable)
        }

        val pendingIntent =
            PendingIntent.getActivity(context, 101, intent2,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)


        //해당 요일이 아니라면 울리지 않음
        var today = (cal.get(Calendar.DAY_OF_WEEK) % 7 - 2)
        if(today < 0) {
            if(today == -1) {
                today = 6
            } else today = 5
        }


        if(!dates[today] || !isActivated){
            return
        }

        RingtonePlay.playAudio(context,sound)
        //알림창 제목
        mbuilder.setContentTitle(title)
        .setSmallIcon(R.drawable.alarm)
        .setAutoCancel(true)
        .setContentIntent(pendingIntent)
        val notification = mbuilder!!.build()
        val notificationManager = NotificationManagerCompat.from(context)


        notificationManager.notify(2, notification)

    }

}
