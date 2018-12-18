package com.alliance.advent

import org.junit.Test

import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.math.absoluteValue

class Day18Test {
    @Test
    fun day18Part1() {
        val lines = inputLines()
        var map = lines
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
        val lines = inputLines()
        var map = lines
            .mapIndexed { y, line -> y to line }
            .flatMap { (y, line) -> line.mapIndexed { x, _ -> (x to y) to line[x] } }
            .toMap()
        val countLines = lines.size
        val countRows = lines[0].length
        val oldOnes = mutableMapOf<String, Int>()
        for (i in 1..1000000000) {
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
            val strMap = mapToString(countRows, countLines, map)
            if (oldOnes.keys.contains(strMap)) {
                val old = oldOnes.getValue(strMap)
                val cycleLength = i - old
                val nth = old + ((1000000000 - old) % cycleLength)
                val result  = oldOnes.filter { (k, v) -> v == nth }.keys.first()
                val lumbyards = result.count { it == '#' }
                val wooden = result.count { it == '|' }
                println("Lumbyards = $lumbyards, Wooden = $wooden, Total = ${lumbyards * wooden}")
                return
            } else {
                oldOnes[strMap] = i
            }
        }

        val lumbyards = map.values.count { it == '#' }
        val wooden = map.values.count { it == '|' }
        println("Lumbyards = $lumbyards, Wooden = $wooden, Total = ${lumbyards * wooden}")
    }

    private fun mapToString(width: Int, height: Int, map: Map<Pos, Char>): String {
        return (0 until width).flatMap { x -> (0 until height).map { y -> map[x to y] } }.joinToString("")
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day18.txt")?.toURI()).readLines()
    }
}
