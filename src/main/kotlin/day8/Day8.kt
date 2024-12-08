package day8

import getInput
import kotlin.math.absoluteValue

fun main() {
    day8part1(getInput(8, true, 1))
    day8part1(getInput(8, false, 1))
    day8part2(getInput(8, true, 2))
    day8part2(getInput(8, false, 2))
}

private fun day8part1(lines: List<String>) {
    val board = Board(lines)
    for (antennae in board.antennaeByType().values) {
        for (a in antennae) {
            for (b in antennae) {
                if (a == b) {
                    continue
                }
                val diff = b - a
                board.addAntinode(a - diff)
                board.addAntinode(b + diff)
            }
        }
    }
    println(board.antinodes.size)
}

private fun day8part2(lines: List<String>) {
    val board = Board(lines)
    for (antennae in board.antennaeByType().values) {
        for (a in antennae) {
            for (b in antennae) {
                if (a == b) {
                    continue
                }
                val diff = (b - a).simplify()
                var first = b
                var last = a
                while (board.addAntinode(first) or board.addAntinode(last)) {
                    first -= diff
                    last += diff
                }
            }
        }
    }
    println(board.antinodes.size)
}

private class Board(private val lines: List<String>) {
    private val height = lines.size
    private val width = lines[0].length
    private val _antinodes = hashSetOf<Vec>()
    val antinodes: Set<Vec> get() = _antinodes

    fun antennae() = sequence {
        for (x in 0..<width) {
            for (y in 0..<height) {
                if (lines[y][x] != '.') {
                    yield(Antenna(lines[y][x], Vec(x, y)))
                }
            }
        }
    }

    fun antennaeByType() = antennae().groupBy({ it.type }, { it.pos })

    fun addAntinode(pos: Vec): Boolean = pos.x in 0..<width && pos.y in 0..<height && run {
        _antinodes.add(pos)
        true
    }
}

private data class Antenna(val type: Char, val pos: Vec)

private data class Vec(val x: Int, val y: Int) {
    operator fun plus(v: Vec) = Vec(x + v.x, y + v.y)

    operator fun minus(v: Vec) = Vec(x - v.x, y - v.y)

    fun simplify(): Vec {
        val gcd = gcd(x.absoluteValue, y.absoluteValue)
        return Vec(x / gcd, y / gcd)
    }
}

private tailrec fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)