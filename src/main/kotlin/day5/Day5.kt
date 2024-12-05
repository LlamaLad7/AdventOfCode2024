package day5

import getInput
import split

private typealias Graph = Map<Int, List<Int>>

fun main() {
    day5part1(getInput(5, true, 1))
    day5part1(getInput(5, false, 1))
    day5part2(getInput(5, true, 2))
    day5part2(getInput(5, false, 2))
}

private fun day5part1(lines: List<String>) {
    val (graph, updates) = parseInput(lines)
    var total = 0
    for (update in updates) {
        if (update.isValid(graph)) {
            total += update.middle()
        }
    }
    println(total)
}

private fun day5part2(lines: List<String>) {
    val (graph, updates) = parseInput(lines)
    var total = 0
    for (update in updates) {
        if (update.isValid(graph)) {
            continue
        }
        val subGraph = graph.subGraph(update)
        val sorted = topoSort(update, subGraph)
        total += sorted.middle()
    }
    println(total)
}

private fun parseInput(lines: List<String>): Pair<Graph, List<List<Int>>> {
    val (rules, updates) = lines.split("")
    val graph = rules.map { it.split('|') }.groupBy({ (k, _) -> k.toInt() }, { (_, v) -> v.toInt() })
    return graph to updates.map { it.split(',').map(String::toInt) }
}

private fun List<Int>.isValid(graph: Graph): Boolean {
    val seen = hashSetOf<Int>()
    for (page in this) {
        seen.add(page)
        if (graph[page].orEmpty().any { it in seen }) {
            return false
        }
    }
    return true
}

private fun <T> List<T>.middle() = this[size / 2]

private fun Graph.subGraph(elements: List<Int>): Graph {
    val isRelevant = elements.toHashSet()::contains
    return filterKeys(isRelevant).mapValues { (_, v) -> v.filter(isRelevant) }
}

private fun topoSort(values: List<Int>, graph: Graph): List<Int> {
    val result = ArrayDeque<Int>()
    val seen = hashSetOf<Int>()

    fun dfs(node: Int) {
        if (!seen.add(node)) {
            return
        }
        for (after in graph[node].orEmpty()) {
            dfs(after)
        }
        result.addFirst(node)
    }

    for (value in values) {
        dfs(value)
    }
    return result
}