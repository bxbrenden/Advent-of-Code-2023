package AdventDay07Part1

import java.io.File

enum class Card(val rank: Int) {
    A(13),
    K(12),
    Q(11),
    J(10),
    T(9),
    _9(8),
    _8(7),
    _7(6),
    _6(5),
    _5(4),
    _4(3),
    _3(2),
    _2(1)
}

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

fun getHandsFromPuzzle(puzzle: List<String>): MutableList<Pair<List<Card>, Int>> {
    fun charToCard(c: Char): Card {
        val card = when (c) {
            'A' -> Card.A
            'K' -> Card.K
            'Q' -> Card.Q
            'J' -> Card.J
            'T' -> Card.T
            '9' -> Card._9
            '8' -> Card._8
            '7' -> Card._7
            '6' -> Card._6
            '5' -> Card._5
            '4' -> Card._4
            '3' -> Card._3
            '2' -> Card._2
            else -> throw IllegalStateException("Unknown card char $c")
        }
        return card
    }

    var allHands = mutableListOf<Pair<List<Card>, Int>>()
    puzzle.forEach {line ->
        var cards = mutableListOf<Card>()
        line.split("\\s".toRegex())[0]
            .map { charToCard(it) }
            .map { cards.add(it)}
        val bid = line.split("\\s".toRegex())[1]
            .trim().toInt()
        allHands.add(Pair(cards, bid))
    }
    return allHands
}

// fun categorizeHand(hand: String)

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    puzzle.forEach {println(it)}
    val allHands = getHandsFromPuzzle(puzzle)
    println(allHands)
    println("Testing: ${allHands[0].first[0].rank}")
}