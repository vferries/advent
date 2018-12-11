package com.alliance.advent

import org.junit.Test

class Day11Test {
    companion object {
        const val serial = 3613
    }

    @Test
    fun day11Part1() {
        val map = generatePowerLevelMap()
        val res = map.keys.filter { (x, y) -> x < 299 && y < 299 }
            .maxBy { (x, y) ->
                (x..(x + 2)).flatMap { i -> (y..(y + 2)).map { j -> map.getOrDefault(i to j, 0) } }.sum()
            }
        println(res)
    }

    @Test
    fun day11Part2() {
        val map = generatePowerLevelMap()
        val rows = mutableMapOf<Pair<IntRange, Int>, Int>()
        val mapTriples = map.map { (coords, v) -> Triple(coords.first, coords.second, 1) to v }.toMap().toMutableMap()
        for (size in (2..300)) {
            for (x in (1..(300 - size + 1))) {
                for (y in (1..(300 - size + 1))) {
                    val lastX = x + size - 1
                    val lastY = y + size - 1
                    // TODO We could precompute these 2
                    val row = (x..(lastX-1)).map { map.getValue(it to lastY) }.sum()
                    val col = (y..(lastY-1)).map { map.getValue(lastX to it) }.sum()
                    mapTriples[Triple(x, y, size)] = mapTriples.getValue(Triple(x, y, size - 1)) + row + col + map.getValue(lastX to lastY)
                }
            }
        }
        println(mapTriples.maxBy { it.value })
/*
        val res = triples.maxBy { (x, y, size) ->
            (x..(x+size-1)).flatMap { i -> (y..(y + size - 1)).map { j -> map.getOrDefault(i to j, 0) } }.sum()
        }
        println(res)*/
    }

    private fun generatePowerLevelMap(): Map<Pair<Int, Int>, Int> =
        (1..300)
            .flatMap { i -> (1..300).map { j -> i to j } }
            .map { (i, j) -> (i to j) to cellPowerLevel(i, j) }
            .toMap()

    private fun cellPowerLevel(x: Int, y: Int): Int {
        val rackId = x + 10
        var powerLevel = rackId * y
        powerLevel += serial
        powerLevel *= rackId
        powerLevel = (powerLevel / 100) % 10
        return powerLevel - 5
    }

    private fun findMaxSubMatrix(a: Map<Pair<Int, Int>, Int>) {
        val cols = 300
        val rows = 300
        var currentResult: IntArray
        var maxSum = Integer.MIN_VALUE
        var left = 1
        var top = 1
        var right = 1
        var bottom = 1

        for (leftCol in 1..cols) {
            val tmp = IntArray(rows)
            for (rightCol in leftCol..cols) {
                for (i in 1..rows) {
                    tmp[i - 1] += a.getValue(i to rightCol)
                }
                currentResult = kadane(tmp)
                if (currentResult[0] > maxSum) {
                    maxSum = currentResult[0]
                    left = leftCol
                    top = currentResult[1]
                    right = rightCol
                    bottom = currentResult[2]
                }
            }
        }
        println("MaxSum: $maxSum , range: [($left, $top)($right, $bottom)]")
    }

    private fun kadane(a: IntArray): IntArray {
        //result[0] == maxSum, result[1] == start, result[2] == end;
        val result = intArrayOf(Integer.MIN_VALUE, 0, -1)
        var currentSum = 0
        var localStart = 0

        for (i in 0 until a.size) {
            currentSum += a[i]
            if (currentSum < 0) {
                currentSum = 0
                localStart = i + 1
            } else if (currentSum > result[0]) {
                result[0] = currentSum
                result[1] = localStart
                result[2] = i
            }
        }

        //all numbers in a are negative
        if (result[2] == -1) {
            result[0] = 0
            for (i in 0 until a.size) {
                if (a[i] > result[0]) {
                    result[0] = a[i]
                    result[1] = i
                    result[2] = i
                }
            }
        }
        return result
    }
}
