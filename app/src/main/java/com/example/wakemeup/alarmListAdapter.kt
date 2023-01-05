package com.example.wakemeup

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.wakemeup.ringAlarm.AlarmReceiver
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class alarmListAdapter (val context: Context,  val dateList: MutableList<AlarmDataModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return dateList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(com.example.wakemeup.R.layout.alarm_item, null)

        val timeArea = view.findViewById<TextView>(R.id.timeArea);
        val dOrN = view.findViewById<TextView>(R.id.dOrN)
        val isActivated = view.findViewById<ToggleButton>(R.id.activateBtn);
        val data = ArrayList<TextView>()

        data.add(view.findViewById<TextView>(R.id.monday))
        data.add(view.findViewById<TextView>(R.id.tuesday))
        data.add(view.findViewById<TextView>(R.id.wednesday))
        data.add(view.findViewById<TextView>(R.id.thursday))
        data.add(view.findViewById<TextView>(R.id.friday))
        data.add(view.findViewById<TextView>(R.id.saturday))
        data.add(view.findViewById<TextView>(R.id.sunday))

        timeArea.text = dateList[position].time.toString()
        dOrN.text = dateList[position].dorN.toString()
        isActivated.isChecked = dateList[position].isActivated

        // 어댑터 돌때마다 알람 등록된거 지우고 새로 해줘야함
        AlarmReceiver().cancelAll()

        for(x in 0..data.size-1) {
            if(dateList[position].dates[x]) {
                data[x].setTextColor(Color.BLACK)
            }
            else
                data[x].setTextColor(Color.WHITE)
        }

        if(dateList[position].isActivated){
            start(context, dateList[position])
        }

        isActivated.setOnClickListener {
            dateList[position].isActivated = isActivated.isChecked
            //바뀌면 start
            if(dateList[position].isActivated) run {
                start(context,dateList[position])
            }
        }

        return view
    }

    fun start(context: Context, Data : AlarmDataModel) {

        var alarmManager = context.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        var calendar = Calendar.getInstance()
        var time = Data.time.split(":")
        var hour = time[0].toInt()
        var minute = time[1].toInt()
        var dorN = Data.dorN as String

        if(dorN.equals("PM")) {
            if(hour == 12) {
                hour = hour
            }else {
                hour += 12
            }
        }else{
            if(hour == 12) {
                //오전 12시는 00시
                hour = 0
            }
        }

        Log.d("data : : : ", hour.toString() + ":"+minute.toString())

        //AlarmReceiver에 값 전달 - 반복문으로 배열의 알람들을 가져옴
        val receiverIntent = Intent(context, AlarmReceiver::class.java)
        receiverIntent.apply {
            putExtra("data", Data as Serializable)
            Log.d("data Receiver", Data.toString())
            putExtra("ringtone", Data.ringTone)
            Log.d("data Receiver", Data.ringTone.toString())
            putExtra("number", Data.id)
            Log.d("data Receiver", Data.id.toString())
        }

//        Toast.makeText(context,hour.toString()+ ":" +minute.toString(), Toast.LENGTH_SHORT).show()
        calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND,0)
        }

         //오늘 시간이 지났으면 다음날 같은 시간 - 이부분이 없으면 이전시간으로 설정시 알람 바로 울림
        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
//            Toast.makeText(context,"내일 울립니다.", Toast.LENGTH_SHORT).show()
        }

        var pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(context, Data.id, receiverIntent, PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getBroadcast(context, Data.id, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,pendingIntent)
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,  calendar.timeInMillis, (AlarmManager.INTERVAL_DAY), pendingIntent);
        Log.d("alarm Receiver", "send alarm to receiver")
    }

}
