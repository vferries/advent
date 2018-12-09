package com.alliance.advent

import org.junit.Test

import java.io.File
import java.util.*
import kotlin.math.absoluteValue

class Day7Test {
    @Test
    fun day7Part1() {
        val precedence = inputLines()
            .map { line -> """Step (.) must be finished before step (.) can begin.""".toRegex().find(line)?.destructured!! }
            .map { (before, after) -> before to after }
        var res = ""

        val letters = (precedence.map { it.first } + precedence.map { it.second }).toSet()
        val next = sortedSetOf<String>()
        do {
            addFreeOnes(next, letters, res, precedence)
            res += next.pollFirst()
            addFreeOnes(next, letters, res, precedence)
        } while (next.size > 0)
        println(res)
    }

    private fun addFreeOnes(
        next: TreeSet<String>,
        letters: Set<String>,
        res: String,
        precedence: List<Pair<String, String>>
    ) {
        next.addAll(
            letters
                .filter { !res.contains(it) && !next.contains(it) }
                .filter { precedence.none { (start, end) -> end == it && !res.contains(start) } })
    }

    @Test
    fun day7Part2() {
        val precedence = inputLines()
            .map { line -> """Step (.) must be finished before step (.) can begin.""".toRegex().find(line)?.destructured!! }
            .map { (before, after) -> before to after }
        var res = ""
        var count = 0

        val letters = (precedence.map { it.first } + precedence.map { it.second }).toSet()
        val next = sortedSetOf<String>()
        var current = mutableListOf<Pair<String, Int>>()
        do {
            addFreeOnesWithCurrent(next, letters, current, res, precedence)

            //TODO
            while (current.size < 5 && !next.isEmpty()) {
                val elem = next.pollFirst()
                current.add(elem to toDuration(elem))
                println(current)
                println(next)
                println()
            }
            val min = current.map { it.second }.min()!!
            current = current.map { (l, d) -> l to (d - min)}.toMutableList()
            current.filter { it.second == 0 }.forEach { res += it.first }
            current = current.filter { it.second > 0 }.toMutableList()
            count += min

            addFreeOnesWithCurrent(next, letters, current, res, precedence)
            println(next.size)
        } while (next.size > 0 || current.isNotEmpty())
        println(res)
        println(count)
    }

    private fun addFreeOnesWithCurrent(
        next: TreeSet<String>,
        letters: Set<String>,
        current: MutableList<Pair<String, Int>>,
        res: String,
        precedence: List<Pair<String, String>>
    ) {
        next.addAll(
            letters
                .filter { l -> !res.contains(l) && !next.contains(l) && !current.map { it.first }.contains(l) }
                .filter { precedence.none { (start, end) -> end == it && !res.contains(start) } })
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day7.txt")?.toURI()).readLines()
    }

    private fun toDuration(task: String): Int {
        return 61 + (task[0] - 'A')
    }
}

//BCEFGHKIJLDMOPSTANQVXRWZUY
//BCEFDGAIJKLMNOPQRSTUVWXHYZ
//BCDEFAGHIJKLMNOPQRSTUVWXYZ
//BCEFLDMQTXHZGKIASVJYORPUWN