package com.example.wakemeup.ringAlarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.AlarmListActivity
import com.example.wakemeup.R
import java.io.Serializable
import java.util.*


class AlarmReceiver : BroadcastReceiver() {

    var manager: NotificationManager? = null
    var builder: NotificationCompat.Builder? = null
    var CHANNEL_ID = "Alarm"
    var CHANNEL_NAME = R.string.app_name.toString()
    var cal: Calendar = Calendar.getInstance()
    var notificationManager :NotificationManagerCompat? = null
    var data : AlarmDataModel? = null
    var mbuilder : NotificationCompat.Builder? = null
    var mChannel : NotificationChannel? = null
    var media : MediaPlayer? = null


    override fun onReceive(context: Context?, intent: Intent?) {


        val am = context!!.getSystemService(AppCompatActivity.ALARM_SERVICE)

        builder = null
        manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        cancelAll()
        if(mChannel!=null && notificationManager!= null) {
            notificationManager!!.deleteNotificationChannel(CHANNEL_ID)
        }

        // 필요한 적용값
        var data = intent!!.getParcelableExtra<AlarmDataModel>("data") as AlarmDataModel;
        var ring = intent!!.getParcelableExtra<Uri>("ringtone");

        var flag = false
        var title = data.title
        var dates = data.dates
        var isActivated = data.isActivated
        var sound = ring as Uri
        var id = data.id

        Log.d("alarm Receiver", data.toString() + " : " + id.toString())
        Log.d("alarm Receiver", sound.toString())
        Log.d("alarm Recevier : id = ", id.toString())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = CHANNEL_NAME
            val importance = NotificationManager.IMPORTANCE_HIGH
            mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel!!.description = title
            mChannel!!.enableLights(true)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel!!)
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
                PendingIntent.getActivity(context, id, intent2,PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            for(x in dates) {
                if(x) {
                    // 설정 요일이 있음을 의미
                    flag = true
                }
            }

            //해당 요일이 아니라면 울리지 않음 - 요일 설정이 있으면 해당 요일에 울림. 요일 설정이 없으면 설정시간에서 가장 가까운 시간에 울림
            if(flag) {
                var today = (cal.get(Calendar.DAY_OF_WEEK) % 7 - 2)
                if(today < 0) {
                    if(today == -1) {
                        today = 6
                    } else today = 5
                }
                if(!dates[today] || !isActivated){
                    cancelAll()
                    return
                }
            }

            //알림창 제목
            mbuilder!!.setContentTitle(title)
                .setSmallIcon(R.drawable.alarm)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        media = RingtonePlay.playAudio(context,sound)

        val notification = mbuilder!!.build()
        notificationManager = NotificationManagerCompat.from(context)
        notificationManager!!.notify(id, notification)
    }

    fun cancelAll() {
        notificationManager = null
        mbuilder = null
        data = null
    }
}
