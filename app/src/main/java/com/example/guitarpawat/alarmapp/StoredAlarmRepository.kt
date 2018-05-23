package com.example.guitarpawat.alarmapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v4.content.res.TypedArrayUtils
import android.util.Log
import java.util.*
import kotlin.math.roundToInt

class StoredAlarmRepository(context: Context): AlarmRepository() {
    companion object {
        private const val DATABASE_NAME = "AlarmClock.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "Alarms"
        private const val COLUMN_ID = "id"
        private const val COLUMN_HOUR = "hour"
        private const val COLUMN_MINUTE = "minute"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_BITS = "bits"

        private const val SQL_CREATE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_HOUR INTEGER, " +
                "$COLUMN_MINUTE INTEGER, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_BITS INTEGER)"

        private const val SQL_DROP = "DROP TABLE IF EXISTS $TABLE_NAME"

        class FeedReaderHelper(context: Context): SQLiteOpenHelper(
                context, DATABASE_NAME,null, DATABASE_VERSION) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL(SQL_CREATE)
            }

            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                db.execSQL(SQL_DROP)
                onCreate(db)
            }

            override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
                onUpgrade(db, oldVersion, newVersion)
            }

        }
    }

    protected var ids = ArrayList<Long>()

    private val dbHelper: FeedReaderHelper
    init {
        dbHelper = FeedReaderHelper(context)
    }

    private fun addToDB(alarm: Alarm): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_HOUR, alarm.hour)
            put(COLUMN_MINUTE, alarm.minute)
            put(COLUMN_DESCRIPTION, alarm.description)
            put(COLUMN_BITS, toBits(alarm))
        }

        return db.insert(TABLE_NAME, null, values)
    }

    private fun getFromDB(): Cursor {
        val db = dbHelper.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME",null)
    }

    private fun deleteFromDB(id: Long) {
//        Log.println(Log.ASSERT,"delete",id.toString())
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME " +
                "WHERE $COLUMN_ID = ?", arrayOf(id.toString()))
    }

    private fun findID(alarm: Alarm): Long {
        for(i in 0 until alarmList.size) {
            if(alarm==getAlarm(i)) {
                return ids[i]
            }
        }
        return -1
    }

    private fun updateToDB(id: Long, alarm: Alarm) {
//        Log.println(Log.ASSERT,"u-id",id.toString())
        val db = dbHelper.writableDatabase
//        val values = ContentValues().apply {
//            put(COLUMN_HOUR, alarm.hour)
//            put(COLUMN_MINUTE, alarm.minute)
//            put(COLUMN_DESCRIPTION, alarm.description)
//            put(COLUMN_BITS, toBits(alarm))
//        }
//
//        val selection = "$COLUMN_ID = ?"
//        val selectionArgs = arrayOf(id.toString())
        db.execSQL("UPDATE $TABLE_NAME SET $COLUMN_HOUR=?, $COLUMN_MINUTE=?, " +
                "$COLUMN_DESCRIPTION=?, $COLUMN_BITS=? WHERE $COLUMN_ID=$id",
                arrayOf(alarm.hour.toString(), alarm.minute.toString(),
                        alarm.description, toBits(alarm).toString()))
//        db.update(TABLE_NAME,values,selection,selectionArgs).toString()
    }

    private fun toBits(alarm: Alarm): Int {
        var bits = 0
        val days = alarm.days
        for(i in 0..6) {
            if(days[i]) {
                bits += Math.pow(2.0,i.toDouble()).roundToInt()
            }
        }
        if(alarm.active) {
            bits += Math.pow(2.0, 7.0).roundToInt()
        }
        return bits
    }

    private fun decodeBits(bits: Int): Array<Boolean> {
        var bits = bits
        var res = BooleanArray(8)
        for(i in 0..7) {
            res[i] = (bits%2 == 1)
            bits /= 2
        }
        /*res.forEach {
            Log.println(Log.ASSERT,"decode",it.toString())
        }*/
        return res.toTypedArray()
    }

    override fun loadAlarms() {
        val cursor = getFromDB()
        with(cursor) {
            while(moveToNext()) {
                //Log.println(Log.ASSERT,"load",getInt(getColumnIndex(COLUMN_ID)).toString())
                val bits = decodeBits(getInt(getColumnIndex(COLUMN_BITS)))
                val days = Arrays.copyOfRange(bits,0,7)
                //Log.println(Log.ASSERT,"bit-load",getInt(getColumnIndex(COLUMN_BITS)).toString())
                val active = bits[7]
                val hour = getInt(getColumnIndex(COLUMN_HOUR))
                val minute = getInt(getColumnIndex(COLUMN_MINUTE))
                val description = getString(getColumnIndex(COLUMN_DESCRIPTION))
                ids.add(getLong(getColumnIndex(COLUMN_ID)))
                alarmList.add(Alarm(hour, minute, active, description, days))
            }
        }
    }

    override fun addAlarm(alarm: Alarm) {
        ids.add(addToDB(alarm))
        super.addAlarm(alarm)
    }

    override fun setAlarm(alarm: Alarm, pos: Int) {
        updateToDB(ids[pos],alarm)
        super.setAlarm(alarm, pos)
    }

    override fun removeAlarm(pos: Int) {
        val id =findID(getAlarm(pos))
        deleteFromDB(id)
        ids.remove(id)
        super.removeAlarm(pos)
    }

}