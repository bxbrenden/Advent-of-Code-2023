package AdventDay04Part1

import java.io.File
import kotlin.math.pow

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun processScratchOffs(cards: List<String>): MutableMap<Int, Int> {
    var results = mutableMapOf<Int, Int>()
    for (card in cards) {
        println("Processing card $card")
        val spl = card.split(" | ")
        println("spl: $spl")
        val left = spl[0].split(": ")
        println("left: $left")
        val cardNum = left[0].replace("Card ", "").trim().toInt()
        println("cardNum: $cardNum")
        val winningNums = left[1].trim().split("\\s".toRegex())
            .filter {it.length > 0}
            .map {it.toInt()}
            .toSet()
        println("winningNums: $winningNums")
        val myNums = spl[1].split("\\s".toRegex())
            .filter {it.length > 0}
            .map {println(it); it.toInt()}
            .toSet()
        println("myNums: $myNums")
        val common = winningNums.intersect(myNums)
        println("common: $common")
        if (common.size > 0) {
            val points = 2.toFloat().pow(common.size - 1).toInt()
            println("points: $points")
            results[cardNum] = points
        }
    }
    return results
} 

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    puzzle.forEach {println(it)}
    val results = processScratchOffs(puzzle)
    println(results.values.sum())
}