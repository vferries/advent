package com.alliance.advent

import org.junit.Test

import java.io.File
import java.util.*
import kotlin.math.absoluteValue

class Day9Test {
    @Test
    fun day9Part1() {
        println(game(424, 71482))
    }

    @Test
    fun day9Part2() {
        println(game(424, 71482 * 100))
    }
}

class Board : ArrayDeque<Int>() {
    fun rotate(amount: Int) {
        if (amount >= 0) {
            for (i in 0 until amount) {
                addFirst(removeLast())
            }
        } else {
            for (i in 0 until -amount - 1) {
                addLast(remove())
            }
        }
    }
}

private fun game(players: Int, marbleMaxValue: Int): Long {
    val board = Board()
    val scores = LongArray(players)
    board.addFirst(0)

    for (marble in (1..marbleMaxValue)) {
        if (marble % 23 == 0) {
            board.rotate(-7)
            scores[marble % players] += board.pop().toLong() + marble

        } else {
            board.rotate(2)
            board.addLast(marble)
        }
    }

    return scores.max()!!
}
