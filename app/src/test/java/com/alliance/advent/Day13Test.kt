package com.alliance.advent

import com.alliance.advent.Day13Test.Turn.*
import org.junit.Test

import java.io.File

class Day13Test {
    data class Train(val pos: Pair<Int, Int>, val direction: Char, val nextTurn: Turn)

    enum class Turn {
        LEFT, STRAIGHT, RIGHT
    }

    val directions = listOf('^', '>', 'v', '<')

    @Test
    fun day13Part1() {
        val lines = inputLines()
        val tracks = mutableMapOf<Pair<Int, Int>, Char>()
        var trains = mutableListOf<Train>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    'v', '^', '>', '<' -> trains.add(Train(x to y, c, LEFT))
                    '-', '|', '/', '\\', '+' -> tracks[x to y] = c
                }
            }
        }
        trains.forEach { (pos, direction, _) ->
            val replacement = when (direction) {
                'v', '^' -> '|'
                '<', '>' -> '-'
                else -> '#'
            }
            tracks[pos] = replacement
        }

        while (true) {
            trains = trains.sortedWith(compareBy({it.pos.second}, {it.pos.first})).toMutableList()
            for (i in 0 until trains.size) {
                val (pos, direction, nextTurn) = trains[i]
                var newDirection = direction
                var newNextTurn = nextTurn
                val currentTrack = tracks.getValue(pos)
                when (currentTrack) {
                    '+' -> {
                        when (nextTurn) {
                            LEFT -> newDirection = directions[(directions.indexOf(direction) - 1 + directions.size) % directions.size]
                            RIGHT -> newDirection = directions[(directions.indexOf(direction) + 1) % directions.size]
                            STRAIGHT -> {}
                        }
                        newNextTurn = values()[(nextTurn.ordinal + 1) % 3]
                    }
                    '/' -> {
                        newDirection = when (direction) {
                            '^' -> '>'
                            'v' -> '<'
                            '<' -> 'v'
                            '>' -> '^'
                            else -> '#'
                        }
                    }
                    '\\' -> {
                        newDirection = when (direction) {
                            '^' -> '<'
                            'v' -> '>'
                            '<' -> '^'
                            '>' -> 'v'
                            else -> '#'
                        }
                    }
                }
                val newPos = when (newDirection) {
                    '^' -> pos.first to (pos.second - 1)
                    'v' -> pos.first to (pos.second + 1)
                    '<' -> (pos.first - 1) to pos.second
                    '>' -> (pos.first + 1) to pos.second
                    else -> pos
                }
                if (trains.any { (pos, _, _) -> pos == newPos }) {
                    println("Collision at pos $newPos")
                    return
                }
                trains[i] = Train(newPos, newDirection, newNextTurn)
            }
        }
    }

    @Test
    fun day13Part2() {
        val lines = inputLines()
        val tracks = mutableMapOf<Pair<Int, Int>, Char>()
        var trains = mutableListOf<Train>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    'v', '^', '>', '<' -> trains.add(Train(x to y, c, LEFT))
                    '-', '|', '/', '\\', '+' -> tracks[x to y] = c
                }
            }
        }
        trains.forEach { (pos, direction, _) ->
            val replacement = when (direction) {
                'v', '^' -> '|'
                '<', '>' -> '-'
                else -> '#'
            }
            tracks[pos] = replacement
        }

        while (trains.size > 1) {
            trains = trains.sortedWith(compareBy({it.pos.second}, {it.pos.first})).toMutableList()
            val collisionned = mutableListOf<Train>()
            for (i in 0 until trains.size) {
                val (pos, direction, nextTurn) = trains[i]
                var newDirection = direction
                var newNextTurn = nextTurn
                val currentTrack = tracks.getValue(pos)
                when (currentTrack) {
                    '+' -> {
                        when (nextTurn) {
                            LEFT -> newDirection = directions[(directions.indexOf(direction) - 1 + directions.size) % directions.size]
                            RIGHT -> newDirection = directions[(directions.indexOf(direction) + 1) % directions.size]
                            STRAIGHT -> {}
                        }
                        newNextTurn = values()[(nextTurn.ordinal + 1) % 3]
                    }
                    '/' -> {
                        newDirection = when (direction) {
                            '^' -> '>'
                            'v' -> '<'
                            '<' -> 'v'
                            '>' -> '^'
                            else -> '#'
                        }
                    }
                    '\\' -> {
                        newDirection = when (direction) {
                            '^' -> '<'
                            'v' -> '>'
                            '<' -> '^'
                            '>' -> 'v'
                            else -> '#'
                        }
                    }
                }
                val newPos = when (newDirection) {
                    '^' -> pos.first to (pos.second - 1)
                    'v' -> pos.first to (pos.second + 1)
                    '<' -> (pos.first - 1) to pos.second
                    '>' -> (pos.first + 1) to pos.second
                    else -> pos
                }
                //FIXME Are we moving a collisioned train ?
                if (!collisionned.contains(trains[i])) {
                    val collisionningTrain = trains.filter { !collisionned.contains(it) }.find { (pos, _, _) -> pos == newPos }
                    if (collisionningTrain != null) {
                        println("Collision at pos $newPos")
                        collisionned += trains[i]
                        collisionned += collisionningTrain
                    } else {
                        trains[i] = Train(newPos, newDirection, newNextTurn)
                    }
                }
            }
            trains.removeAll(collisionned)
            collisionned.clear()
        }
        println("Last train remaining: ${trains[0]}")
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day13.txt")?.toURI()).readLines()
    }
}
