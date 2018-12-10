package com.alliance.advent

import org.junit.Test

import java.io.File
import java.util.*
import kotlin.math.absoluteValue

data class Star(val x: Int, val y: Int, val vx: Int, val vy: Int)

class Day10Test {
    @Test
    fun day10Part1() {
        var waitTime = 0
        var precedence = inputLines()
            .map { line -> """position=< *(-?\d+), *(-?\d+)> velocity=< *(-?\d+), *(-?\d+)>""".toRegex().find(line)?.destructured!! }
            .map { (x, y, vx, vy) -> Star(x.toInt(), y.toInt(), vx.toInt(), vy.toInt()) }

        while (precedence.somePointsOutside()) {
            precedence = precedence.map { (x, y, vx, vy) -> Star(x + vx, y + vy, vx, vy) }
            waitTime++
        }
        do {
            printCurrent(precedence)
            print(waitTime++)
            precedence = precedence.map { (x, y, vx, vy) -> Star(x + vx, y + vy, vx, vy) }
        } while (!precedence.somePointsOutside())
    }

    private fun printCurrent(precedence: List<Star>) {

        val maxX = precedence.map { it.x }.max()!!
        val maxY = precedence.map { it.y }.max()!!
        val points = precedence.map { (x, y, _, _) -> x to y }.toSet()
        (0..maxX).forEach { x ->
            (0..maxY).forEach { y ->
                print(if (points.contains(x to y)) "#" else " ")
            }
            println()
        }
        println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------")
    }

    @Test
    fun day10Part2() {
    }

    private fun addFreeOnesWithCurrent(
        next: TreeSet<String>,
        letters: Set<String>,
        current: MutableList<Pair<String, Int>>,
        res: String,
        precedence: List<Pair<String, String>>
    ) {
        next.addAll(
            letters
                .filter { l -> !res.contains(l) && !next.contains(l) && !current.map { it.first }.contains(l) }
                .filter { precedence.none { (start, end) -> end == it && !res.contains(start) } })
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day10.txt")?.toURI()).readLines()
    }
}

private fun List<Star>.somePointsOutside(): Boolean {
    return this.any { (x, y, _, _) -> x < 0 || y < 0 }
}
