package com.example.guitarpawat.alarmapp

import android.support.annotation.CallSuper
import java.util.ArrayList

abstract class AlarmRepository {
    protected var alarmList: ArrayList<Alarm> = ArrayList()

    abstract fun loadAlarms()

    @CallSuper
    open fun addAlarm(alarm: Alarm) {
        alarmList.add(alarm)
    }

    @CallSuper
    open fun getAlarms(): ArrayList<Alarm> {
        return alarmList
    }

    @CallSuper
    open fun getAlarm(pos: Int): Alarm {
        return alarmList[pos]
    }

    @CallSuper
    open fun setAlarm(alarm: Alarm, pos: Int) {
        alarmList[pos] = alarm
    }

    @CallSuper
    open fun removeAlarm(pos: Int) {
        alarmList.removeAt(pos)
    }

    abstract fun findID(alarm: Alarm): Long
}