package AdventDay6Part1

import java.io.File

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun getRaceHistory(puzzle: List<String>): List<Pair<Int, Int>> {
    var raceHistory = mutableListOf<Int>()
    puzzle.map { it.split(":\\s".toRegex())[1]}
        .map {it.split("\\s".toRegex())}
        .forEach {line ->
            line.filter {it.length > 0 && it != ""}
                .map {it.toInt()}
                .map {raceHistory.add(it)}
        }
    val half = raceHistory.size / 2
    val firstHalf = raceHistory.slice(0..half - 1)
    val secondHalf = raceHistory.slice(half..<raceHistory.size)
    return firstHalf.zip(secondHalf)
}

fun optimizeSpeed(raceDuration: Int, recordTime: Int): Int {
    var waysToWin = 0
    for (chargeTime in 1..<raceDuration) {
        val raceTime = raceDuration - chargeTime
        val distance = chargeTime * raceTime
        if (distance > recordTime) {
            waysToWin += 1
        }
        println("Charge Time: $chargeTime, Race Time: $raceTime, Distance: $distance ")
    }
    println("Ways to Win: $waysToWin")
    return waysToWin
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    puzzle.map {println(it)}
    val raceHistory = getRaceHistory(puzzle)
    var product = 1
    raceHistory.forEach {(time, distance) -> product *= optimizeSpeed(time, distance)}
    println("ANSWER: $product")
} 