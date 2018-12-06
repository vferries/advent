package com.alliance.advent

import org.junit.Test

import java.io.File
import kotlin.math.absoluteValue

class Day6Test {
    @Test
    fun day6Part1() {
        val coords = inputLines().map { line -> line.split(", ").map { it.toInt() } }
        val minX = coords.minBy { (x, _) -> x }!![0]
        val maxX = coords.maxBy { (x, _) -> x }!![0]
        val minY = coords.minBy { (_, y) -> y }!![1]
        val maxY = coords.maxBy { (_, y) -> y }!![1]
        val countByIndex: MutableMap<Int, Int> = mutableMapOf()
        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                val distances = coords.map { (x1, y1) -> (x - x1).absoluteValue + (y - y1).absoluteValue }
                val minDistance = distances.min()
                if (distances.count { it == minDistance } == 1) {
                    val minIndex = distances.indexOf(minDistance)
                    countByIndex[minIndex] = countByIndex.getOrDefault(minIndex, 0) + 1
                }
            }
        }
        val withoutInfinite = countByIndex.filter { (k, v) ->
            val (x, y) = coords[k]
                    coords.any { (x1, y1) -> x1 > x && y1 < y }
                    && coords.any { (x1, y1) -> x1 > x && y1 > y }
                    && coords.any { (x1, y1) -> x1 < x && y1 < y }
                    && coords.any { (x1, y1) -> x1 < x && y1 > y }
        }
        println(withoutInfinite.maxBy { it.value })
    }

    @Test
    fun day5Part2() {
        val coords = inputLines().map { line -> line.split(", ").map { it.toInt() } }
        val minX = coords.minBy { (x, _) -> x }!![0]
        val maxX = coords.maxBy { (x, _) -> x }!![0]
        val minY = coords.minBy { (_, y) -> y }!![1]
        val maxY = coords.maxBy { (_, y) -> y }!![1]
        var count = 0
        (minX..maxX).forEach { x ->
            (minY..maxY).forEach { y ->
                val distances = coords.map { (x1, y1) -> (x - x1).absoluteValue + (y - y1).absoluteValue }
                val totalDistance = distances.sum()
                if (totalDistance < 10000) {
                    count++
                }
            }
        }
        println(count)
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day6.txt")?.toURI()).readLines()
    }
}
