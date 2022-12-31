package com.example.wakemeup

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorInt

class alarmListAdapter (val context: Context,  val dateList: MutableList<AlarmDataModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return dateList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(com.example.wakemeup.R.layout.alarm_item, null)

        val timeArea = view.findViewById<TextView>(R.id.timeArea);
        val dOrN = view.findViewById<TextView>(R.id.dOrN)
        val isActivated = view.findViewById<ToggleButton>(R.id.activateBtn);
        val data = ArrayList<TextView>()

        data.add(view.findViewById<TextView>(R.id.monday))
        data.add(view.findViewById<TextView>(R.id.tuesday))
        data.add(view.findViewById<TextView>(R.id.wednesday))
        data.add(view.findViewById<TextView>(R.id.thursday))
        data.add(view.findViewById<TextView>(R.id.friday))
        data.add(view.findViewById<TextView>(R.id.saturday))
        data.add(view.findViewById<TextView>(R.id.sunday))

        timeArea.text = dateList[position].time.toString()
        dOrN.text = dateList[position].dorN.toString()
        isActivated.isChecked = dateList[position].isActivated

        for(x in 0..data.size-1) {
            if(dateList[position].dates[x]) {
                data[x].setTextColor(Color.BLACK)
            }
            else
                data[x].setTextColor(Color.WHITE)
        }

        isActivated.setOnClickListener {
            dateList[position].isActivated = isActivated.isChecked
        }

        return view
    }
}