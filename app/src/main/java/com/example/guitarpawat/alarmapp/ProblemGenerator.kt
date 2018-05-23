package com.example.guitarpawat.alarmapp

import java.util.*

object ProblemGenerator {
    private val random = Random()

    fun generateProblem(): Problem {
        val a = random.nextInt(99) + 1
        val b = random.nextInt(99) + 1
        val operator = random.nextInt(3)
        val x = random.nextInt(3)
        val ans: Int
        val str: String
        val op: String
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
        str = when(x) {
            0 -> {
                "x $op $b = $ans"
            }
            1 -> {
                "$a $op x = $ans"
            }
            else -> {
                "$a $op $b = x"
            }
        }
        return Problem(str, ans)
    }

    class Problem(private val problem: String,private val answer: Int) {
        fun isAnswerMatch(ans: Int): Boolean {
            return ans==answer
        }
        fun getProblem(): String {
            return problem
        }
    }
}