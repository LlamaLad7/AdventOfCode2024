package utils

import java.io.File

fun getInput(day: Int, test: Boolean, part: Int): List<String> {
    fun withSuffix(suffix: String) =
        File(".", "src/main/resources/${if (test) "test_inputs" else "inputs"}/day$day$suffix.txt")
            .takeIf { it.exists() }
            ?.readText() ?: ""
    val suffix = if (part == 2) "_part2" else ""
    val text = withSuffix(suffix).ifBlank { withSuffix("") }
    return text.lines()
}

fun <T> List<T>.split(separator: T): List<List<T>> {
    val result = mutableListOf(mutableListOf<T>())
    for (item in this) {
        if (item == separator) {
            result.add(mutableListOf())
        } else {
            result.last().add(item)
        }
    }
    return result
}

private val intRegex = "[+-]?\\d+".toRegex()

fun String.ints() = intRegex.findAll(this).map { it.value.toInt() }.toList()
fun String.longs() = intRegex.findAll(this).map { it.value.toLong() }.toList()