package com.alliance.advent

import org.junit.Test

import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.math.absoluteValue

typealias Pos = Pair<Int, Int>

class Day18Test {
    @Test
    fun day18Part1() {
        var map = inputLines()
            .mapIndexed { y, line -> y to line }
            .flatMap { (y, line) -> line.mapIndexed { x, _ -> (x to y) to line[x] } }
            .toMap()
        for (i in 1..10) {
            map = map.map { (pos, value) ->
                pos to when (value) {
                    '.' -> if (neighbours(pos).count { map.getOrDefault(it, 'O') == '|' } >= 3) '|' else '.'
                    '|' -> if (neighbours(pos).count { map.getOrDefault(it, 'O') == '#' } >= 3) '#' else '|'
                    '#' -> if (
                        neighbours(pos).count { map.getOrDefault(it, 'O') == '#' } >= 1
                        && neighbours(pos).count { map.getOrDefault(it, 'O') == '|' } >= 1) '#' else '.'
                    else -> value
                }
            }.toMap()
        }

        val lumbyards = map.values.count { it == '#' }
        val wooden = map.values.count { it == '|' }
        println("Lumbyards = $lumbyards, Wooden = $wooden, Total = ${lumbyards * wooden}")
    }

    fun neighbours(pos: Pos): List<Pos> {
        val (x, y) = pos
        return listOf(
            (x-1) to (y-1),
            x to (y-1),
            (x+1) to (y-1),
            (x-1) to y,
            (x+1) to y,
            (x-1) to (y+1),
            x to (y+1),
            (x+1) to (y+1)
        )
    }

    @Test
    fun day17Part2() {
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day18.txt")?.toURI()).readLines()
    }
}
