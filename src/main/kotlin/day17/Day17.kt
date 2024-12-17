package day17

import utils.getInput
import utils.ints
import utils.z3.z3Optimize

fun main() {
    day17part1(getInput(17, true, 1))
    day17part1(getInput(17, false, 1))
//    day17part2(getInput(17, true, 2))
    day17part2(getInput(17, false, 2))
}

private fun day17part1(lines: List<String>) {
    var printed = false
    val program = Program(lines) {
        if (printed) {
            print(",$it")
        } else {
            printed = true
            print(it)
        }
    }
    program.execute()
    println()
}

private fun day17part2(lines: List<String>) {
    val expected = lines.last().ints()
    val solution = z3Optimize {
        val startA = "a".z3BV()
        var a = startA
        for (output in expected) {
            var b = a and 7.z3BV()
            b = b xor 2.z3BV()
            val c = a ushr b
            b = b xor c
            a = a ushr 3.z3BV()
            b = b xor 7.z3BV()
            constraints((b and 7.z3BV()) eq output.z3BV())
        }
        constraints(a eq 0.z3BV())

        minimize(startA)
        check(solve())
        startA.solution
    }
    println(solution)
    check(part2Impl(solution.toLong(), expected.toIntArray()))
}

// Reference implementation
private fun part2Impl(aIn: Long, expected: IntArray): Boolean {
    var nextExpected = 0
    var a = aIn

    do {
        var b = a and 7
        b = b xor 2
        val c = a shr b.toInt()
        b = b xor c
        a = a shr 3
        b = b xor 7
        if ((b and 7).toInt() != expected[nextExpected++]) {
            return false
        }
    } while (a != 0L)
    return nextExpected == expected.size
}

private class Program(
    private var a: Int,
    private var b: Int,
    private var c: Int,
    private val program: List<Int>,
    private val output: (Int) -> Unit
) {
    private var pc = 0

    constructor(lines: List<String>, output: (Int) -> Unit) : this(
        lines[0].ints().single(),
        lines[1].ints().single(),
        lines[2].ints().single(),
        lines[4].ints(),
        output
    )

    fun execute() {
        while (pc in program.indices) {
            check(pc % 2 == 0)
            val opcode = program[pc]
            val operand = program[pc + 1]
            pc += 2
            runInstruction(opcode, operand)
        }
    }

    private fun runInstruction(opcode: Int, operand: Int) {
        when (opcode) {
            0 -> a = a shr loadCombo(operand)
            1 -> b = b xor operand
            2 -> b = loadCombo(operand) % 8
            3 -> if (a != 0) pc = operand
            4 -> b = b xor c
            5 -> output(loadCombo(operand) % 8)
            6 -> b = a shr loadCombo(operand)
            7 -> c = a shr loadCombo(operand)
            else -> error("Invalid opcode $opcode")
        }
    }

    private fun loadCombo(combo: Int) = when (combo) {
        in 0..3 -> combo
        4 -> a
        5 -> b
        6 -> c
        else -> error("Illegal combo $combo")
    }
}