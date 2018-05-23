package com.example.guitarpawat.alarmapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.util.Log
import android.widget.AdapterView
import android.content.DialogInterface
import android.support.v7.app.AlertDialog




class MainActivity : AppCompatActivity(), AlarmView {

    private lateinit var presenter: AlarmPresenter
    private lateinit var adapter: AlarmAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = AlarmPresenter(this,StoredAlarmRepository(this))
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
                    .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { _, _ -> presenter.removeAlarm(position) })
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
    }
}
