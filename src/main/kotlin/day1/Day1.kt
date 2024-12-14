package day1

import utils.getInput
import kotlin.math.abs

fun main() {
    day1part1(getInput(1, true, 1))
    day1part1(getInput(1, false, 1))
    day1part2(getInput(1, true, 2))
    day1part2(getInput(1, false, 2))
}

private fun day1part1(lines: List<String>) {
    val left = lines.map { line -> line.split("   ")[0].toInt() }
    val right = lines.map { line -> line.split("   ")[1].toInt() }
    println(left.sorted().zip(right.sorted()).sumOf { (a, b) -> abs(a - b) })
}

private fun day1part2(lines: List<String>) {
    val left = lines.map { line -> line.split("   ")[0].toInt() }
    val right = lines.map { line -> line.split("   ")[1].toInt() }.groupingBy { it }.eachCount()

    println(left.sumOf { it * (right[it] ?: 0) })
}