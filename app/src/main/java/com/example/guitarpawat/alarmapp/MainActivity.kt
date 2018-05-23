package com.example.guitarpawat.alarmapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import android.widget.AdapterView
import android.content.DialogInterface
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AlertDialog
import java.util.*


class MainActivity : AppCompatActivity(), AlarmView {

    private lateinit var presenter: AlarmPresenter
    private lateinit var adapter: AlarmAdapter
    private lateinit var alarmManager: AlarmManager
    private lateinit var alarmRepository: AlarmRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmRepository = StoredAlarmRepository(this)
        presenter = AlarmPresenter(this,alarmRepository)
        presenter.start()

        alarmListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@MainActivity,SetAlarmActivity::class.java)
            intent.putExtra("pos",position)
            intent.putExtra("alarm",adapter.getItem(position).toBundle())
            startActivityForResult(intent,2)
        }

        alarmListView.onItemLongClickListener = AdapterView.OnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(this@MainActivity)
                    .setTitle("Confirmation")
                    .setMessage("Do you want to delete this alarm?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { _, _ ->
                        removeFromManager(alarmRepository.getAlarm(position))
                        presenter.removeAlarm(position)
                    })
                    .setNegativeButton(android.R.string.no, null).show()
            true
        }
    }

    override fun setAlarmAdapter(alarms: ArrayList<Alarm>) {
        adapter = AlarmAdapter(this,alarms)
        alarmListView.adapter = AlarmAdapter(this,alarms)
    }

    fun onAddAlarmButtonClicked(view: View) {
        val intent = Intent(this,SetAlarmActivity::class.java)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                val alarm = Alarm.extractBundle(data.getBundleExtra("result"))
                presenter.addAlarm(alarm)
            }
        } else if(requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                val alarm = Alarm.extractBundle(data.getBundleExtra("result"))
                val pos = data.getIntExtra("pos",-1)
                if(pos != -1) {
                    presenter.changeAlarm(alarm, pos)
                } else {
                    presenter.addAlarm(alarm)
                }
            }
        }
    }

    override fun updateAlarmList() {
        adapter.notifyDataSetChanged()
        val list = adapter.alarms
        list!!.forEach {
            if(it.active) {
                addToManager(it)
            }
        }
    }

    private fun addToManager(alarm: Alarm) {
        Log.println(Log.ASSERT,"id-add",alarmRepository.findID(alarm).toString())
        var calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
        calendar.set(Calendar.MINUTE, alarm.minute)
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("alarm",alarm.toBundle())
        val pendingIntent = PendingIntent.getBroadcast(this,alarmRepository.findID(alarm)
                .toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
    }

    private fun removeFromManager(alarm: Alarm) {
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("alarm",alarm.toBundle())
        val pendingIntent = PendingIntent.getBroadcast(this,alarmRepository.findID(alarm)
                .toInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT)
        Log.println(Log.ASSERT,"id-del",alarmRepository.findID(alarm).toString())
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }


}
