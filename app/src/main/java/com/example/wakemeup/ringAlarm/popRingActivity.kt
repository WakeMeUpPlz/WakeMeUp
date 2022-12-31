package com.example.wakemeup.ringAlarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityAlarmListBinding
import com.example.wakemeup.databinding.ActivityPopRingBinding
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class popRingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPopRingBinding
    companion object {
        const val KEY_PATTERN_TYPE = "type"

        const val TYPE_DEFAULT = 0
        const val TYPE_WITH_INDICATOR = 1
        const val TYPE_JD_STYLE = 2
        const val TYPE_9x9 = 3
        const val TYPE_SECURE_MODE = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_ring)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pop_ring)

        val sdf = SimpleDateFormat("HH : mm")
        val currentDate=sdf.format(Date())
        binding.viewTime.setText(currentDate)

        var intent = intent
        var data = intent.getParcelableExtra<AlarmDataModel>("alarmModel") as AlarmDataModel

        binding.goPatternBtn.setOnClickListener {
            startPatternActivity(TYPE_9x9)
        }
    }

    private fun startPatternActivity(type: Int) {
        val intent = Intent(this, patternActivity::class.java)
        intent.putExtra(KEY_PATTERN_TYPE, type)
        startActivity(intent)
    }
}