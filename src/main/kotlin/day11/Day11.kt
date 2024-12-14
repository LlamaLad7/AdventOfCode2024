package day11

import com.aballano.mnemonik.memoize
import utils.getInput
import kotlin.math.log10

fun main() {
    day11part1(getInput(11, true, 1))
    day11part1(getInput(11, false, 1))
    day11part2(getInput(11, true, 2))
    day11part2(getInput(11, false, 2))
    println()
}

private fun day11part1(lines: List<String>) {
    val nums = lines.single().split(' ').map { it.toLong() }
    println(nums.sumOf { runSim(25, it) })
}

private fun day11part2(lines: List<String>) {
    val nums = lines.single().split(' ').map { it.toLong() }
    println(nums.sumOf { runSim(75, it) })
}

private fun Long.numOfDigits() = log10(toDouble()).toInt() + 1

private const val MAX_TO_MULTIPLY = Long.MAX_VALUE / 2024

private val runSim = ::runSim0.memoize()

private fun runSim0(turnsLeft: Int, num: Long): Long {
    if (turnsLeft == 0) {
        return 1
    }
    if (num == 0L) {
        return runSim(turnsLeft - 1, 1L)
    }
    val digits = num.numOfDigits()
    if (digits % 2 == 0) {
        val str = num.toString()
        check(str.length == digits)
        return runSim(turnsLeft - 1, str.take(digits / 2).toLong()) +
                runSim(turnsLeft - 1, str.drop(digits / 2).toLong())
    }
    check(num < MAX_TO_MULTIPLY)
    return runSim(turnsLeft - 1, num * 2024)
}