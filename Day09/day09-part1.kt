package AdventDay09Part1

import java.io.File

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun getRowDiffs(row: List<Int>): List<Int>{
    var diffs = mutableListOf<Int>()
    for ((i, num) in row.withIndex()) {
        if (i == row.size - 1) {
            break
        } else {
            val currentDiff = row[i + 1] - row[i]
            diffs.add(currentDiff)
        }
    }
    return diffs
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    var allDiffs = mutableListOf<List<Int>>()
    puzzle.forEach {line -> 
        var row = mutableListOf<Int>()
        line.trim()
            .split("\\s".toRegex())
            .map {it.toInt()}
            .map {row.add(it)}
        val rowDiffs = getRowDiffs(row)
        allDiffs.add(rowDiffs)
    }
    println(allDiffs)
}