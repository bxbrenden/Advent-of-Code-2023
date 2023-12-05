package AdventDay05Part2

import java.io.File

fun readInput(filename: String): String {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList.joinToString(separator = "\n")
}

fun getStartingSeeds(puzzle:String): List<Long> {
    val startingSeeds = puzzle.split("\n\n")[0]
    println("Handling section: \"$startingSeeds\"")
    val spl = startingSeeds.split(": ")
    println("After split: \"$spl\"")
    val label = spl[0].replace(" map", "").trim()
    println("Label: \"$label\"")
    val values = mutableListOf<Long>()
    spl[1]
        .trim()
        .split("\\s".toRegex())
            .filter {it.length > 0 && it != ""}
            .map {it.toLong()}
            .map {values.add(it)}
    var odds = mutableListOf<Long>()
    var evens = mutableListOf<Long>()
    values.mapIndexed {idx, value -> if (idx % 2 != 0) odds.add(value) else evens.add(value)}
    val zipped = evens.zip(odds)
    println("zipped: $zipped")
    val finalSeeds = mutableListOf<Long>()
    for (z in zipped) {
        val (first, second) = z
        for (num in first..first + second - 1) {
            finalSeeds.add(num)
        }
    }
    return finalSeeds
}

fun processMaps(puzzle: String): MutableMap<String, List<List<Long>>> {
    val allMaps = mutableMapOf<String, List<List<Long>>>()
    val sections = puzzle.split("\n\n")
    for (section in sections.slice(1..<sections.size)) {
        println("Handling section: \"$section\"")
        val spl = section.split(":")
        println("After split: \"$spl\"")
        val label = spl[0].replace(" map", "").trim()
        println("Label: \"$label\"")
        val values = mutableListOf<List<Long>>()
        spl[1]
            .split("\n")
            .forEach { line ->
                var innerVals = mutableListOf<Long>()
                line.split("\\s".toRegex())
                    .filter {it.length > 0 && it != ""}
                    .map {it.toLong()}
                    .map {innerVals.add(it)}
                if (innerVals.size > 0 ) values.add(innerVals)
            }
        println("Values: \"$values\"")
        allMaps[label] = values
    }
    return allMaps
}

fun translateViaMap(itemNumber: Long, rangeInfo: List<List<Long>>): Long {
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

fun fromSeedToLocation(seeds: List<Long>, allMaps: MutableMap<String, List<List<Long>>>) =
    seeds.map {translateViaMap(it, allMaps["seed-to-soil"]!!)}
        .map {translateViaMap(it, allMaps["soil-to-fertilizer"]!!)}
        .map {translateViaMap(it, allMaps["fertilizer-to-water"]!!)}
        .map {translateViaMap(it, allMaps["water-to-light"]!!)}
        .map {translateViaMap(it, allMaps["light-to-temperature"]!!)}
        .map {translateViaMap(it, allMaps["temperature-to-humidity"]!!)}
        .map {translateViaMap(it, allMaps["humidity-to-location"]!!)}
        .min()

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    val seeds = getStartingSeeds(puzzle)
    println(seeds)
    // val allMaps = processMaps(puzzle)
    // println("From seed to location ${fromSeedToLocation(seeds, allMaps)}")
}