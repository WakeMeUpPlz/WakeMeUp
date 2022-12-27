package com.example.wakemeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.addAlarm.addAlarmActivity
import com.example.wakemeup.databinding.ActivityAlarmListBinding
import kotlin.collections.ArrayList

class AlarmListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAlarmListBinding
    lateinit var newData : AlarmDataModel
    lateinit var activityResultLauncher1: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncher2: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_alarm_list)
        val arrayList : ArrayList<Boolean> = arrayListOf<Boolean>()
        var test = AlarmDataModel(
            arrayList, "title", "01051175487", "AM", true,true, "song", "08:00"
        )

        var data = arrayListOf<AlarmDataModel>(test)
        val ad = alarmListAdapter(this,data)
        binding.alarmListView.adapter = ad
        arrayList.add(true)
        arrayList.add(true)
        arrayList.add(true)
        arrayList.add(false)
        arrayList.add(true)
        arrayList.add(true)
        arrayList.add(false)

        activityResultLauncher1 =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                if (it.resultCode == RESULT_OK) {
                    //새 알람 추가하기
                    newData = it.data?.getSerializableExtra("newAlarm") as AlarmDataModel
                    data.add(newData)
                    Toast.makeText(this,"new data" + newData.toString(), Toast.LENGTH_SHORT).show()
                    ad.notifyDataSetChanged()
                }
            }


        //수정코드... 아직 미완성
//        binding.alarmListView.setOnItemClickListener { parent, view, position, id ->
//            val intent = Intent(this, addAlarmActivity::class.java)
//            intent.putExtra("alarmData", data[position])
//            activityResultLauncher1.launch(intent)
//        }



        binding.addAlarmBtn.setOnClickListener {
            val intent = Intent(this, addAlarmActivity::class.java)
            activityResultLauncher1.launch(intent)
        }

        setContentView(binding.root)

    }
}
