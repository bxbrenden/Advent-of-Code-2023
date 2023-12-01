
package AdventDay01Part2
import AdventDay01Part1.readInputFile
import java.io.File

fun extractNumberAndText(line: String): Int {
    println("Testing line: $line")
    var intList: MutableList<Char> = mutableListOf<Char>()
    val numRegex = "(one|two|three|four|five|six|seven|eight|nine)".toRegex()
    // val textToInt = hashMapOf(
    //     "one" to 1
    //     "two" to 2
    //     "three" to 3
    //     "four" to 4
    //     "five" to 5
    //     "six" to 6
    //     "seven" to 7
    //     "eight" to 8
    //     "nine" to 9
    // )
    val firstTextNum: MatchResult? = numRegex.find(line)
    if (firstTextNum != null) {
        println("The first occurrence of a textual number is ${firstTextNum.value} at range ${firstTextNum.range}")
    }
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
    lines.forEach {e -> total += extractNumberAndText(e)}
    println("ANSWER: $total")
}