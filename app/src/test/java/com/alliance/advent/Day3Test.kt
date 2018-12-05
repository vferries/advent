package com.alliance.advent

import org.junit.Test

import java.io.File

data class Fabric(val id: Int, val x: Int, val y: Int, val width: Int, val height: Int)

class Day3Test {
    @Test
    fun day3Part1() {
        val lines = inputLines()
        val fabrics = lines.map(::lineToFabric)
        val map = countMap(fabrics)
        println(map.values.filter { it > 1 }.size)
    }

    @Test
    fun day3Part2() {
        val lines = inputLines()
        val fabrics = lines.map(::lineToFabric)
        val map = countMap(fabrics)
        val result= fabrics.find { f ->
            val xRange = (f.x until (f.x + f.width))
            val yRange = (f.y until (f.y + f.height))
            iterateRanges(xRange, yRange).all {p -> map[p] == 1}
        }
        println(result?.id)
    }

    private fun countMap(fabrics: List<Fabric>): MutableMap<Pair<Int, Int>, Int> {
        val map = mutableMapOf<Pair<Int, Int>, Int>()
        fabrics.forEach { f ->
            val xRange = (f.x until (f.x + f.width))
            val yRange = (f.y until (f.y + f.height))
            iterateRanges(xRange, yRange).forEach { (i, j) -> map[i to j] = map.getOrDefault(i to j, 0) + 1 }
        }
        return map
    }

    private fun iterateRanges(
        xRange: IntRange,
        yRange: IntRange
    ) = xRange.flatMap { x -> yRange.map { y -> x to y } }

    private fun lineToFabric(line: String): Fabric {
        val (id, x, y, width, height) = """#(\d+) @ (\d+),(\d+): (\d+)x(\d+)""".toRegex().find(line)?.destructured!!
        return Fabric(id.toInt(), x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }

    private fun inputLines(): List<String> {
        val lines = File(javaClass.classLoader?.getResource("day3.txt")?.toURI()).readLines()
        return lines
    }
}
