package com.example.wakemeup.addAlarm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.wakemeup.MainActivity
import com.example.wakemeup.R
import com.example.wakemeup.databinding.ActivityDateSelectBinding

class dateSelectActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDateSelectBinding
    lateinit var arrayList : ArrayList<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var dateList = mutableListOf<String>("월요일 마다", "화요일 마다", "수요일 마다", "목요일 마다", "금요일 마다", "토요일 마다", "일요일 마다")
        arrayList = intent.getSerializableExtra("selected dates") as ArrayList<Boolean>
        binding = DataBindingUtil.setContentView(this, R.layout.activity_date_select)
        var adapter = dateSelectorRadioButtonAdapter(this, R.layout.date_selector_radio_button_layout, dateList, arrayList)
        binding.listview.adapter = adapter


        binding.listview.setOnItemClickListener { parent, view, position, id ->
            adapter.notifyDataSetChanged()
        }

        binding.exitToAddAlarm.setOnClickListener {
            finish()
        }

        binding.saveBtn.setOnClickListener {
            val intent = Intent(this, addAlarmActivity::class.java)
            intent.apply {
                putExtra("data", arrayList)
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        setContentView(binding.root)

    }


}