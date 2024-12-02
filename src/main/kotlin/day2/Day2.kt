package day2

import getInput
import kotlin.math.abs

fun main() {
    day2part1(getInput(2, true, 1))
    day2part1(getInput(2, false, 1))
    day2part2(getInput(2, true, 2))
    day2part2(getInput(2, false, 2))
}

private fun day2part1(lines: List<String>) {
    val result = lines.count { line ->
        line.ints().isSafe()
    }
    println(result)
}

private fun day2part2(lines: List<String>) {
    val result = lines.count { line ->
        val nums = line.ints()
        nums.isValidWithSkip { a, b -> a < b } || nums.isValidWithSkip { a, b -> a > b }
    }
    println(result)
}

private fun List<Int>.isValidWithSkip(test: (Int, Int) -> Boolean): Boolean {
    val badPair = badIndex(test)
    return badPair == -1 || without(badPair).isSafe() ||
            badPair < lastIndex && without(badPair + 1).isSafe()
}

private fun List<Int>.badIndex(test: (Int, Int) -> Boolean) =
    zipWithNext().indexOfFirst { (a, b) -> abs(a - b) !in 1..3 || !test(a, b) }

private fun List<Int>.isDecreasing() = zipWithNext { a, b -> a - b }.all { it < 0 }
private fun List<Int>.isIncreasing() = zipWithNext { a, b -> a - b }.all { it > 0 }
private fun List<Int>.differenceInRange(range: IntRange) = zipWithNext { a, b -> abs(a - b) }.all { it in range }
private fun List<Int>.isSafe() = (isDecreasing() || isIncreasing()) && differenceInRange(1..3)
private fun String.ints() = splitToSequence(' ').map { it.toInt() }.toList()
private fun List<Int>.without(index: Int) = toMutableList().apply { removeAt(index) }