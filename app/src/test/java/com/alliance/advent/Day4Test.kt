package com.alliance.advent

import org.junit.Test

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class Record(val date: Date, val value: String)

class Day4Test {
    @Test
    fun day4Part1() {
        val mapAsleep = mutableMapOf<Int, List<Int>>()
        val lines = inputLines()
        val records = lines.map(::parseRecords).sortedBy { it.date }
            .map { if (it.date.hours > 1) Record(toNextMidnight(it.date), it.value) else it }
        var lastSleepy: Int? = null
        var currentId = 0
        records.forEachIndexed { index, (date, value) ->
            if (value.startsWith("Guard #")) {
                currentId = """Guard #(\d+) begins shift""".toRegex().find(value)?.destructured!!.component1().toInt()
                lastSleepy = null
            } else if (index == records.size - 1 && lastSleepy != null) {
                (lastSleepy!! until 60).forEach { minute -> mapAsleep[currentId] = mapAsleep.getOrDefault(currentId, listOf()) + minute }
            } else if (value == "falls asleep") {
                lastSleepy = date.minutes
            } else if (value == "wakes up" && lastSleepy != null) {
                (lastSleepy!! until date.minutes).forEach { minute -> mapAsleep[currentId] = mapAsleep.getOrDefault(currentId, listOf()) + minute }
            }
        }
        val id = mapAsleep.keys.maxBy { mapAsleep.getValue(it).size }!!
        val max = mapAsleep.getValue(id).groupBy { it }.values.maxBy { it.size }?.get(0)!!
        println("$id $max, ${id * max}")
    }

    @Test
    fun day4Part2() {
        val mapAsleep = mutableMapOf<Int, List<Int>>()
        val lines = inputLines()
        val records = lines.map(::parseRecords).sortedBy { it.date }
            .map { if (it.date.hours > 1) Record(toNextMidnight(it.date), it.value) else it }
        var lastSleepy: Int? = null
        var currentId = 0
        records.forEachIndexed { index, (date, value) ->
            if (value.startsWith("Guard #")) {
                currentId = """Guard #(\d+) begins shift""".toRegex().find(value)?.destructured!!.component1().toInt()
                lastSleepy = null
            } else if (index == records.size - 1 && lastSleepy != null) {
                (lastSleepy!! until 60).forEach { minute -> mapAsleep[currentId] = mapAsleep.getOrDefault(currentId, listOf()) + minute }
            } else if (value == "falls asleep") {
                lastSleepy = date.minutes
            } else if (value == "wakes up" && lastSleepy != null) {
                (lastSleepy!! until date.minutes).forEach { minute -> mapAsleep[currentId] = mapAsleep.getOrDefault(currentId, listOf()) + minute }
            }
        }
        //TODO Minute with one single guard asleep the most
        val minute = (0..59).maxBy { minute -> mapAsleep.getValue(mapAsleep.keys.maxBy { key -> mapAsleep.getOrDefault(key, listOf()).count { it == minute } }!!).filter { it == minute }.size }!!
        val id = mapAsleep.keys.maxBy { k -> mapAsleep.getValue(k).count { it == minute }}!!
        println("$id $minute, ${id * minute}")
    }

    private fun parseRecords(line: String): Record {
        val (day, rest) = """^\[(.*)] (.*)$""".toRegex().find(line)?.destructured!!
        val date = SimpleDateFormat("yyyy-MM-dd hh:mm").parse(day)
        return Record(date, rest)
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day4.txt")?.toURI()).readLines()
    }

    private fun toNextMidnight(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        return calendar.time
    }
}
