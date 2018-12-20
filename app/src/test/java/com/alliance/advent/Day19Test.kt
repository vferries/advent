package com.alliance.advent

import org.junit.Test

import java.io.File
import kotlin.math.absoluteValue

data class Op(val code: String, val a: Int, val b: Int, val c: Int)

class Day19Test {
    @Test
    fun day19Part1() {
        val lines = inputLines()
        val ip = lines[0].split(" ")[1].toInt()
        println(ip)
        val instructions = lines.drop(1).map {
            val (opCode, a, b, c) = """(.+) (\d+) (\d+) (\d+)""".toRegex().find(it)?.destructured!!
            Op(opCode, a.toInt(), b.toInt(), c.toInt())
        }
        var registers = arrayOf(1, 0, 0, 0, 0, 0)
        while (registers[ip] >= 0 && registers[ip] < instructions.size) {
            val op = instructions[registers[ip]]
            registers = operations.getValue(op.code).invoke(registers, op.a, op.b, op.c)
            registers[ip]++
            println(registers.joinToString(", "))
        }
        println(registers[0])

        //let p2 = 10551277;
        //println!("  2: {}", p2 + (1..=p2/2).filter(|x| p2 % x == 0).sum::<u32>());
/*        val samples: MutableList<Op> = mutableListOf()
        for (i in 0 until lines.size step 4) {
            if (lines[i].startsWith("#")) {
            } else {
            }
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
    //operations.getValue(code)(registers, a, b, c)
    println(result[0])*/
}

@Test
fun day16Part2() {
    val p2 = 10551277
    val res = (1..(p2/2)).filter {p2 % it == 0 }.sum()
    println(p2 + res)
}

private fun inputLines(): List<String> {
    return File(javaClass.classLoader?.getResource("day19.txt")?.toURI()).readLines()
}
}
