package com.example.guitarpawat.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import java.util.*

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
//        Log.println(Log.ASSERT,"alarm","ringgggg!")
//        Toast.makeText(context,"RING!!!",Toast.LENGTH_LONG).show()
        var shouldAlarm = false
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        val currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        val alarm = Alarm.extractBundle(intent.getBundleExtra("alarm"))
        val days = alarm.days
        when(currentDayOfWeek) {
            Calendar.SUNDAY -> {
                if(days[0]) {
                    shouldAlarm = true
                }
            }
            Calendar.MONDAY -> {
                if(days[1]) {
                    shouldAlarm = true
                }
            }
            Calendar.TUESDAY -> {
                if(days[2]) {
                    shouldAlarm = true
                }
            }
            Calendar.WEDNESDAY -> {
                if(days[3]) {
                    shouldAlarm = true
                }
            }
            Calendar.THURSDAY -> {
                if(days[4]) {
                    shouldAlarm = true
                }
            }
            Calendar.FRIDAY -> {
                if(days[5]) {
                    shouldAlarm = true
                }
            }
            Calendar.SATURDAY -> {
                if(days[6]) {
                    shouldAlarm = true
                }
            }
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // FOR TESTING: If alarm_time < current_time not more than 2 minutes will ring.
        if(hour != alarm.hour || minute != alarm.minute) {
            shouldAlarm = false
        }

        if(shouldAlarm) {
            val intent = Intent(context, AlarmAlertActivity::class.java)
            intent.putExtra("alarm",alarm.toBundle())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val service = Intent(context, RingService::class.java)
            context.startService(service)
            intent.putExtra("service",service)
            context.startActivity(intent)
        }
    }

}