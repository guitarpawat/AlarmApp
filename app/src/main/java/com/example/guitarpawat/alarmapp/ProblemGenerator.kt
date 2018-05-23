package com.example.guitarpawat.alarmapp

import android.util.Log
import java.util.*

class Problem private constructor(private val problem: String,private val answer: Int) {

    companion object {
        private val random = Random()
        fun generateProblem(): Problem {
            val a = random.nextInt(99) + 1
            val b = random.nextInt(99) + 1
            val operator = random.nextInt(3)
            val x = random.nextInt(3)
            val ans: Int
            val str: String
            val op: String
            val res: Int
            when(operator) {
                0 -> {
                    ans = a+b
                    op = "+"
                }
                1 -> {
                    ans = a-b
                    op = "-"
                }
                else -> {
                    ans = a*b
                    op = "*"
                }
            }
            when(x) {
                0 -> {
                    str = "x $op $b = $ans"
                    res = a
                }
                1 -> {
                    str = "$a $op x = $ans"
                    res = b
                }
                else -> {
                    str = "$a $op $b = x"
                    res = ans
                }
            }
            return Problem(str, res)
        }
    }

    fun isAnswerMatch(ans: Int): Boolean {
        return ans==answer
    }
    fun getProblem(): String {
//        Log.println(Log.ASSERT,"ans",answer.toString())
        return problem
    }


}