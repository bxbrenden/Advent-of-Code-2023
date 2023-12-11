package AdventDay09Part1

import java.io.File

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun getRowDiffs(row: MutableList<Int>): MutableList<Int>{
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

fun getRecursiveRowDiffs(row: MutableList<Int>, allDiffs: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
    allDiffs.add(row)
    val diffs = getRowDiffs(row)
    if (diffs.sum() == 0) {
        allDiffs.add(diffs)
        return allDiffs
    } else {
        return getRecursiveRowDiffs(diffs, allDiffs)
    }
}

fun reverseRecurseRowDiffs(revRows: MutableList<MutableList<Int>>, totalDiff: Int = 0, nextDiff: Int = 0, index: Int = 0): Int {
    val nextestDiff = nextDiff + revRows[index].last()
    val newTotal = totalDiff + nextestDiff
    if (index == revRows.size - 1) {
        return newTotal
    } else {
        return reverseRecurseRowDiffs(revRows, newTotal, nextDiff, index + 1)
    }
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    var answer = 0
    puzzle.forEach {line -> 
        var allDiffs = mutableListOf<MutableList<Int>>()
        var row = mutableListOf<Int>()
        line.trim()
            .split("\\s".toRegex())
            .map {it.toInt()}
            .map {row.add(it)}
        val recursiveRowDiffs = getRecursiveRowDiffs(row, allDiffs)
        var revved: MutableList<MutableList<Int>> = allDiffs.reversed().slice(1..<allDiffs.size).toMutableList()
        println(revved)
        val recRev = reverseRecurseRowDiffs(revved)
        println(recRev)
        answer += recRev
    }
    println("Answer: $answer")
}