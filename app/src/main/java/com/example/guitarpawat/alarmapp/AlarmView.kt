package com.example.guitarpawat.alarmapp

interface AlarmView {
    fun setAlarmAdapter(adapter: ArrayList<Alarm>)
    fun updateAlarmList()
}