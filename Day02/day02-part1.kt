package AdventDay02Part1

import java.io.File


fun readInputFile(filename: String): MutableList<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines { lines -> lines.forEach {lineList.add(it)} }
    return lineList
}

fun getMaxColor(game: String, color: String): Int {
    val gameId = game.split(": ")[0].replace("Game ", "").toInt()
    val gamesList: List<String> = game.split(": ")[1].split("; ")
    var colorMax = 0
    for (game_ in gamesList) {
        val rounds = game_.split(", ")
        for (round in rounds) {
            if (round.contains(color)) {
                // println("Game $game_ contains $color? ${round.contains(color)}")
                val numColor = round.split(' ')[0].toInt()
                // println("Game $round contains $numColor $color cubes")
                if (numColor > colorMax) {
                    colorMax = numColor
                }
            }
        }
    }
    // println("The max $color value for game \"$game\" was $colorMax")
    return colorMax
}

fun gameIsPossible(game: String): Pair<Int, Boolean> {
    val maxRed = 12
    val maxGreen = 13
    val maxBlue = 14
    val gameId = game.split(": ")[0].replace("Game ", "").toInt()
    if (getMaxColor(game, "blue") <= maxBlue && getMaxColor(game, "green") <= maxGreen && getMaxColor(game, "red") <= maxRed) {
        println("Game \"$game\" is possible ✅")
        return Pair(gameId, true)
    } else {
        println("Game \"$game\" is impossible ❌")
        return Pair(gameId, false)
    }
}

fun main(args: Array<String>) {
    val inputFile: String = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInputFile(inputFile)
    // puzzle.forEach {line -> println(line)}
    val idSum = puzzle.map {gameIsPossible(it)}
        .filter {(_, y) -> y == true}
        .map {(x, _) -> x}
        .sum()
    println("ANSWER: $idSum")
}


// Tried to do it the "FP" way, but I got tripped up by List<List<String>> when running `map` to split strings
// fun gameIsPossible(game: String): Pair<Int, Boolean> {
//     val gameId = game.split(": ")[0].replace("Game ", "").toInt()
//     val gamesList: List<String> = game.split(": ")[1].split("; ")
//     val greenGamesMax: Int = gamesList
//         .filter {it.contains("green")}
//         .map {it.split(", ")}
//         .map {it.split(" ")[0].toInt()}
//         .max()
//     val redGamesMax: Int = gamesList
//         .map {it.split(", ")}
//         .filter {it.contains("red")}
//         .map {it.split(' ')[0]}
//         .map {it.toInt()}
//         .max()
//     val blueGamesMax: Int = gamesList
//         .map {it.split(", ")}
//         .filter {it.contains("blue")}
//         .map {it.split(' ')[0]}
//         .map {it.toInt()}
//         .max()
//     println("Maximums for game $game:")
//     println("Red: $redGamesMax, Green: $greenGamesMax, Blue: $blueGamesMax")
//     val possible = redGamesMax <= maxRed && greenGamesMax <= maxGreen && blueGamesMax <= maxBlue
//     println("Game \"$game\" is possible? $possible.")
//     return Pair(gameId, possible)
// }