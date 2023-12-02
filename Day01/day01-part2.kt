package AdventDay01Part2x
import java.io.File

val textToInt = hashMapOf(
    "one" to 1,
    "two" to 2,
    "three" to 3,
    "four" to 4,
    "five" to 5,
    "six" to 6,
    "seven" to 7,
    "eight" to 8,
    "nine" to 9,
)


fun readInputFile(filename: String): MutableList<String> {
    val lineList = mutableListOf<String>()
    File(filename).useLines { lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

// The two functions below are adapted from https://stackoverflow.com/a/62191093/11536033
fun ignoreCaseOpt(ignoreCase: Boolean) =
    if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

fun String?.indexesOf(pat: String, ignoreCase: Boolean = true): List<Int> =
    pat.toRegex(ignoreCaseOpt(ignoreCase))
        .findAll(this?: "")
        .map { it.range.first }
        .toList()

fun findFirstAndLastNum(line: String): Int {
    var earliest: Int? = null
    var last: Int? = null
    var earliestDigitIndex: Int? = null
    var earliestDigit: Int? = null
    var lastDigitIndex: Int? = null
    var lastDigit: Int? = null
    for ((i, c) in line.withIndex()) {
        if (c.isDigit()) {
            if (earliestDigitIndex == null && earliestDigit == null && lastDigitIndex == null && lastDigit == null) {
                earliestDigitIndex = i
                earliestDigit = c.digitToInt()
                lastDigitIndex = i
                lastDigit = c.digitToInt()
                break
            }
        }
    }
    for ((i, c) in line.withIndex()) {
        if (c.isDigit()) {
            if (lastDigitIndex != null && lastDigit != null) {
                if (i > lastDigitIndex) {
                    lastDigitIndex = i
                    lastDigit = c.digitToInt()
                }
            } else{
                lastDigitIndex = i
                lastDigit = c.digitToInt()
            }
        }
    }
    var earliestTextIndex: Int? = null
    var earliestTextNum: Int? = null
    var lastTextIndex: Int? = null
    var lastTextNum: Int? = null
    for (num in listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")) {
        val indices: List<Int> = line.indexesOf(num)
        if (indices.size > 0) {
            if (earliestTextIndex == null && earliestTextNum == null) {
                earliestTextIndex = indices[0]
                earliestTextNum = textToInt[num]
                lastTextIndex = indices[0]
                lastTextNum = textToInt[num]
            }
            for (index in indices) {
                if (index < earliestTextIndex!!) {
                    earliestTextIndex = index
                    earliestTextNum = textToInt[num]
                }
                if (index > lastTextIndex!!) {
                    lastTextIndex = index
                    lastTextNum = textToInt[num]
                }
            }
        }
    }
    // Handle case where earliestDigit is defined and earliestTextNum is null
    if (earliestTextNum == null && earliestDigit != null) {
        println("There were no occurrences of textual numbers in line \"$line\"")
        earliest = earliestDigit
    // Handle case where earliestTextNum is defined and earliestDigit is null
    } else if (earliestTextNum != null && earliestDigit == null){
        println("There were no occurrences of numerical digits in line \"$line\"")
        println("The earliest textual number in line \"$line\" is $earliestTextNum at index $earliestTextIndex")
        earliest = earliestTextNum
    // Handle case where earliestTextNum AND earliestDigit are defined and not null
    } else if (earliestTextNum != null && earliestDigit != null) {
        earliest = if (earliestTextIndex!! < earliestDigitIndex!!) earliestTextNum else earliestDigit
        println("Both earlies were defined, but the earliest number overall for line \"$line\" was $earliest")
    // Handle case where both earlies are null by crashing
    } else if (earliestTextNum == null && earliestDigit == null) {
        throw IllegalStateException("No earliest digits found!")
    }

    // Handle case where lastTextNum is null but lastDigit is defined
    if (lastDigit != null && lastTextNum == null){
        println("Only the last numerical digit was defined as $lastDigit, but there was no last textual number")
        last = lastDigit
    // Handle case where lastDigit is null but lastTextNum is defined
    } else if (lastTextNum != null && lastDigit == null) {
        println("Only the last textual number was defined as $lastTextNum, but the last numerical digit was not defined")
        last = lastTextNum
    // Handle case where both lasts are defined and not null
    } else if (lastTextNum != null && lastDigit != null) {
        last = if (lastTextIndex!! > lastDigitIndex!!) lastTextNum else lastDigit
        val lastIndex = if (lastTextIndex > lastDigitIndex) lastTextIndex else lastDigitIndex
        println("Both lasts were defined, but the last overall was $last at index $lastIndex")
    // Handle case where neither last is defined by crashing
    } else if (lastTextNum == null && lastDigit == null){
        throw IllegalStateException("No last digits found!")
    }

    println("Line: \"$line\", First: $earliest, Last: $last")
    val numForLine = "$earliest$last".toInt()
    println("Num. for line: $numForLine")
    return numForLine
    // return Pair(earliest!!, last!!)
}

fun main(args: Array<String>) {
    val inputFile = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInputFile(inputFile)
    // puzzle.forEach {findFirstAndLastNum(it)}
    val answer = puzzle.map {findFirstAndLastNum(it)}
        .sum()
    println("ANSWER: $answer")
}
