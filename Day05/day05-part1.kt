package AdventDay05Part1

import java.io.File

fun readInput(filename: String): String {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList.joinToString(separator = "\n")
}

fun getStartingSeeds(puzzle:String): List<Int> {
    val startingSeeds = puzzle.split("\n\n")[0]
    println("Handling section: \"$startingSeeds\"")
    val spl = startingSeeds.split(": ")
    println("After split: \"$spl\"")
    val label = spl[0].replace(" map", "").trim()
    println("Label: \"$label\"")
    val values = mutableListOf<Int>()
    spl[1]
        .trim()
        .split("\\s".toRegex())
            .filter {it.length > 0 && it != ""}
            .map {it.toInt()}
            .map {values.add(it)}
    println("Values: \"$values\"")
    return values
}

fun processMaps(puzzle: String): MutableMap<String, List<List<Int>>> {
    val allMaps = mutableMapOf<String, List<List<Int>>>()
    val sections = puzzle.split("\n\n")
    for (section in sections.slice(1..<sections.size)) {
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
        allMaps[label] = values
    }
    // TODO: return something real
    return allMaps
}

fun translateViaMap(itemNumber: Int, rangeInfo: List<List<Int>>): Int {
    for (range in rangeInfo){
        val (destRangeStart, srcRangeStart, rangeLen) = range
        if (itemNumber in srcRangeStart..srcRangeStart + rangeLen) {
            return itemNumber + (destRangeStart - srcRangeStart)
        } else {
            continue
        }
    }
    return itemNumber
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    println(puzzle)
    // val seeds = getStartingSeeds(puzzle)
    // println("Seeds: $seeds")
    val allMaps = processMaps(puzzle)
    println(allMaps)
    // val rangeInfo = listOf(listOf(50, 98, 2), listOf(52, 50, 48))
    // val soilNumber = translateViaMap(79, rangeInfo)
    // println("Soil number for seed 79: $soilNumber")
}