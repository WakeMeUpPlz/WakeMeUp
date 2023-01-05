package com.example.wakemeup.ringAlarm

import android.Manifest.permission_group.SMS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.MainActivity
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityPatternBinding
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.KEY_PATTERN_TYPE
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.TYPE_1
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.TYPE_2
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.TYPE_3
import com.itsxtt.patternlock.PatternLockView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class patternActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPatternBinding
    var count : Int = 0
    private lateinit var countText : TextView
    var data : AlarmDataModel? = null
    var number : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = intent.getParcelableExtra<AlarmDataModel>("alarmModel") as AlarmDataModel
        var type = intent.getIntExtra(popRingActivity.KEY_PATTERN_TYPE, popRingActivity.TYPE_1)
        number = data!!.helper

        when(type) {
            popRingActivity.TYPE_1 -> { //1번패턴 실행
                setContentView(R.layout.activity_pattern1)
                findViewById<com.itsxtt.patternlock.PatternLockView>(R.id.Pattern1LockView).setOnPatternListener(listener1)
                countText = findViewById<TextView>(R.id.count)
                val sdf = SimpleDateFormat("HH : mm")
                val currentDate=sdf.format(Date())
                findViewById<TextView>(R.id.viewTime).setText(currentDate)
            }
            popRingActivity.TYPE_2 -> { //2번패턴 실행
                setContentView(R.layout.activity_pattern2)
                findViewById<com.itsxtt.patternlock.PatternLockView>(R.id.Pattern2LockView).setOnPatternListener(listener2)
                countText = findViewById<TextView>(R.id.count)
                val sdf = SimpleDateFormat("HH : mm")
                val currentDate=sdf.format(Date())
                findViewById<TextView>(R.id.viewTime).setText(currentDate)
            }
            popRingActivity.TYPE_3 -> { //3번패턴 실행
                setContentView(R.layout.activity_pattern3)
                findViewById<com.itsxtt.patternlock.PatternLockView>(R.id.Pattern3LockView).setOnPatternListener(listener3)
                countText = findViewById<TextView>(R.id.count)
                val sdf = SimpleDateFormat("HH : mm")
                val currentDate=sdf.format(Date())
                findViewById<TextView>(R.id.viewTime).setText(currentDate)
            }
        }

    }

    private fun sendMail(number : String) {
        try {
            val smsManager:SmsManager
            if (Build.VERSION.SDK_INT>=23) {
                smsManager = this.getSystemService(SmsManager::class.java)
            }
            else{
                smsManager = SmsManager.getDefault()
            }
            smsManager.sendTextMessage(number, null, "깨워줘", null, null)
            Toast.makeText(applicationContext, "Message Sent", Toast.LENGTH_LONG).show()
            ActivityCompat.finishAffinity(this)
            System.exit(0)
        } catch (e: Exception) {
            Log.d("tis", e.message.toString())
            Toast.makeText(applicationContext, "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG)
                .show()
        }
    }

    private var listener1  = object : PatternLockView.OnPatternListener {
        override fun onStarted() {
            super.onStarted()

        }
        override fun onProgress(ids: ArrayList<Int>) {
            super.onProgress(ids)
        }
        override fun onComplete(ids: ArrayList<Int>): Boolean {
            var isCorrect = TextUtils.equals("03642", getPatternString(ids)) //1번패턴 답 03642
            var tip: String
            if (isCorrect) {
                tip = "correct"
                ActivityCompat.finishAffinity(this@patternActivity)
                System.exit(0)
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                tip = "틀렸습니다. 다시 입력하세요"
                count++      //틀렸을경우 횟수증가
                countText.setText(count.toString())
            }
            if(count== 5) {
                if(!data!!.isHelperActivated || data!!.helper.length == 0) {
                    moveTaskToBack(true);
                }else{
                    if(data!!.helper.length != 0) {
                        sendMail(data!!.helper)
                        moveTaskToBack(true);
                    }
                }
            }
            Toast.makeText(this@patternActivity, tip, Toast.LENGTH_SHORT).show()
            return isCorrect
        }
    }

    private var listener2  = object : PatternLockView.OnPatternListener {

        override fun onStarted() {
            super.onStarted()
        }

        override fun onProgress(ids: ArrayList<Int>) {
            super.onProgress(ids)
        }

        override fun onComplete(ids: ArrayList<Int>): Boolean {
            var isCorrect = TextUtils.equals("0124678", getPatternString(ids))
            var tip: String
            if (isCorrect) {
                tip = "correct"
                ActivityCompat.finishAffinity(this@patternActivity)
                System.exit(0)
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                tip = "틀렸습니다. 다시 입력하세요"
                count++      //틀렸을경우 횟수증가
                countText.setText(count.toString())
            }
            if(count== 5) {
                if(!data!!.isHelperActivated || data!!.helper.length == 0) {
                    moveTaskToBack(true);
                }else{
                    if(data!!.helper.length != 0) {
                        sendMail(data!!.helper)
                        moveTaskToBack(true);
                    }
                }
            }
            Toast.makeText(this@patternActivity, tip, Toast.LENGTH_SHORT).show()
            return isCorrect
        }
    }

    private var listener3  = object : PatternLockView.OnPatternListener {

        override fun onStarted() {
            super.onStarted()
        }

        override fun onProgress(ids: ArrayList<Int>) {
            super.onProgress(ids)
        }

        override fun onComplete(ids: ArrayList<Int>): Boolean {
            var isCorrect = TextUtils.equals("01258", getPatternString(ids))
            var tip: String
            if (isCorrect) {
                tip = "correct"
                ActivityCompat.finishAffinity(this@patternActivity)
                System.exit(0)
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                tip = "틀렸습니다. 다시 입력하세요"
                count++      //틀렸을경우 횟수증가
                countText.setText(count.toString())
            }
            if(count==5) {
                if(!data!!.isHelperActivated || data!!.helper.length == 0) {
                    moveTaskToBack(true);
                }else{
                    if(data!!.helper.length != 0) {
                        sendMail(data!!.helper)
                        moveTaskToBack(true);
                    }
                }
            }
            Toast.makeText(this@patternActivity, tip, Toast.LENGTH_SHORT).show()
            return isCorrect
        }
    }

    private fun getPatternString(ids: ArrayList<Int>) : String { //사용자가 그린 패턴 읽어옴
        var result = ""
        for (id in ids) {
            result += id.toString()
        }
        return result
    }
}
