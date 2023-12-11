package AdventDay09Part1

import java.io.File

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun getRowDiffs(row: List<Int>): List<Int>{
    var diffs = mutableListOf<Int>()
    for ((i, _) in row.withIndex()) {
        if (i == row.size - 1) {
            break
        } else {
            val currentDiff = row[i + 1] - row[i]
            diffs.add(currentDiff)
        }
    }
    return diffs
}

fun getRecursiveRowDiffs(row: List<Int>, allDiffs: MutableList<List<Int>>): MutableList<List<Int>> {
    allDiffs.add(row)
    val diffs = getRowDiffs(row)
    if (diffs.sum() == 0) {
        allDiffs.add(diffs)
        return allDiffs
    } else {
        return getRecursiveRowDiffs(diffs, allDiffs)
    }
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    puzzle.forEach {line -> 
        var allDiffs = mutableListOf<List<Int>>()
        var row = mutableListOf<Int>()
        line.trim()
            .split("\\s".toRegex())
            .map {it.toInt()}
            .map {row.add(it)}
        val recursiveRowDiffs = getRecursiveRowDiffs(row, allDiffs)
        println(recursiveRowDiffs)
    }
}