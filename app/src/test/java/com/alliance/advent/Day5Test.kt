package com.alliance.advent

import org.junit.Test

import java.io.File

class Day5Test {
    @Test
    fun day5Part1() {
        var polymer = inputLines()[0]
        polymer = reducePolymer(polymer)
        println(polymer.length)
    }

    private fun reducePolymer(polymer: String): String {
        var p = polymer
        var changesFound = true
        while (changesFound) {
            changesFound = false
            for (i in (0..(p.length - 2))) {
                val a = p[i]
                val b = p[i + 1]
                if (a.toLowerCase() == b.toLowerCase() && a != b) {
                    changesFound = true
                    p = p.replace("" + a + b, "")
                    break
                }
            }
        }
        return p
    }

    @Test
    fun day5Part2() {
        var polymer = inputLines()[0]
        polymer = reducePolymer(polymer)
        val lengthByChar = ('a'..'z').map {c -> reducePolymer(polymer.replace("$c|${c.toUpperCase()}".toRegex(), "")).length}
        println(lengthByChar.min())
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day5.txt")?.toURI()).readLines()
    }
}
