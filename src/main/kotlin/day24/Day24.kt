package day24

import utils.getInput
import utils.ints
import utils.split

fun main() {
    day24part1(getInput(24, true, 1))
    day24part1(getInput(24, false, 1))
//    day24part2(getInput(24, true, 2))
    day24part2(getInput(24, false, 2).toMutableList())
}

private fun day24part1(lines: List<String>) {
    val system = parseInput(lines)
    var result = 0L
    for ((name, wire) in system) {
        if (!name.startsWith('z')) {
            continue
        }
        val shift = name.ints().single()
        if (wire.evaluate(system::getValue) == 1) {
            result = result or (1L shl shift)
        }
    }
    println(result)
}

private fun day24part2(lines: MutableList<String>) {
    lines.replace("pkm XOR cvq -> vmv", "pkm XOR cvq -> z07")
    lines.replace("y07 AND x07 -> z07", "y07 AND x07 -> vmv")

    lines.replace("smh XOR bqn -> kfm", "smh XOR bqn -> z20")
    lines.replace("jbq OR hds -> z20", "jbq OR hds -> kfm")

    lines.replace("jsg XOR vwh -> hnv", "jsg XOR vwh -> z28")
    lines.replace("vwh AND jsg -> z28", "vwh AND jsg -> hnv")

    lines.replace("x35 AND y35 -> tqr", "x35 AND y35 -> hth")
    lines.replace("x35 XOR y35 -> hth", "x35 XOR y35 -> tqr")

    val involved = listOf("vmv", "z07", "kfm", "z20", "hnv", "z28", "tqr", "hth")
    println(involved.sorted().joinToString(","))

//    val system = parseInput(lines)
//    println("digraph G {")
//    for ((name, wire) in system) {
//        when (wire) {
//            is Wire.Constant -> {}
//            is Wire.Binary -> {
//                val label = when (wire) {
//                    is Wire.And -> "AND"
//                    is Wire.Or -> "OR"
//                    is Wire.Xor -> "XOR"
//                }
//                println("$name [label=\"$label\"]")
//                println("${wire.left} -> $name")
//                println("${wire.right} -> $name")
//                if (name.startsWith('z')) {
//                    val bit = name.ints().single()
//                    println("$name -> output$bit")
//                }
//            }
//        }
//    }
//    println("}")
//
//    // Heuristics to determine the swapped gates
//    val seen = hashSetOf<String>()
//    for (x in (1L shl 44) downTo 0) {
//        for (y in (1L shl 44) downTo 0) {
//            val z = x + y
//            for ((wire, output) in evaluate(system, x, y)) {
//                val shift = wire.ints().single()
//                val bit = ((z shr shift) and 1).toInt()
//                if (output != bit && seen.add(wire)) {
//                    println("$wire is wrong")
//                }
//            }
//        }
//    }
}

private fun MutableList<String>.replace(original: String, new: String) {
    set(indexOf(original), new)
}

private fun evaluate(system: Map<String, Wire>, x: Long, y: Long): Map<String, Int> {
    fun lookup(name: String): Wire {
        if (name.startsWith('x')) {
            val shift = name.ints().single()
            return Wire.Constant(((x ushr shift) and 1L).toInt())
        }
        if (name.startsWith('y')) {
            val shift = name.ints().single()
            return Wire.Constant(((y ushr shift) and 1L).toInt())
        }
        return system.getValue(name)
    }

    val result = sortedMapOf<String, Int>()
    for ((name, wire) in system) {
        if (!name.startsWith('z')) {
            continue
        }
        result[name] = wire.evaluate(::lookup)
    }
    return result
}

private val binaryRegex = "(.+) (AND|OR|XOR) (.+) -> (.+)".toRegex()

private fun parseInput(lines: List<String>): Map<String, Wire> {
    val result = mutableMapOf<String, Wire>()
    val (constants, binaries) = lines.split("")
    for ((name, value) in constants.asSequence().map { it.split(": ") }) {
        result[name] = Wire.Constant(value.toInt())
    }

    for ((_, left, op, right, name) in binaries.asSequence().map { binaryRegex.find(it)!!.groupValues }) {
        val ctor = when (op) {
            "AND" -> Wire::And
            "OR" -> Wire::Or
            "XOR" -> Wire::Xor
            else -> error("Invalid op $op")
        }
        result[name] = ctor(left, right)
    }
    return result
}

private sealed interface Wire {
    fun evaluate(lookup: (String) -> Wire): Int

    data class Constant(val value: Int) : Wire {
        override fun evaluate(lookup: (String) -> Wire) = value
    }

    sealed class Binary(var left: String, var right: String) : Wire {
        abstract fun combine(left: Int, right: Int): Int

        override fun evaluate(lookup: (String) -> Wire) =
            combine(lookup(left).evaluate(lookup), lookup(right).evaluate(lookup))
    }

    class And(left: String, right: String) : Binary(left, right) {
        override fun combine(left: Int, right: Int) = left and right
    }

    class Or(left: String, right: String) : Binary(left, right) {
        override fun combine(left: Int, right: Int) = left or right
    }
    class Xor(left: String, right: String) : Binary(left, right) {
        override fun combine(left: Int, right: Int) = left xor right
    }
}