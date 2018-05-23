package com.example.guitarpawat.alarmapp

import java.util.*

class MockAlarmRepository: AlarmRepository() {
    override fun findID(alarm: Alarm): Long {
        return alarmList.indexOf(alarm).toLong()
    }

    override fun loadAlarms() {
        if(alarmList.size == 0) {
            alarmList.add(Alarm(6,30,true, days = booleanArrayOf(false,true,true,true,true,true,false).toTypedArray()))
            alarmList.add(Alarm(6,45,false, days = booleanArrayOf(true,false,false,false,false,false,true).toTypedArray()))
        }
    }




}