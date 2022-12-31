package com.example.wakemeup.ringAlarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.wakemeup.AlarmDataModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class alarmViewActivity : AppCompatActivity() {

    lateinit var alarmManager: AlarmManager
    var pendingIntent: PendingIntent? = null
    lateinit var data : AlarmDataModel
    var CHANNEL_ID = "Alarm"
    var CHANNEL_NAME = com.example.wakemeup.R.string.app_name.toString()
    private var mCalender: GregorianCalendar? = null
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.wakemeup.R.layout.activity_alarm_view)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        mCalender = GregorianCalendar()
        Log.v("HelloAlarmActivity", mCalender!!.getTime().toString())

        setContentView(com.example.wakemeup.R.layout.activity_main)
        start()

    }

    //알람이 활성화 되면 -> 해당 함수
    private fun start() {
        var calendar = Calendar.getInstance()
//        var time = data.time.split(":")
//        var hour = time[0].toInt()
//        var minute = time[1].toInt()
//        var flag = false

        //AlarmReceiver에 값 전달
        //AlarmReceiver에 값 전달
        val receiverIntent = Intent(this, AlarmReveiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, receiverIntent, PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val from = "2022-12-30 12:10:00" //임의로 날짜와 시간을 지정
        //날짜 포맷을 바꿔주는 소스코드

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var datetime: Date? = null
        try {
            datetime = dateFormat.parse(from)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        calendar.time = datetime
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    // 알람 울리고 -> 멈추기 버튼 (미션)
    private fun stop() {
        if(pendingIntent == null) {
            return
        }

        intent = Intent(this, AlarmReveiver::class.java)
        intent.putExtra("state","off")
        sendBroadcast(intent)

        pendingIntent = null
    }

    private fun unActivate() {
        if(pendingIntent == null) {
            return
        }
        // 알람 취소
        alarmManager.cancel(pendingIntent)

    }

}