package day25

import utils.getInput
import utils.split

fun main() {
    day25part1(getInput(25, true, 1))
    day25part1(getInput(25, false, 1))
}

private fun day25part1(lines: List<String>) {
    val (locks, keys) = parseInput(lines)
    var total = 0
    for (lock in locks) {
        for (key in keys) {
            if (fits(lock, key)) {
                total++
            }
        }
    }
    println(total)
}

private fun fits(lock: List<Int>, key: List<Int>) = lock.zip(key).all { (a, b) -> b <= a }

private fun parseInput(lines: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
    val locks = mutableListOf<List<Int>>()
    val keys = mutableListOf<List<Int>>()
    for (grid in lines.split("")) {
        val sig = (0..4).map { col ->
            (0..6).asSequence()
                .map { row -> grid[row][col] }
                .zipWithNext { a, b -> a != b }
                .indexOfFirst { it }
                .let { 5 - it }
        }
        if (grid[0][0] == '#') {
            locks.add(sig)
        } else {
            keys.add(sig)
        }
    }
    return locks to keys
}