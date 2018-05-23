package com.example.guitarpawat.alarmapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.alarm_list_layout.view.*

class AlarmAdapter(context: Context?, val alarms: MutableList<Alarm>?, val resource: Int = R.layout.alarm_list_layout) : ArrayAdapter<Alarm>(context, resource, alarms) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(resource,null,true)
        val alarm = alarms!![position]

        rowView.hour.text = String.format("%02d",alarm.hour)
        rowView.minute.text = String.format("%02d",alarm.minute)

        val days = alarm.days
        val allDay = arrayOf("S","M","T","W","T","F","S")
        var s = ""
        for(i in 0..6) {
            if(days[i]) {
                s += (allDay[i]+" ")
            } else {
                s += "  "
            }
        }
        s.substring(0,s.length-1)
        rowView.day.text = s

        if(alarm.active) {
            rowView.active.text = "ON"
        } else {
            rowView.active.text = "OFF"
        }

        return rowView
    }
}