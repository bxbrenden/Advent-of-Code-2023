package AdventDay02Part2

import java.io.File


fun readInputFile(filename: String): MutableList<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines { lines -> lines.forEach {lineList.add(it)} }
    return lineList
}

fun getMinColor(game: String, color: String): Int {
    val gameId = game.split(": ")[0].replace("Game ", "").toInt()
    val gamesList: List<String> = game.split(": ")[1].split("; ")
    var colorMin = 0
    for (game_ in gamesList) {
        val rounds = game_.split(", ")
        for (round in rounds) {
            if (round.contains(color)) {
                // println("Game $game_ contains $color? ${round.contains(color)}")
                val numColor = round.split(' ')[0].toInt()
                // println("Game $round contains $numColor $color cubes")
                if (numColor > colorMin) {
                    colorMin = numColor
                }
            }
        }
    }
    // println("The minumum $color cubes to make game \"$game\" possible was $colorMin")
    return colorMin
}

fun getCubesPower(game: String): Int {
    val gameId = game.split(": ")[0].replace("Game ", "").toInt()
    val minBlue = getMinColor(game, "blue")
    val minGreen = getMinColor(game, "green")
    val minRed = getMinColor(game, "red")
    val power = minBlue * minGreen * minRed
    println("The power for game $gameId is $power (minBlue $minBlue * minGreen $minGreen * minRed $minRed)")
    return power
}


fun main(args: Array<String>) {
    val inputFile: String = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInputFile(inputFile)
    // puzzle.forEach {line -> println(line)}
    val powerSum = puzzle.map {getCubesPower(it)}
        .sum()
    println("ANSWER: $powerSum")
}