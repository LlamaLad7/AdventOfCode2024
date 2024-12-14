package day13

import getInput
import longs
import split
import utils.z3.z3Optimize

fun main() {
    day13part1(getInput(13, true, 1))
    day13part1(getInput(13, false, 1))
    day13part2(getInput(13, true, 2))
    day13part2(getInput(13, false, 2))
}

private fun day13part1(lines: List<String>) {
    val machines = lines.split("").map(::parseMachine)
    val tokens = machines.sumOf { it.fewestTokens() ?: 0 }
    println(tokens)
}

private fun day13part2(lines: List<String>) {
    val machines = lines.split("").map { parseMachine(it, 10000000000000) }
    val tokens = machines.sumOf { it.fewestTokens() ?: 0 }
    println(tokens)
}

private fun parseMachine(lines: List<String>, offset: Long = 0): Machine {
    val (aX, aY) = lines[0].longs()
    val (bX, bY) = lines[1].longs()
    val (prizeX, prizeY) = lines[2].longs()
    return Machine(Button(aX, aY, 3), Button(bX, bY, 1), Point(prizeX + offset, prizeY + offset))
}

private data class Machine(val a: Button, val b: Button, val prize: Point) {
    fun fewestTokens(): Long? = z3Optimize {
        val aPresses = "a".z3Int
        val bPresses = "b".z3Int
        val tokens = "t".z3Int

        constraints(
            aPresses * a.dx.z3Int + bPresses * b.dx.z3Int eq prize.x.z3Int,
            aPresses * a.dy.z3Int + bPresses * b.dy.z3Int eq prize.y.z3Int,
            tokens eq aPresses * a.cost.z3Int + bPresses * b.cost.z3Int,
        )
        minimize(tokens)
        if (solve()) {
            tokens.solution
        } else {
            null
        }
    }
}

private data class Button(val dx: Long, val dy: Long, val cost: Int)
private data class Point(val x: Long, val y: Long)