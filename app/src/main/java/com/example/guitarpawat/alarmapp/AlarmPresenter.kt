package com.example.guitarpawat.alarmapp

class AlarmPresenter(val view: AlarmView, val repository: AlarmRepository) {
    fun start() {
        repository.loadAlarms()
        updateAlarm()
    }

    fun addAlarm(alarm: Alarm) {
        repository.addAlarm(alarm)
        updateAlarm()
    }

    fun changeAlarm(alarm: Alarm, pos: Int) {
        repository.setAlarm(alarm, pos)
        updateAlarm()
    }

    fun removeAlarm(pos: Int) {
        repository.removeAlarm(pos)
        updateAlarm()
    }

    private fun updateAlarm() {
        view.setAlarmAdapter(repository.getAlarms())
        view.updateAlarmList()
    }
}