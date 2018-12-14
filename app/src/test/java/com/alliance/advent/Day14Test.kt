package com.alliance.advent

import org.junit.Test

class Day14Test {
    @Test
    fun day14Part1() {
        val indexStart = 556061
        val recipes = mutableListOf(3, 7)
        var worker1Index = 0
        var worker2Index = 1
        while (recipes.size <= indexStart + 10) {
            val sum = recipes[worker1Index] + recipes[worker2Index]
            recipes.addAll(sum.toDigitList())
            worker1Index = (worker1Index + recipes[worker1Index] + 1) % recipes.size
            worker2Index = (worker2Index + recipes[worker2Index] + 1) % recipes.size
        }
        println(recipes.subList(indexStart, indexStart + 10).joinToString(""))
    }

    @Test
    fun day14Part2() {
        val indexStart = 556061
        var recipes = "37"
        var worker1Index = 0
        var worker2Index = 1
        //FIXME Find an effective way to calculate that one
        while (recipes.length < 10000000) {
            val sum = recipes[worker1Index].toDigit() + recipes[worker2Index].toDigit()
            recipes += sum.toString()
            worker1Index = (worker1Index + recipes[worker1Index].toDigit() + 1) % recipes.length
            worker2Index = (worker2Index + recipes[worker2Index].toDigit() + 1) % recipes.length
        }
        println(recipes.indexOf(indexStart.toString()))
    }
}

fun Char.toDigit(): Int = toInt() - '0'.toInt()
fun Int.toDigitList(): List<Int> = toString().map { it.toInt() - '0'.toInt() }