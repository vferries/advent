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
        val num = 556061.toString()
        val start = "37"
        val elves = mutableListOf(0, 1)
        val recipes = StringBuilder(30000000)
        recipes.append(start)

        while(true) {
            recipes(recipes, elves)

            val lastBit = if(recipes.length > 10) recipes.subSequence(recipes.length - 10, recipes.length) else ""

            if(lastBit.contains(num)) {
                println(recipes.length - 10 + lastBit.indexOf(num) )
                return
            }
        }
    }

    private fun recipes(recipes: StringBuilder, elves: MutableList<Int>) {
        recipes.append(elves.indices.map { recipes[elves[it]] - '0' }.sum())
        elves[0] = ((elves[0] + (recipes[elves[0]] - '0') + 1) % recipes.length)
        elves[1] = ((elves[1] + (recipes[elves[1]] - '0') + 1) % recipes.length)
    }
}

fun Char.toDigit(): Int = toInt() - '0'.toInt()
fun Int.toDigitList(): List<Int> = toString().map { it.toInt() - '0'.toInt() }