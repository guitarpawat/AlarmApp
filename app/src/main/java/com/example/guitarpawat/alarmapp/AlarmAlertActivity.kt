package com.example.guitarpawat.alarmapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_alarm_alert.*

class AlarmAlertActivity : AppCompatActivity() {

    lateinit var alarm: Alarm
    private val problem1 = Problem.generateProblem()
    private val problem2 = Problem.generateProblem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_alert)
        alarm = Alarm.extractBundle(intent.getBundleExtra("alarm"))
        alarmDescriptionTextView.text = alarm.description
        problem1Text.text = problem1.getProblem()
        problem2Text.text = problem2.getProblem()
    }

    fun onCheckButtonClick(view: View) {
        try {
            if (problem1.isAnswerMatch(problem1Answer.text.toString().toInt()) &&
                    problem2.isAnswerMatch(problem2Answer.text.toString().toInt())) {
                val service = intent.getParcelableExtra<Intent>("service")
                stopService(service)
                finishAffinity()
            } else {
                Toast.makeText(this, "Wrong Answer!", Toast.LENGTH_SHORT).show()
            }
        }catch(e: NumberFormatException) {
            Toast.makeText(this, "The answer is integer!", Toast.LENGTH_SHORT).show()
        }
    }
}
