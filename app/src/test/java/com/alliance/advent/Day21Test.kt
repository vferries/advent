package com.alliance.advent

import org.junit.Test

import java.io.File
import kotlin.math.absoluteValue

class Day21Test {
    @Test
    fun day19Part1() {
        val lines = inputLines()
        val ip = lines[0].split(" ")[1].toInt()
        println(ip)
        val instructions = lines.drop(1).map {
            val (opCode, a, b, c) = """(.+) (\d+) (\d+) (\d+)""".toRegex().find(it)?.destructured!!
            Op(opCode, a.toInt(), b.toInt(), c.toInt())
        }
        var registers = arrayOf(0, 0, 0, 0, 0, 0)
        while (registers[ip] >= 0 && registers[ip] < instructions.size) {
            val op = instructions[registers[ip]]
            if (op.code == "eqqr") {
                println(registers[3])
            }
            registers = operations.getValue(op.code).invoke(registers, op.a, op.b, op.c)
            registers[ip]++
            //println(registers.joinToString(", "))
        }
        println(registers[0])
}

@Test
fun day16Part2() {
//    #ip 2
    val x = 0
    val reg = arrayOf(x, 0, 0, 0, 0, 0)
    reg[3] = 123
    reg[3] = reg[3] and 456
    reg[3] = if (reg[3] == 72) 1 else 0
    reg[2] = reg[3] + reg[2] // Jump if reg[3] == 72
    reg[2] = 0 // Loop top
//  [x, 0, 5, 72, 0]
    reg[3] = 0
    reg[4] = reg[3] or 65536
    reg[3] = 7041048
    reg[5] = reg[4] and 255 // Keep only 8 last bits -> 0
    reg[3] = reg[3] + reg[5]
    reg[3] = reg[3] and 16777215
    reg[3] = reg[3] * 65899
    reg[3] = reg[3] and 16777215
    reg[5] = if (256 > reg[4]) 1 else 0
    reg[2] = reg[5] + reg[2]
    reg[2] = reg[2] + 1 // Jump next instruction
    reg[2] = 27
    reg[5] = 0
    reg[1] = reg[5] + 1
    reg[1] = reg[1] * 256
    reg[1] = if (reg[1] > reg[4]) 1 else 0
    reg[2] = reg[1] + reg[2]
    reg[2] = reg[2] + 1 // Jump next
    reg[2] = 25
    reg[5] = reg[5] + 1
    reg[2] = 17
    reg[4] = reg[5]
    reg[2] = 7
//    eqrr 3 0 5 //MAGIC HAPPENS HERE
    reg[5] = if (reg[3] == reg[0]) 1 else 0
    reg[2] = reg[5] + reg[2]
    reg[2] = 5
}

private fun inputLines(): List<String> {
    return File(javaClass.classLoader?.getResource("day21.txt")?.toURI()).readLines()
}
}
