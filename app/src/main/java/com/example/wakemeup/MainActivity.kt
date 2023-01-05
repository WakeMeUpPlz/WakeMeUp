package com.example.wakemeup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.addAlarm.addAlarmActivity
import com.example.wakemeup.databinding.ActivityAddAlarmBinding
import com.example.wakemeup.databinding.ActivityMainBinding
import com.example.wakemeup.ringAlarm.Num
import com.example.wakemeup.ringAlarm.patternActivity
import com.example.wakemeup.ringAlarm.popRingActivity
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    companion object { //3종류 패턴 타입
        const val KEY_PATTERN_TYPE = "type"
        const val TYPE_1 = 1
        const val TYPE_2 = 2
        const val TYPE_3 = 3
    }


    private lateinit var binding : ActivityMainBinding
    var time by Delegates.notNull<Int>()
    var minute by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        binding.goAddAlarm.setOnClickListener {
            val intent = Intent(this, AlarmListActivity::class.java)
            startActivity(intent)
        }


        val num = Num(3) //1-3사이 랜덤번호 생성함수 호출(1~3사이)
        val result = num.rand()

        if (result == 1)    //patternlockactivity로 넘어가서 패턴타입 호출
            startPatternActivity(popRingActivity.TYPE_1)
        else if (result ==2)
            startPatternActivity(popRingActivity.TYPE_2)
        else
            startPatternActivity(popRingActivity.TYPE_3)

    }

    private fun startPatternActivity(type: Int) { //xml 시작
        val intent = Intent(this, patternActivity::class.java)
        intent.putExtra(popRingActivity.KEY_PATTERN_TYPE, type)
        startActivity(intent)
    }
}


class Num(val pattern: Int) { //랜덤번호 생성(1~pattern 사이)
    fun rand(): Int {
        return (1..pattern).random()
    }
}