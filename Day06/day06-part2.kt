package AdventDay6Part2

import java.io.File

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun getRaceHistory(puzzle: List<String>): List<Pair<Long, Long>> {
    var raceHistory = mutableListOf<Long>()
    puzzle.map { it.split(":\\s".toRegex())[1]}
        .map {it.replace("\\s".toRegex(), "")}
        .map {it.toLong()}
        .map {raceHistory.add(it)}
        
    val half = raceHistory.size / 2
    val firstHalf = raceHistory.slice(0..half - 1)
    val secondHalf = raceHistory.slice(half..<raceHistory.size)
    return firstHalf.zip(secondHalf)
}

// fun binarySearchOptimal(raceDuration: Long, recordTime: Long): Int {
//     var waysToWin = 0
//     val firstVal = 1
//     val middleVal = raceDuration / 2
//     val lastVal = raceDuration - 1
// }

// fun testChargeTime(chargeTime: Long, raceDuration: Long, recordTime: Long): Boolean {
//     val raceTime = raceDuration - chargeTime
//     val distance = chargeTime * raceTime
//     println("Charge Time: $chargeTime, Race Time: $raceTime, Distance: $distance ")
//     return distance > recordTime
// }

fun optimizeSpeed(raceDuration: Long, recordTime: Long): Int {
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
    println(raceHistory)
    var product = 1
    raceHistory.forEach {(time, distance) -> product *= optimizeSpeed(time, distance)}
    println("ANSWER: $product")
} 