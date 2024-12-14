package day3

import utils.getInput

fun main() {
    day3part1(getInput(3, true, 1))
    day3part1(getInput(3, false, 1))
    day3part2(getInput(3, true, 2))
    day3part2(getInput(3, false, 2))
}

private val mulRegex = "mul\\((\\d+),(\\d+)\\)".toRegex()

private fun day3part1(lines: List<String>) {
    println(
        lines.flatMap {
            mulRegex.findAll(it)
        }
            .sumOf {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
    )
}

private val commandRegex = "(do(n't)?\\(\\))|(mul\\((\\d+),(\\d+)\\))".toRegex()

private fun day3part2(lines: List<String>) {
    var enabled = true
    var total = 0
    for (match in lines.flatMap { commandRegex.findAll(it) }) {
        val opcode = match.value.substringBefore('(')
        when {
            opcode == "do" -> enabled = true
            !enabled -> continue
            opcode == "don't" -> enabled = false
            opcode == "mul" -> total += match.groupValues[4].toInt() * match.groupValues[5].toInt()
        }
    }
    println(total)
}