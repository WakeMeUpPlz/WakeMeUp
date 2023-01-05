package com.example.wakemeup

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.addAlarm.addAlarmActivity
import com.example.wakemeup.auth.AuthData
import com.example.wakemeup.databinding.ActivityAlarmListBinding
import com.example.wakemeup.retrofit.AlarmData
import com.example.wakemeup.retrofit.DBAlarmDataModel
import com.example.wakemeup.retrofit.ResponseData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


class AlarmListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlarmListBinding
    lateinit var newData : AlarmDataModel
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    var alarmList : ArrayList<AlarmDataModel> = arrayListOf<AlarmDataModel>()
    lateinit var alarmManager: AlarmManager
    private var mCalender: GregorianCalendar? = null
    var notificationManager: NotificationManager? = null
    var position : Int = 0
    var ad : alarmListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_list)
        var number_id : Int = 0
        val listview = binding.alarmListView

        if(intent.getStringExtra("listItem_code")!=null && intent.getStringExtra("listItem_code").equals("request")) {
            var data = intent.getSerializableExtra("alarmList") as List<AlarmData>
            for(x in data) {
                alarmList.add(convertDataToAlarmDataModel(x))
            }
        }
        ad = alarmListAdapter(this, alarmList)
        // notification code
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        mCalender = GregorianCalendar()
        if(alarmList.size == 0) {
            number_id = 0
        }else {
            number_id = alarmList[alarmList.size-1].id +1
        }

        // 알람 수정
        activityResultLauncher2 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == RESULT_OK) {
                    // 알람 수정하기
                    newData = it.data?.getSerializableExtra("newAlarm") as AlarmDataModel
                    alarmList[position]= newData
                    var dataToDB = convertDataToAlarmData(newData)
                    updateData(dataToDB)
                    ad?.notifyDataSetChanged()
                }
            }
        // 한번 클릭시 수정 -> activityResultLauncher2
        listview.adapter = ad
        listview.setOnItemClickListener { parent, view, position, id ->
            Log.d("CLICK", "OnClickListener")
            val intent = Intent(this, addAlarmActivity::class.java)
            intent.putExtra("request", "modify")
            intent.putExtra("beModifiedData", alarmList[position] as Serializable)
            intent.putExtra("number", number_id)
            this.position = position
            activityResultLauncher2.launch(intent)
        }



        // 꾹 누르면 삭제할 수 있는 다이얼로그 생성
        listview.setOnItemLongClickListener(OnItemLongClickListener { parent, v, position, id ->
            Log.d("CLICK", "OnLongClickListener")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("알람 삭제").setMessage("해당 알람을 삭제 하시겠습니까?")
            builder.setPositiveButton(
                "삭제"
            ) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    "삭제" + alarmList[position].id,
                    Toast.LENGTH_LONG
                ).show()
                deleteData(alarmList[position].id)
                alarmList.removeAt(position)
                ad?.notifyDataSetChanged()
            }
            builder.setNegativeButton(
                "삭제안함"
            ) { dialog, which ->
                Toast.makeText(
                    applicationContext,
                    "삭제하지 않습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
            builder.show()
            true
        })

        // 알람 추가
        activityResultLauncher1 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == RESULT_OK) {
                    //새 알람 추가하기
                    newData = it.data?.getSerializableExtra("newAlarm") as AlarmDataModel
                    alarmList.add(newData)
                    var dataToDB = convertDataToAlarmData(newData)
                    putData(dataToDB)
                    number_id ++
                    ad?.notifyDataSetChanged()
                }
            }

        binding.addAlarmBtn.setOnClickListener {
            val intent = Intent(this, addAlarmActivity::class.java)
            intent.putExtra("number", number_id)
            Log.d("number", number_id.toString())
            activityResultLauncher1.launch(intent)
        }

        setContentView(binding.root)
    }

    // DB에서 데이터 받아와서 현재 클라이언트에서 사용하는 알람모델로 만들어줌
    fun convertDataToAlarmDataModel(Data : AlarmData) : AlarmDataModel {

        var dates : ArrayList<Boolean> = ArrayList()
        for(x in 0..6) {
            if(Data.dates[x] == 'T') {
                dates.add(true)
            }else
                dates.add(false)
        }
        var isActivated = false
        var isHelperActivated = false
        var ringtone = Uri.EMPTY

        if(Data.isActivated.equals(1)) {
            isActivated = true
        }
        if(Data.isHelperActivated.equals(1)) {
            isHelperActivated =  true
        }
        if (Data.ringTone==null) {
            ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        }else {
            ringtone = Uri.parse(Data.ringTone)
        }
        var dataModel  = AlarmDataModel(Data.alarm_id, dates, Data.title, Data.helper, Data.dorN, isActivated, isHelperActivated,ringtone,Data.time)

        return dataModel
    }

    // 현재 클라이언트에서 사용하는 알람모델을 DB용으로 바꿔줌
    fun convertDataToAlarmData(Data : AlarmDataModel) : AlarmData {

        var dates : String = ""
        var isActivated : Int = 0
        var isHelperActiavted : Int = 0

        for(x in Data.dates) {
            if(x) {
                dates = dates + "T"
            }else
                dates = dates + "F"
        }

        if(Data.isActivated) {
            isActivated = 1
        }else {
            isActivated = 0
        }

        if(Data.isHelperActivated) {
            isHelperActiavted = 1
        }else {
            isHelperActiavted = 0
        }

        if(Data.isActivated) {
            isActivated = 1
        }
        if(Data.isHelperActivated) {
            isHelperActiavted =  1
        }

        var ringtone = Data.ringTone.toString()
        if (Data.ringTone==null) {
            ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()
        }

        var dataModel  = AlarmData(Data.id, Data.time, Data.title, ringtone, Data.helper, dates, isActivated, isHelperActiavted,Data.dorN)
        return dataModel
    }


    // 알람넣기
    fun putData(Data : AlarmData){
        AuthData().service.alarmRegsiter(Data.alarm_id, Data.title, Data.helper, Data.dorN, Data.isActivated, Data.isHelperActivated, Data.ringTone,LoginActivity.preferences.getId("id"),Data.time, Data.dates)?.enqueue(object :
            Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: ResponseData? = response.body()
                    Log.d("YMC", "아이디: " + LoginActivity.preferences.getId("id"));
                    Log.d("YMC", "알람추가 접근 성공: " + result.toString());
                    if(result!!.success.equals("true")) {
                        Log.d("YMC", "알람추가 성공: " + result.toString());
                    }
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Toast.makeText(baseContext, "알람 추가 실패 - 서버 불안정", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
                Toast.makeText(baseContext, "인터넷이 불안정합니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 알람삭제
    fun deleteData(AlarmNum : Int){

        AuthData().service.alarmDelete(AlarmNum, LoginActivity.preferences.getId("id"))?.enqueue(object :
            Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: ResponseData? = response.body()
                    Log.d("YMC", "알람삭제 접근 성공: " + result.toString());
                    if(result!!.success.equals("true")) {
                        Log.d("YMC", "알람삭제 성공: " + result.toString());
                    }
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Toast.makeText(baseContext, "알람 삭제 실패 - 서버 불안정", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
                Toast.makeText(baseContext, "인터넷이 불안정합니다.", Toast.LENGTH_SHORT).show()

            }
        })
    }

    // 알람 수정
    fun updateData(Data : AlarmData){

        AuthData().service.alarmUpdate(Data.alarm_id, Data.title, Data.helper, Data.dorN, Data.isActivated, Data.isHelperActivated, Data.ringTone,LoginActivity.preferences.getId("id"), Data.time, Data.dates)?.enqueue(object :
            Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if(response.isSuccessful){
                    // 정상적으로 통신이 성공된 경우
                    var result: ResponseData? = response.body()
                    Log.d("YMC", "알람 수정 접근 성공: " + result.toString());
                    if(result!!.success.equals("true")) {
                        Log.d("YMC", "알람 수정 성공: " + result.toString());
                    }
                }else{
                    // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                    Toast.makeText(baseContext, "알람 수정 실패 - 서버 불안정", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
                Toast.makeText(baseContext, "인터넷이 불안정합니다.", Toast.LENGTH_SHORT).show()

            }
        })
    }

}
