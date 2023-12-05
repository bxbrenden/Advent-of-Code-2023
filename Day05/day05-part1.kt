package AdventDay05Part1

import java.io.File

fun readInput(filename: String): String {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList.joinToString(separator = "\n")
}

fun processMaps(puzzle: String): Int {
    val sections = puzzle.split("\n\n")
    for (section in sections) {
        println("Handling section: \"$section\"")
        val spl = section.split(":")
        println("After split: \"$spl\"")
        val label = spl[0].replace(" map", "").trim()
        println("Label: \"$label\"")
        val values = mutableListOf<List<Int>>()
        spl[1]
            .split("\n")
            .forEach { line ->
                var innerVals = mutableListOf<Int>()
                line.split("\\s".toRegex())
                    .filter {it.length > 0 && it != ""}
                    .map {it.toInt()}
                    .map {innerVals.add(it)}
                if (innerVals.size > 0 ) values.add(innerVals)
            }
        println("Values: \"$values\"")
    }
    return 0
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    println(puzzle)
    processMaps(puzzle)
}