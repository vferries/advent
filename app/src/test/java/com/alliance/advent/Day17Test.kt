package com.alliance.advent

import org.junit.Test

import java.io.File
import java.util.*
import java.util.regex.Pattern
import kotlin.math.absoluteValue

class Day17Test {
    @Test
    fun day17Part1() {
        val map = inputLines()
            .flatMap { line ->
                val xRange = """y=(\d+), x=(\d+)..(\d+)""".toRegex()
                if (xRange.matches(line)) {
                    val (y, x1, x2) = xRange.find(line)?.destructured!!
                    (x1.toInt()..x2.toInt()).map { x -> Pair(x to y.toInt(), '#') }
                } else {
                    val yRange = """x=(\d+), y=(\d+)..(\d+)""".toRegex()
                    val (x, y1, y2) = yRange.find(line)?.destructured!!
                    (y1.toInt()..y2.toInt()).map { y -> Pair(x.toInt() to y, '#') }
                }
            }.toMap()
        val xs = map.keys.map(Pair<Int, Int>::first)
        val ys = map.keys.map(Pair<Int, Int>::second)
        val minX = xs.min()!!
        val maxX = xs.max()!!
        val minY = ys.min()!!
        val maxY = ys.max()!!
        // Construct as stringList
        var stringMap = mutableListOf<String>()
        for (y in (0..(maxY+1))) {
            var line = ""
            for (x in ((minX - 1)..(maxX + 1))) {
                line += map.getOrDefault(x to y, ' ')
            }
            stringMap.add(line)
        }
        val sources = Stack<Pair<Int, Int>>()
        var lastCount: Int
        var newCount = 0
        do {
            sources.add((501 - minX) to 0)
            stringMap = stringMap.map { line -> line.replace("|", " ") }.toMutableList()
            while (sources.isNotEmpty()) {
                val (x, y) = sources.pop()
                if (y <= maxY && stringMap[y][x] != '#' && stringMap[y][x] != '~' && stringMap[y][x] != '|') {
                    val line = stringMap[y].toMutableList()
                    line[x] = '|'
                    stringMap[y] = line.joinToString("")
                    if (stringMap[y + 1][x] == '#' || stringMap[y + 1][x] == '~') {
                        sources.add((x - 1) to y)
                        sources.add((x + 1) to y)
                    } else {
                        sources.add(x to (y + 1))
                    }
                }
            }
            stringMap = stringMap.map(this::replace).toMutableList()
            lastCount = newCount
            newCount = stringMap.map { line -> line.filter { c -> c == '~' || c == '|' }.length }.sum()
        } while (lastCount != newCount)
        for (line in stringMap) { println(line) }
        println((minY..maxY).map { stringMap[it].count { c -> c == '~' || c == '|' } }.sum())
        println((minY..maxY).map { stringMap[it].count { c -> c == '~' } }.sum())
    }

    private fun replace(s: String): String {
        val r = Pattern.compile("""#\|+#""")
        val m = r.matcher(s)
        val sb = StringBuffer()
        while (m.find()) {
            m.appendReplacement(sb, "#" + "~".repeat(m.group(0).length - 2) + "#")
        }
        m.appendTail(sb) // append the rest of the contents
        return sb.toString()
    }

    @Test
    fun day17Part2() {
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day17.txt")?.toURI()).readLines()
    }
}
