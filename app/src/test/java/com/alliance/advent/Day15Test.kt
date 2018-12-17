package com.alliance.advent

import org.junit.Test
import java.io.File

typealias Pos = Pair<Int, Int>

class Day15Test {
    data class Creature(var pos: Pos, val race: Race, var hitPoints: Int = 200, val force: Int = 3)

    enum class Race {
        ELFE, GOBLIN
    }

    @Test
    fun day15Part1() {
        val lines = inputLines()
        val map = mutableMapOf<Pos, Boolean>()
        val creatures = mutableListOf<Creature>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                map[x to y] = c != '#'
                when (c) {
                    'E' -> creatures += Creature(x to y, Race.ELFE)
                    'G' -> creatures += Creature(x to y, Race.GOBLIN)
                }
            }
        }

        var round = 0
        while (!(creatures.all { it.race == Race.ELFE } || creatures.all { it.race == Race.GOBLIN})) {
            round++
            creatures.sortWith(compareBy({it.pos.second}, {it.pos.first}))
            val deadCreatures = mutableListOf<Creature>()

            for (creature in creatures) {
                if (!deadCreatures.contains(creature)) {
                    val aliveCreatures = creatures.filter { !deadCreatures.contains(it) }
                    var opponents = opponentsInRange(creature, aliveCreatures)
                    if (opponents.isEmpty()) {
                        val next = nextPosition(creature, map, aliveCreatures)
                        if (next != null) creature.pos = next
                        opponents = opponentsInRange(creature, aliveCreatures)
                    }
                    if (opponents.isNotEmpty()) {
                        val minHP = opponents.map { it.hitPoints }.min()
                        val orderedPos = adjacentPositions(creature.pos)
                        val target = opponents.filter { it.hitPoints == minHP }.sortedBy { orderedPos.indexOf(it.pos) }.first()
                        target.hitPoints -= 3
                        if (target.hitPoints <= 0) {
                            deadCreatures += target
                        }
                    }
                }
            }
            creatures.removeAll(deadCreatures)
            deadCreatures.clear()
        }
        val remainingHP = creatures.sumBy(Creature::hitPoints)
        println(" Round = $round, Remaining HP = $remainingHP, Total = ${round * remainingHP}")
    }

    fun nextPosition(creature: Creature, map: Map<Pos, Boolean>, aliveCreatures: List<Creature>): Pos? {
        val visited = mutableSetOf(creature.pos)
        var nextRound = adjacentPositions(creature.pos).filter { map.getValue(it) && !aliveCreatures.map { it.pos }.contains(it) }.map { it to it }
        while (nextRound.isNotEmpty()) {
            //println("nextMoveRound ${nextRound}")
            val first = nextRound.find { (_, dest) ->
                opponentsInRange(Creature(dest, creature.race), aliveCreatures).isNotEmpty()
            }
            if (first != null) return first.first
            visited.addAll(nextRound.map { it.second })

            val tmp = mutableListOf<Pair<Pos, Pos>>()
            nextRound.forEach { (first, last) ->
                val validPositions = adjacentPositions(last).filter { pos ->
                    !visited.contains(pos) && map.getValue(pos) && !aliveCreatures.map { it.pos }.contains(pos)
                }
                validPositions.forEach { pos ->
                    if (!visited.contains(pos)) {
                        tmp.add(first to pos)
                        visited.add(pos)
                    }
                }
            }
            nextRound = tmp
        }
        return null
    }

    fun opponentsInRange(creature: Creature, aliveCreatures: List<Creature>): List<Creature> {
        val adjacentPos = adjacentPositions(creature.pos)
        return aliveCreatures.filter { adjacentPos.contains(it.pos) && it.race != creature.race }
    }

    fun adjacentPositions(pos: Pos): List<Pair<Int, Int>> {
        val (x, y) = pos
        return listOf(x to (y - 1), (x - 1) to y, (x + 1) to y, x to (y + 1))
    }

    @Test
    fun day15Part2() {
        val lines = inputLines()
        val map = mutableMapOf<Pos, Boolean>()
        val creatures = mutableListOf<Creature>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                map[x to y] = c != '#'
                when (c) {
                    'E' -> creatures += Creature(x to y, Race.ELFE, force = 19)
                    'G' -> creatures += Creature(x to y, Race.GOBLIN)
                }
            }
        }

        var round = 3
        while (!(creatures.all { it.race == Race.ELFE } || creatures.all { it.race == Race.GOBLIN})) {
            round++
            creatures.sortWith(compareBy({it.pos.second}, {it.pos.first}))
            val deadCreatures = mutableListOf<Creature>()

            for (creature in creatures) {
                if (!deadCreatures.contains(creature)) {
                    val aliveCreatures = creatures.filter { !deadCreatures.contains(it) }
                    var opponents = opponentsInRange(creature, aliveCreatures)
                    if (opponents.isEmpty()) {
                        val next = nextPosition(creature, map, aliveCreatures)
                        if (next != null) creature.pos = next
                        opponents = opponentsInRange(creature, aliveCreatures)
                    }
                    if (opponents.isNotEmpty()) {
                        val minHP = opponents.map { it.hitPoints }.min()
                        val orderedPos = adjacentPositions(creature.pos)
                        val target = opponents.filter { it.hitPoints == minHP }.sortedBy { orderedPos.indexOf(it.pos) }.first()
                        target.hitPoints -= creature.force
                        if (target.hitPoints <= 0) {
                            deadCreatures += target
                        }
                    }
                }
            }
            if (deadCreatures.find { it.race == Race.ELFE } != null) {
                println("ONE ELVE DIED")
                return
            }
            creatures.removeAll(deadCreatures)
            deadCreatures.clear()
        }
        val remainingHP = creatures.sumBy(Creature::hitPoints)
        println(" Round = $round, Remaining HP = $remainingHP, Total = ${round * remainingHP}")
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day15.txt")?.toURI()).readLines()
    }
}
