package day7

import getInput
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

fun main() {
    day7part1(getInput(7, true, 1))
    day7part1(getInput(7, false, 1))
    day7part2(getInput(7, true, 2))
    day7part2(getInput(7, false, 2))
}

private fun day7part1(lines: List<String>) {
    val equations = parseInput(lines)
    println(equations.asSequence().filter { it.solvable(false) }.sumOf { it.target })
}

private fun day7part2(lines: List<String>) {
    val equations = parseInput(lines)
    println(equations.asSequence().filter { it.solvable(true) }.sumOf { it.target })
}

private fun parseInput(lines: List<String>) = lines.map { line ->
    val (target, rest) = line.split(": ")
    val operands = rest.split(' ')
    Equation(target.toLong(), operands.map { it.toLong() }.toPersistentList())
}

private data class Equation(val target: Long, val operands: PersistentList<Long>) {
    fun solvable(canConcat: Boolean): Boolean {
        if (operands.size == 1) {
            return operands.single() == target
        }
        val (first, second) = operands
        if (first > target) {
            return false
        }
        val rest = operands.removeAt(0).removeAt(0)
        if (longArrayOf(first + second, first * second).any {
                Equation(target, rest.add(0, it)).solvable(canConcat)
            }) {
            return true
        }
        if (canConcat && first.numDigits() + second.numDigits() <= target.numDigits()) {
            return Equation(target, rest.add(0, "$first$second".toLong())).solvable(canConcat)
        }
        return false
    }
}

private fun Long.numDigits() = toString().length