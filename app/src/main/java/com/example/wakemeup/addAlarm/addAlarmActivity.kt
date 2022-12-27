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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityAddAlarmBinding


class addAlarmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAlarmBinding
    lateinit var time_picker : TimePicker
    lateinit var chosenRingtone : String
    lateinit var title : String
    lateinit var dateViewArr : ArrayList<TextView>
    lateinit var savedData : AlarmDataModel
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>

    lateinit var dateArr : ArrayList<Boolean>
    lateinit var number: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_alarm)
        time_picker = binding.alarmTimeSelector
        title = binding.titleArea.text.toString()
        dateArr = arrayListOf(false,false,false,false,false,false,false)
        chosenRingtone = ""
        number = ""

        dateViewArr = arrayListOf()
        dateViewArr.add(binding.monday)
        dateViewArr.add(binding.tuesday)
        dateViewArr.add(binding.wednesday)
        dateViewArr.add(binding.thursday)
        dateViewArr.add(binding.friday)
        dateViewArr.add(binding.saturday)
        dateViewArr.add(binding.sunday)

        activityResultLauncher1 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == RESULT_OK) {

                    // 반복 요일 색깔 변경 (색깔 변경하기 - 잘안보임)
                    dateArr = it.data?.getSerializableExtra("data") as ArrayList<Boolean>
                    Toast.makeText(this, dateArr.toString(), Toast.LENGTH_SHORT).show()

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
            activityResultLauncher1.launch(intent)
        }

        // 보류 -> 값 받아오는 코드만 바뀌는 것이라 데이터베이스 넣는 과정은 그대로 진행하면됨. 변수는 그대로 갈 예정
        binding.setSoundBtn.setOnClickListener {
            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
            childForResult.launch(intent)
        }

        // title 정하기
        binding.setTitlelayout.setOnClickListener{
            title = binding.titleArea.text.toString()
        }

        // helper 정하기
        binding.setHelperBtn.setOnClickListener{
            val intent = Intent(this, selectHelperActivity::class.java)
            activityResultLauncher2.launch(intent)
        }

        binding.saveBtn.setOnClickListener {
            save()
            val intent = Intent(this, addAlarmActivity::class.java)
            intent.apply {
                putExtra("newAlarm", savedData)
            }
            setResult(RESULT_OK, intent)
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
                            val uri =
                                result.data!!.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
                            if (uri != null) {
                                chosenRingtone = uri.toString()
                                Toast.makeText(
                                    this,
                                    "ringtone=$chosenRingtone",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {}
                    }
        }

    private fun save(){

        val doN : String
        val h_tim : Int
        var hour : String
        if(time_picker.hour < 12) {
            doN = "AM"
            h_tim = time_picker.hour
        }
        else{
            doN = "PM"
            h_tim = (time_picker.hour - 12)
        }

        if(h_tim < 10 ) {
            hour = "0"+h_tim.toString()
        }else {
            hour = h_tim.toString()
        }

        val time = hour + ":" + time_picker.minute.toString()
        savedData = AlarmDataModel(dateArr,title,number,doN,true,binding.helpActiavateBtn.isChecked,chosenRingtone,time)

    }

}

