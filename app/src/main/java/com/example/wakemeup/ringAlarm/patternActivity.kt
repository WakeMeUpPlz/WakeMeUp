package com.example.wakemeup.ringAlarm

import android.Manifest.permission_group.SMS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.AlarmDataModel
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityPatternBinding
import com.itsxtt.patternlock.PatternLockView
import kotlin.properties.Delegates

class patternActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPatternBinding
    private lateinit var correct : ArrayList<Int>
    private var isPatternCorrect by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pattern)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pattern)
        isPatternCorrect = true
        var data = intent.getParcelableExtra<AlarmDataModel>("alarmModel") as AlarmDataModel
        Log.d("pattern", "now in this pattern")

        // 패턴 풀거나 횟수 초과시 소리 사라짐
        RingtonePlay.stopAudio()
        sendMail("01051175487")

//        if(!data.isHelperActivated || data.helper.length == 0) {
//            // 이때 어떡하지 ;;; 생각못함 그냥 계속 알람 울리게 둘까...?
//        }else{
//            if(data.helper.length != 0) {
////                sendMail(data.helper)
//            }
//        }

        binding.ninePatternLockView.setOnPatternListener(object : PatternLockView.OnPatternListener {

            override fun onStarted() {
                super.onStarted()
                Log.d("pattern", "now in pattern")

            }

            override fun onProgress(ids: ArrayList<Int>) {
                super.onProgress(ids)
                Log.d("pattern", "now in progress")
            }

            override fun onComplete(ids: ArrayList<Int>): Boolean {
                var isCorrect = TextUtils.equals("012", getPatternString(ids))
                var tip: String
                if (isCorrect) {
                    tip = "correct:" + getPatternString(ids)
                } else {
                    tip = "error:" + getPatternString(ids)
                }

                Toast.makeText(this@patternActivity, tip, Toast.LENGTH_SHORT).show()
                return isCorrect
            }
        })
    }

    private fun getPatternString(ids: ArrayList<Int>) : String {
        var result = ""
        for (id in ids) {
            result += id.toString()
        }
        return result
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
        } catch (e: Exception) {
            Log.d("tis", e.message.toString())
            Toast.makeText(applicationContext, "Please enter all the data.."+e.message.toString(), Toast.LENGTH_LONG)
                .show()
        };


    }
}
