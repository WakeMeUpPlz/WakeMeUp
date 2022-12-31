package com.example.wakemeup.ringAlarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.MainActivity
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityPatternBinding
import com.example.wakemeup.databinding.ActivityPopRingBinding
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.KEY_PATTERN_TYPE
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.TYPE_9x9
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.TYPE_JD_STYLE
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.TYPE_SECURE_MODE
import com.example.wakemeup.ringAlarm.popRingActivity.Companion.TYPE_WITH_INDICATOR
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
        Log.d("pattern", "now in this pattern")


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
}
