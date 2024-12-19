import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import java.awt.Desktop
import java.io.File
import java.net.URI

private const val DAY = 19
private val dotenv = dotenv()

fun main() = runBlocking {
    val client = HttpClient()
    val input = client.get("https://adventofcode.com/2024/day/$DAY/input") {
        cookie("session", dotenv["ADVENT_OF_CODE_SESSION"])
    }.bodyAsText().trimEnd()
    if (input.writeToResource("inputs", false)) {
        return@runBlocking
    }
    Desktop.getDesktop().browse(URI("https://adventofcode.com/2024/day/$DAY"))
    println("Test Input:")
    val lines = mutableListOf<String>()
    do {
        val line = readln().takeIf { it != "done" }
        line?.let(lines::add)
    } while (line != null)
    lines.joinToString("\n").writeToResource("test_inputs", true)
    getStubCode().writeToCode("day$DAY/Day$DAY.kt")
}

private fun getStubCode() = """
    package dayDAY

    import utils.getInput

    fun main() {
        dayDAYpart1(getInput(DAY, true, 1))
        dayDAYpart1(getInput(DAY, false, 1))
        dayDAYpart2(getInput(DAY, true, 2))
        dayDAYpart2(getInput(DAY, false, 2))
    }

    private fun dayDAYpart1(lines: List<String>) {
        
    }

    private fun dayDAYpart2(lines: List<String>) {
        
    }
""".trimIndent().replace("DAY", DAY.toString())

private fun String.writeToResource(directory: String, part2: Boolean): Boolean {
    val file = File(".", "src/main/resources/$directory/day$DAY.txt")
    if (file.exists()) {
        return true
    }
    File(file.parent).mkdirs()
    file.delete()
    file.writeText(this)
    if (part2) {
        File(".", "src/main/resources/$directory/day${DAY}_part2.txt").createNewFile()
    }
    return false
}

private fun String.writeToCode(path: String) {
    val file = File(".", "src/main/kotlin/$path")
    if (file.exists()) {
        return
    }
    File(file.parent).mkdirs()
    file.delete()
    file.writeText(this)
}

