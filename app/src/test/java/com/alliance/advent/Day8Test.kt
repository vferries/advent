package com.alliance.advent

import org.junit.Test

import java.io.File
import java.util.*
import kotlin.math.absoluteValue

class Day8Test {
    @Test
    fun day8Part1() {
        val numbers = inputLines()[0].split(" ").map { it.toInt() }
        val root = parseTree(numbers)
        println(root.sumMetadata())
    }

    private fun parseTree(numbers: List<Int>): Node {
        return parseNodes(1, numbers)[0]
    }

    private fun parseNodes(nodesCount: Int, l: List<Int>): List<Node> {
        val nodes = mutableListOf<Node>()
        var list = l
        for (i in (0 until nodesCount)) {
            val childCount = list[0]
            val metaDataCount = list[1]
            val children = parseNodes(childCount, list.subList(2, list.size))

            val childrenLength = children.sumBy { it.nodeSize() }
            val metaData = list.subList(2 + childrenLength, 2 + childrenLength + metaDataCount)
            nodes += Node(metaData, children)
            list = list.subList(2 + childrenLength + metaDataCount, list.size)
        }
        return nodes
    }

    @Test
    fun day8Part2() {
        val numbers = inputLines()[0].split(" ").map { it.toInt() }
        val root = parseTree(numbers)
        println(root.nodeValue())
    }

    private fun inputLines(): List<String> {
        return File(javaClass.classLoader?.getResource("day8.txt")?.toURI()).readLines()
    }
}

data class Node(val metaData: List<Int>, val children: List<Node>) {
    fun nodeSize(): Int {
        return 2 + metaData.size + children.sumBy { it.nodeSize() }
    }

    fun sumMetadata(): Int {
        return metaData.sum() + children.sumBy { it.sumMetadata() }
    }

    fun nodeValue(): Int {
        return if (children.size == 0) metaData.sum() else metaData.sumBy {
            if (it > 0 && it <= children.size) children[it - 1].nodeValue() else 0
        }
    }
}
