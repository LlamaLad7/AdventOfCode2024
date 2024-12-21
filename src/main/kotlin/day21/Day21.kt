package day21

import com.aballano.mnemonik.memoize
import utils.getInput
import utils.ints

fun main() {
    day21part1(getInput(21, true, 1))
    day21part1(getInput(21, false, 1))
    day21part2(getInput(21, true, 2))
    day21part2(getInput(21, false, 2))
}

private fun day21part1(lines: List<String>) {
    println(lines.sumOf { it.complexity(2) })
}

private fun day21part2(lines: List<String>) {
    println(lines.sumOf { it.complexity(25) })
}

private fun String.complexity(depth: Int): Long {
    return pressesFor(this.map { it.keypadPos() }, depth, true) * this.ints().single()
}

private val pressesFor = ::pressesFor0.memoize()

private fun pressesFor0(points: List<Point>, depth: Int, numberKeypad: Boolean): Long {
    var result = 0L
    var start = if (numberKeypad) 'A'.keypadPos() else Button.A.pos
    for (end in points) {
        val presses = mutableListOf<Button>()

        val rights = end.x - start.x
        val ups = start.y - end.y
        val downs = end.y - start.y
        val lefts = start.x - end.x
        repeat(rights) {
            presses.add(Button.RIGHT)
        }
        repeat(ups) {
            presses.add(Button.UP)
        }
        repeat(downs) {
            presses.add(Button.DOWN)
        }
        repeat(lefts) {
            presses.add(Button.LEFT)
        }

        if (depth == 0) {
            result += presses.size + 1
            start = end
            continue
        }

        val options = mutableListOf(presses)
        val hole = if (numberKeypad) Point(0, 3) else Point(0, 0)
        if (start.x == hole.x && end.y == hole.y || start.y == hole.y && end.x == hole.x) {
            // No choice of direction, going in the opposite order would touch the hole
        } else {
            // Can do the opposite order
            options.add(presses.asReversed())
        }
        val bestPresses = options.minOf { option ->
            pressesFor((option + Button.A).map { it.pos }, depth - 1, false)
        }

        result += bestPresses
        start = end
    }

    return result
}

private fun Char.keypadPos() = when (this) {
    '7' -> Point(0, 0)
    '8' -> Point(1, 0)
    '9' -> Point(2, 0)
    '4' -> Point(0, 1)
    '5' -> Point(1, 1)
    '6' -> Point(2, 1)
    '1' -> Point(0, 2)
    '2' -> Point(1, 2)
    '3' -> Point(2, 2)
    '0' -> Point(1, 3)
    'A' -> Point(2, 3)
    else -> error("Invalid keypad char $this")
}

private data class Point(val x: Int, val y: Int)

private enum class Button(x: Int, y: Int) {
    UP(1, 0),
    A(2, 0),
    LEFT(0, 1),
    DOWN(1, 1),
    RIGHT(2, 1);

    val pos = Point(x, y)
}