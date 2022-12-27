package com.example.wakemeup.addAlarm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.wakemeup.MainActivity


class dateSelectorRadioButtonAdapter (val context: Context, val radio_button : Int, val dateList: MutableList<String>) : BaseAdapter() {
    var selectedIndex = -1

    override fun getCount(): Int {
        return dateList.size
    }

    override fun getItem(position: Int): Any {
        return dateList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getSelectedIndex(position: Int) {
        selectedIndex = position
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View = LayoutInflater.from(context).inflate(radio_button, null)
        val dateArea = view.findViewById<TextView>(com.example.wakemeup.R.id.date)
        val checkArea = view.findViewById<CheckBox>(com.example.wakemeup.R.id.isChecked)
        val activity : dateSelectActivity
        activity = context as dateSelectActivity

        checkArea.setOnClickListener {
//            Toast.makeText(context, "this", Toast.LENGTH_SHORT).show()
            if(activity.arrayList.size > 0) {
                if(checkArea.isChecked){
                    activity.arrayList[position] = true
                }else {
                    activity.arrayList[position] = false
                }
            }
        }

        val date = dateList[position]
        dateArea.text = date
        return view
    }

}