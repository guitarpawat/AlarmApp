package com.example.guitarpawat.alarmapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_set_alarm.*
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.widget.TextView
import com.example.guitarpawat.alarmapp.R.id.timePicker
import android.os.Build
import android.util.Log
import kotlinx.android.synthetic.main.alarm_list_layout.*


class SetAlarmActivity : AppCompatActivity(){

    lateinit var alarm: Alarm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_alarm)
        timePicker.setIs24HourView(true)
        if(intent.hasExtra("alarm")) {
            val bundle = intent.extras.getBundle("alarm")
            alarm = Alarm.extractBundle(bundle)
            timePicker.hour = alarm.hour
            timePicker.minute = alarm.minute
        } else {
            alarm = Alarm(0,0,false)
        }
        description.setText(alarm.description)
        activeSwitch.isChecked = alarm.active
        updateDays()
    }

    fun onSetButtonClicked(view: View) {
        alarm.hour = timePicker.hour
        alarm.minute = timePicker.minute
        alarm.description = description.text.toString()
        alarm.active = activeSwitch.isChecked
        val returnIntent = Intent()
        returnIntent.putExtra("result", alarm.toBundle())
        if(intent.hasExtra("pos")) {
            returnIntent.putExtra("pos",intent.getIntExtra("pos",-1))
        }
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    fun onCancelButtonClicked(view: View) {
        val returnIntent = Intent()
        setResult(Activity.RESULT_CANCELED, returnIntent)
        finish()
    }

    fun onSundayButtonClicked(view: View) {
        changeDay(0,sundayInfo.text.toString())
    }

    fun onMondayButtonClicked(view: View) {
        changeDay(1,mondayInfo.text.toString())
    }

    fun onTuesdayButtonClicked(view: View) {
        changeDay(2,tuesdayInfo.text.toString())
    }

    fun onWednesdayButtonClicked(view: View) {
        changeDay(3,wednesdayInfo.text.toString())
    }

    fun onThursdayButtonClicked(view: View) {
        changeDay(4,thursdayInfo.text.toString())
    }

    fun onFridayButtonClicked(view: View) {
        changeDay(5,fridayInfo.text.toString())
    }

    fun onSaturdayButtonClicked(view: View) {
        changeDay(6,saturdayInfo.text.toString())
    }

    private fun changeDay(pos: Int, active: String) {
        alarm.setActiveDay(pos,!active.equals("ON",true))
        updateDays()
    }

    private fun updateDays() {
        val days = alarm.days
        updateDay(sundayInfo,days[0])
        updateDay(mondayInfo,days[1])
        updateDay(tuesdayInfo,days[2])
        updateDay(wednesdayInfo,days[3])
        updateDay(thursdayInfo,days[4])
        updateDay(fridayInfo,days[5])
        updateDay(saturdayInfo,days[6])
    }

    private fun updateDay(t: TextView, active: Boolean) {
        if(active) {
            t.text = "ON"
            t.setTextColor(Color.rgb(0,127,0))
        } else {
            t.text = "OFF"
            t.setTextColor(Color.RED)
        }
    }
}
