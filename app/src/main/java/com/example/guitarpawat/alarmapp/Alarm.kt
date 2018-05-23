package com.example.guitarpawat.alarmapp

import android.os.Bundle

class Alarm(var hour: Int, var minute: Int, var active :Boolean = false, var description: String = "",
            days: Array<Boolean> = BooleanArray(7).toTypedArray()){
    var days = days
        private set

    companion object {
        fun extractBundle(bundle: Bundle): Alarm {
            return Alarm(bundle.getInt("hour"),bundle.getInt("minute"),bundle.getBoolean("active"),
                    bundle.getString("description"),bundle.getBooleanArray("days").toTypedArray())
        }
    }

    fun setActiveDay(day: Int, active: Boolean = true) {
        days[day] = active
    }

    fun toBundle(): Bundle {
        val bundle = Bundle()
        bundle.putInt("hour",hour)
        bundle.putInt("minute",minute)
        bundle.putBoolean("active",active)
        bundle.putString("description",description)
        bundle.putBooleanArray("days",days.toBooleanArray())
        return bundle
    }

    override fun toString(): String {
        return String.format("%s [%d:%d]",description,hour,minute)
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(!(other is Alarm)) return false
        val obj = other as Alarm
        return obj.hour==hour && obj.minute==minute &&
                obj.active==active && obj.days.contentEquals(days) &&
                obj.description==description
    }
}