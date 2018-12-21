package com.alliance.advent

import org.junit.Test

import java.io.File

class Day20Test {
    @Test
    fun day20Part1() {
        val directions = inputLines()[0].drop(1).dropLast(1)
        val map: MutableMap<Pos, Char> = mutableMapOf()

        fun explorePaths(start: Pos, dirs: String): Set<Pos> {
                var currentIndex = 0
                var currentPos: Pos = start
                map[currentPos] = '.'
                while (currentIndex < dirs.length) {
                    val (x, y) = currentPos
                    map[currentPos] = '.'
                    surroundingPositions(currentPos).forEach { map.putIfAbsent(it, '?') }
                    when (dirs[currentIndex]) {
                        'N' -> {
                            map[x to (y + 1)] = '-'
                            currentPos = x to (y + 2)
                        }
                        'S' -> {
                            map[x to (y - 1)] = '-'
                            currentPos = x to (y - 2)
                        }
                        'E' -> {
                            map[(x + 1) to y] = '|'
                            currentPos = (x + 2) to y
                        }
                        'W' -> {
                            map[(x - 1) to y] = '|'
                            currentPos = (x - 2) to y
                        }
                        '(' -> {
                            var endingIndex = currentIndex
                            var parensCount = 1
                            val pipeIndexes = mutableListOf(currentIndex)
                            while (parensCount != 0) {
                                endingIndex++
                                when (dirs[endingIndex]) {
                                    '(' -> parensCount++
                                    ')' -> parensCount--
                                    '|' -> if (parensCount == 1) pipeIndexes.add(endingIndex)
                                }
                            }
                            pipeIndexes += endingIndex
                            val subpaths = pipeIndexes.zip(pipeIndexes.drop(1)).map { (start, end) -> dirs.substring((start+1) until end) }
                            return subpaths
                                .flatMap { subpath -> explorePaths(currentPos, subpath) }.toSet()
                                .flatMap { ending -> explorePaths(ending, dirs.substring(endingIndex + 1)) }.toSet()
                        }
                    }
                    currentIndex++
                }
            return setOf(currentPos)
        }

        explorePaths(0 to 0, directions)

        val xs = map.keys.map { it.first }
        val ys = map.keys.map { it.second }
        val minX = xs.min()!!
        val maxX = xs.max()!!
        val minY = ys.min()!!
        val maxY = ys.max()!!
        (maxY downTo minY).forEach { y ->
            (minX..maxX).forEach { x ->
                val char = map.getOrDefault(x to y, '#')
                print(if (char == '?') '#' else char)
            }
            println()
        }

        // Longest path search...
        var maxPathLength = 0
        val visitedPos = mutableSetOf(0 to 0)
        var nextPos = setOf(0 to 0)
        var lessThan1000 = 0
        while (nextPos.isNotEmpty()) {
            maxPathLength++
            visitedPos.addAll(nextPos)
            if (maxPathLength == 1000) {
                lessThan1000 = visitedPos.size
            }
            nextPos = nextPos.flatMap { pos ->
                adjacentPositions(pos).filter { (destination, door) ->
                    !visitedPos.contains(destination) && (map[door] == '|' || map[door] == '-')
                }.map { it.first }
            }.toSet()
        }
        println()
        println(maxPathLength - 1)
        println()
        val moreThan1000 = visitedPos.size - lessThan1000
        println(moreThan1000)
    }

    private fun adjacentPositions(pos: Pos): List<Pair<Pos, Pos>> {
        val (x, y) = pos
        return listOf(
            (x - 2 to y) to (x - 1 to y),
            (x + 2 to y) to (x + 1 to y),
            (x to y - 2) to (x to y - 1),
            (x to y + 2) to (x to y + 1)
        )
    }

    fun surroundingPositions(pos: Pos): List<Pos> {
        val (x, y) = pos
        return listOf(
            x to (y - 1),
            x to (y + 1),
            (x - 1) to y,
            (x + 1) to y
        )
    }

    @Test
    fun day20Part2() {
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day20.txt")?.toURI()).readLines()
    }

}
