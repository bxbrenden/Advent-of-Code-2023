
package AdventDay01Part1
import java.io.File

fun readInputFile(filename: String): MutableList<String> {
    val lineList = mutableListOf<String>()
    File(filename).useLines { lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun extractNumber(line: String): Int {
    var intList: MutableList<Char> = mutableListOf<Char>()
    for (c in line) {
        if (c.isDigit()) {
            intList.add(c)
        }
    }
    // val numStr: String = intList.joinToString(separator = "") // This doesn't work because it uses _all_ digits, not just first and last
    val numStr: String = intList.first().toString() + intList.last().toString()
    var converted = -1
    try {
        converted = numStr.toInt()
    } catch (nfe: NumberFormatException) {
        println("The String value \"$numStr\" could not be converted to Int")
    }
    println("$converted")
    return converted
}

fun main(args: Array<String>) {
    val filename: String = if (args.size > 0) args[0] else "sample_input.txt"
    val lines: List<String> = readInputFile(filename)
    var total: Int = 0
    lines.forEach {e -> total += extractNumber(e)}
    println("ANSWER: $total")
}