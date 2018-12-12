package com.alliance.advent

import org.junit.Test
import java.io.File

class Day12Test {
    @Test
    fun day12Part1() {
        val lines = inputLines()
        var current = lines[0].split(" ")[2]
        val rules = lines.subList(2, lines.size).map { line ->
            val (pattern, result) = line.split(" => ")
            pattern to result
        }.toMap()
        var shift = 0
        for (i in 1..20) {
            current = "...$current..."
            shift -= 3
            current = (0..current.length).joinToString("") { index ->
                if (index < 2 || index > current.length - 3) "." else rules.getOrDefault(current.substring((index - 2)..(index + 2)), ".")
            }
            println(current)
        }
        val count = current.foldIndexed(0) { i, acc, c -> acc + if (c == '#') i + shift else 0 }
        println(count)
    }

    @Test
    fun day12Part2() {
        val lines = inputLines()
        var current = lines[0].split(" ")[2]
        val rules = lines.subList(2, lines.size).map { line ->
            val (pattern, result) = line.split(" => ")
            pattern to result
        }.toMap()
        var shift = 0L
        for (i in 1..50000000000) {
            val previous = current
            val previousShift = shift
            current = "...$current..."
            shift -= 3
            current = (0..current.length).joinToString("") { index ->
                if (index < 2 || index > current.length - 3) "." else rules.getOrDefault(current.substring((index - 2)..(index + 2)), ".")
            }
            while (current[0] == '.') {
                current = current.substring(1)
                shift++
            }
            while (current[current.length - 1] == '.') {
                current = current.substring(0, current.length - 1)
            }
            if (previous == current) {
                println("found pattern")
                println(i)
                println(previousShift)
                println(shift)
                shift += (50000000000 - i) * (shift - previousShift)
                break
            }
        }
        val count = current.foldIndexed(0L) { i, acc, c -> acc + if (c == '#') i + shift else 0 }
        println(count)
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day12.txt")?.toURI()).readLines()
    }
}
