package com.alliance.advent

import org.junit.Test

import java.io.File
import kotlin.math.absoluteValue

typealias Registers = Array<Int>

typealias Operation = (Registers, Int, Int, Int) -> Registers

data class Instruction(val opCode: Int, val A: Int, val B: Int, val C: Int)

data class Sample(val before: Registers, val instruction: Instruction, val after: Registers)

val operations: Map<String, Operation> = mapOf(
    "addr" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] + before[b]
        res
    },
    "addi" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] + b
        res
    },
    "mulr" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] * before[b]
        res
    },
    "muli" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] * b
        res
    },
    "banr" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] and before[b]
        res
    },
    "bani" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] and b
        res
    },
    "borr" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] or before[b]
        res
    },
    "bori" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = before[a] or b
        res
    },
    "setr" to { before, a, _, c ->
        val res = before.copyOf()
        res[c] = before[a]
        res
    },
    "seti" to { before, a, _, c ->
        val res = before.copyOf()
        res[c] = a
        res
    },
    "gtir" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = if (a > before[b]) 1 else 0
        res
    },
    "gtri" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = if (before[a] > b) 1 else 0
        res
    },
    "gtrr" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = if (before[a] > before[b]) 1 else 0
        res
    },
    "eqir" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = if (a == before[b]) 1 else 0
        res
    },
    "eqri" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = if (before[a] == b) 1 else 0
        res
    },
    "eqrr" to { before, a, b, c ->
        val res = before.copyOf()
        res[c] = if (before[a] == before[b]) 1 else 0
        res
    }
)

class Day16Test {
    @Test
    fun day16Part1() {
        val samples = parseSamples()
        val applicables = samples.map { (before, instruction, after) ->
            instruction to operations.filter { (_, op) ->
                op(before, instruction.A, instruction.B, instruction.C).contentEquals(after)
            }
        }
        println(applicables.count { (_, map) -> map.size >= 3 })
    }

    private fun parseSamples(): MutableList<Sample> {
        val lines = inputLines()
        val samples: MutableList<Sample> = mutableListOf()
        for (i in 0 until lines.size step 4) {
            if (lines[i].startsWith("Before")) {
                val (ba, bb, bc, bd) = """(\d+), (\d+), (\d+), (\d+)""".toRegex().find(lines[i])?.destructured!!
                val before = arrayOf(ba.toInt(), bb.toInt(), bc.toInt(), bd.toInt())
                val (op, a, b, c) = """(\d+) (\d+) (\d+) (\d+)""".toRegex().find(lines[i + 1])?.destructured!!
                val instruction = Instruction(op.toInt(), a.toInt(), b.toInt(), c.toInt())
                val (aa, ab, ac, ad) = """(\d+), (\d+), (\d+), (\d+)""".toRegex().find(lines[i + 2])?.destructured!!
                val after = arrayOf(aa.toInt(), ab.toInt(), ac.toInt(), ad.toInt())
                val sample = Sample(before, instruction, after)
                samples.add(sample)
            }
        }
        return samples
    }

    @Test
    fun day16Part2() {
        val program = inputLines().drop(3006).map { line -> line.split(" ").map(String::toInt) }
        val result = program.fold(arrayOf(0, 0, 0, 0)) { registers, (opCode, a, b, c) ->
            operations.getValue(opCodeToName.getValue(opCode))(registers, a, b, c)
        }
        println(result[0])

    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day16.txt")?.toURI()).readLines()
    }
}

val opCodeToName = mapOf(
    0 to "setr",
    1 to "eqrr",
    2 to "gtri",
    3 to "muli",
    4 to "eqir",
    5 to "borr",
    6 to "bori",
    7 to "mulr",
    8 to "gtrr",
    9 to "seti",
    10 to "banr",
    11 to "eqri",
    12 to "addr",
    13 to "gtir",
    14 to "addi",
    15 to "bani"
)
