package com.example.wakemeup

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.addAlarm.addAlarmActivity
import com.example.wakemeup.databinding.ActivityAlarmListBinding
import com.example.wakemeup.ringAlarm.AlarmReveiver
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class AlarmListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlarmListBinding
    lateinit var newData : AlarmDataModel
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    lateinit var alarmList : ArrayList<AlarmDataModel>
    lateinit var alarmManager: AlarmManager
    var CHANNEL_ID = "Alarm"
    var CHANNEL_NAME = com.example.wakemeup.R.string.app_name.toString()
    private var mCalender: GregorianCalendar? = null
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_list)
        val arrayList : ArrayList<Boolean> = arrayListOf<Boolean>()

        var test = AlarmDataModel(
            arrayList, "제발", "01051175487", "AM", true,true, Uri.EMPTY, "08:00"
        )

        if(savedInstanceState != null) {
            alarmList = savedInstanceState.getSerializable("alarmList") as ArrayList<AlarmDataModel> /* = java.util.ArrayList<com.example.wakemeup.AlarmDataModel> */
        } else {
            alarmList = arrayListOf<AlarmDataModel>(test)
            arrayList.add(true)
            arrayList.add(true)
            arrayList.add(true)
            arrayList.add(false)
            arrayList.add(false)
            arrayList.add(true)
            arrayList.add(false)
        }

        val ad = alarmListAdapter(this, alarmList)
        binding.alarmListView.adapter = ad

        activityResultLauncher1 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == RESULT_OK) {
                    //새 알람 추가하기
                    newData = it.data?.getSerializableExtra("newAlarm") as AlarmDataModel
                    alarmList.add(newData)
                    Toast.makeText(this,"new data" + newData.toString(), Toast.LENGTH_SHORT).show()
                    ad.notifyDataSetChanged()
                    Log.d("alarm Receiver", "got new alarm")
                    start()
                }
            }


//        수정코드... 아직 미완성
//        binding.alarmListView.setOnItemClickListener { parent, view, position, id ->
//            val intent = Intent(this, addAlarmActivity::class.java)
//            intent.putExtra("alarmData", data[position])
//            activityResultLauncher1.launch(intent)
//        }

        binding.addAlarmBtn.setOnClickListener {
            val intent = Intent(this, addAlarmActivity::class.java)
            activityResultLauncher1.launch(intent)
        }


        // notification code
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        mCalender = GregorianCalendar()
        setContentView(binding.root)
    }

    //알람이 활성화 되면 -> 해당 함수
    private fun start() {

        var calendar = Calendar.getInstance()
//        var time = data.time.split(":")
//        var hour = time[0].toInt()
//        var minute = time[1].toInt()
//        var flag = false

        //AlarmReceiver에 값 전달 - 반복문으로 배열의 알람들을 가져옴
        val receiverIntent = Intent(this, AlarmReveiver::class.java)

        receiverIntent.apply {
            putExtra("data", newData as Serializable)
            Log.d("data Receiver", newData.toString())
            putExtra("ringtone", newData.ringTone)
            Log.d("data Receiver", newData.ringTone.toString())
        }

        calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 44)
            set(Calendar.SECOND,0)
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            PendingIntent.getBroadcast(this,0,receiverIntent,PendingIntent.FLAG_IMMUTABLE)
        }else{
            PendingIntent.getBroadcast(this,0,receiverIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
 //       alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  calendar.timeInMillis, ((AlarmManager.INTERVAL_DAY)*7), pendingIntent);
        Log.d("alarm Receiver", "send alarm to receiver")

    }

    // 화면 돌려도 list 날아가지 않도록
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        var alarmList = alarmList
        outState.putSerializable("alarmList", alarmList)
    }

}
