package com.example.wakemeup.addAlarm

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.TimePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.AlarmListActivity
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityAddAlarmBinding
import java.io.Serializable


class addAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlarmBinding
    lateinit var time_picker : TimePicker
    lateinit var chosenRingtone : Uri
    lateinit var title : String
    lateinit var dateViewArr : ArrayList<TextView>
    lateinit var savedData : AlarmDataModel
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>
    lateinit var dateArr : ArrayList<Boolean>
    lateinit var number: String
    var alarm_id : Int = 0
    var isActivated : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm)
        time_picker = binding.alarmTimeSelector
        title = binding.titleArea.text.toString()
        dateArr = arrayListOf(false,false,false,false,false,false,false)
        // 설정 안하면 기본 음 재생
        chosenRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        number = ""
        isActivated = true
        dateViewArr = arrayListOf()
        dateViewArr.add(binding.monday)
        dateViewArr.add(binding.tuesday)
        dateViewArr.add(binding.wednesday)
        dateViewArr.add(binding.thursday)
        dateViewArr.add(binding.friday)
        dateViewArr.add(binding.saturday)
        dateViewArr.add(binding.sunday)

        alarm_id = intent.getIntExtra("number", -1) as Int


        if(intent.getStringExtra("request")!=null && intent.getStringExtra("request").equals("modify")) {
            var tobeModifiedData = intent.getSerializableExtra("beModifiedData") as AlarmDataModel
            var hour = tobeModifiedData.time.split(":")[0]
            if(tobeModifiedData.dorN == "PM") {
                if(hour.toInt() != 12) {
                    time_picker.hour = hour.toInt() + 12
                }else {
                    time_picker.hour = hour.toInt()
                }
            }else {
                if(hour.toInt() == 12) {
                    time_picker.hour = 0
                }else {
                    time_picker.hour = hour.toInt()
                }
            }

            var minute = tobeModifiedData.time.split(":")[1]
            time_picker.minute = minute.toInt()
            binding.titleArea.setText(tobeModifiedData.title)
            number = tobeModifiedData.helper
            chosenRingtone = tobeModifiedData.ringTone
            alarm_id = tobeModifiedData.id
            dateArr = tobeModifiedData.dates
            isActivated = tobeModifiedData.isActivated
            binding.helpActiavateBtn.isChecked = tobeModifiedData.isHelperActivated
            binding.selectedHelper.setText(tobeModifiedData.helper)

            for(i in 0..dateArr!!.size-1) {
                if(dateArr!!.get(i))
                    dateViewArr[i].setTextColor(Color.BLACK)
                else
                    dateViewArr[i].setTextColor(Color.rgb(160, 133, 133))
            }
        }


        activityResultLauncher1 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == RESULT_OK) {
                    // 반복 요일 색깔 변경 (색깔 변경하기 - 잘안보임)
                    dateArr = it.data?.getSerializableExtra("data") as ArrayList<Boolean>

                    for(i in 0..dateArr!!.size-1) {
                        if(dateArr!!.get(i))
                            dateViewArr[i].setTextColor(Color.BLACK)
                        else
                            dateViewArr[i].setTextColor(Color.rgb(160, 133, 133))
                    }

                }
            }

        activityResultLauncher2 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == RESULT_OK) {
                    // 기상도우미 번호 받아오기 및 설정 - 이름없이 번호만 입력하는 경우도 있어서 걍 번호만 받아옴
                    number = it.data?.getStringExtra("number") as String
                    binding.selectedHelper.text = number
                }
            }

        // 완료
        binding.selectorDayBtn.setOnClickListener {
            val intent = Intent(this, dateSelectActivity::class.java)
            intent.apply {
                putExtra("selected dates", dateArr)
            }
            activityResultLauncher1.launch(intent)
        }

        // 보류 -> 값 받아오는 코드만 바뀌는 것이라 데이터베이스 넣는 과정은 그대로 진행하면됨. 변수는 그대로 갈 예정
        binding.setSoundBtn.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)

            childForResult.launch(intent)
        }

        // title 정하기
        binding.setTitlelayout.setOnClickListener{
            title = binding.titleArea.text.toString()
        }

        // helper 정하기
        binding.setHelperBtn.setOnClickListener{
            val intent = Intent(this, selectHelperActivity::class.java)
            intent.apply {
                putExtra("selected_helper", number)
            }
            activityResultLauncher2.launch(intent)
        }

        binding.saveBtn.setOnClickListener {
            save()
            val intent = Intent(this, AlarmListActivity::class.java)
            intent.apply {
                putExtra("newAlarm", savedData as Serializable)
            }
            setResult(RESULT_OK, intent)
            Log.d("alarm Receiver", "save new alarm")
            finish()
        }

        binding.exitFromAddAlarm.setOnClickListener {
            finish()
        }

        setContentView(binding.root)

    }

    // 키보드 내리기 함수
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return true
    }

    private val childForResult =
        registerForActivityResult (ActivityResultContracts.StartActivityForResult()){
                result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    var uri = result.data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                    if (uri != null) {
                                chosenRingtone = uri
                            }
                        }
                        else -> {}
                    }
        }

    private fun save(){
        val doN : String
        var h_tim : Int
        var hour : String
        var minute : String
        title = binding.titleArea.text.toString()

        if(time_picker.hour < 12) {
            doN = "AM"
            if(time_picker.hour == 0) {
                h_tim = 12
            }else {
                h_tim = time_picker.hour
            }
        }

        else {
            doN = "PM"
            h_tim = (time_picker.hour - 12)
            if(h_tim == 0) {
                h_tim = 12
            }
        }

        if(h_tim < 10 ) {
            hour = "0"+h_tim.toString()
        }else {
            hour = h_tim.toString()
        }

        if(time_picker.minute < 10) {
            minute = "0"+time_picker.minute.toString()
        }else
            minute = time_picker.minute.toString()

        val time = hour + ":" + minute
        savedData = AlarmDataModel(alarm_id,dateArr,title,number,doN,true,binding.helpActiavateBtn.isChecked,chosenRingtone,time)
    }

}

