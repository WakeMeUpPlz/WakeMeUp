package com.example.wakemeup.ringAlarm

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.print.PrintHelper
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityPopRingBinding
import java.io.IOException
import java.io.Serializable
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*
import kotlin.Int


class popRingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPopRingBinding

    companion object { //3종류 패턴 타입
        const val KEY_PATTERN_TYPE = "type"
        const val TYPE_1 = 1
        const val TYPE_2 = 2
        const val TYPE_3 = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_ring)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pop_ring)

        val sdf = SimpleDateFormat("HH : mm")
        val currentDate=sdf.format(Date())
        binding.viewTime.setText(currentDate)

        try {
            RingtonePlay.stopAudio(AlarmReceiver().media!!)
        }catch (e : Exception) {
            Log.d("sound", "stop failed")
        }

        var intent = intent
        var data = intent.getParcelableExtra<AlarmDataModel>("alarmModel") as AlarmDataModel

        val num = Num(3) //1-3사이 랜덤번호 생성함수 호출(1~3사이)
        val result = num.rand()

        binding.goPatternBtn.setOnClickListener {
            val intent = Intent(this, patternActivity::class.java)
            intent.apply {
                putExtra( "alarmModel", data as Serializable)
                putExtra(KEY_PATTERN_TYPE, result)
                startActivity(intent)
            }
        }
    }
}

class Num(val pattern: Int) { //랜덤번호 생성(1~pattern 사이)
    fun rand(): Int {
        return (1..pattern).random()
    }
}