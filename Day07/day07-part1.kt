package AdventDay07Part1

import java.io.File

fun readInput(filename: String): List<String> {
    var lineList = mutableListOf<String>()
    File(filename).useLines {lines -> lines.forEach {lineList.add(it)}}
    return lineList
}

enum class Card(val rank: Int, val cardName: Char) {
    A(14, 'A'),
    K(13, 'K'),
    Q(12, 'Q'),
    J(11, 'J'),
    T(10, 'T'),
    _9(9, '9'),
    _8(8, '8'),
    _7(7, '7'),
    _6(6, '6'),
    _5(5, '5'),
    _4(4, '4'),
    _3(3, '3'),
    _2(2, '2')
}

enum class HandType(val strength: Int) {
    FiveOfAKind(7),
    FourOfAKind(6),
    FullHouse(5),
    ThreeOfAKind(4),
    TwoPair(3),
    OnePair(2),
    HighCard(1),
}

class Hand(val cardsList: String, val bidStr: String) {
    val cards: List<Card>
    val bid: Int
    val handType: HandType
    val strength: Int

    override fun toString(): String {
        return this.cards.map {it.cardName}.joinToString(separator = "")
    }

    fun charToCard(cl: String): List<Card> {
        var cards = mutableListOf<Card>()
        for (c in cl) {
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
            cards.add(card)
        }
        return cards
    }

    init {
        this.cards = charToCard(cardsList)
        this.bid = bidStr.toInt()
        this.handType = categorizeHand(this)
        this.strength = this.handType.strength
    }
}



fun getHandsFromPuzzle(puzzle: List<String>): MutableList<Hand> {
    var allHands = mutableListOf<Hand>()
    puzzle.forEach {line ->
        val (cards, bid) = line.split("\\s".toRegex())
        allHands.add(Hand(cards, bid))
    }
    return allHands
}

fun categorizeHand(hand: Hand): HandType {
    val grouping = hand.cards.groupingBy {it.rank}.eachCount()
        .filter {it.value > 1}
    val numPairs = grouping.values.filter {it == 2}.count()
    val numTrips = grouping.values.filter {it == 3}.count()
    val numQuads = grouping.values.filter {it == 4}.count()
    val numQuints = grouping.values.filter {it == 5}.count()
    var handType = HandType.HighCard
    if (numQuints == 1) {
        handType = HandType.FiveOfAKind
    } else if (numQuads == 1) {
        handType = HandType.FourOfAKind
    } else if (numTrips == 1) {
        if (numPairs == 1) {
            handType = HandType.FullHouse
        } else {
            handType = HandType.ThreeOfAKind
        }
    } else if (numPairs > 0) {
        handType = when (numPairs) {
            1 -> HandType.OnePair
            2 -> HandType.TwoPair
            else -> throw IllegalStateException("Number of pairs was not 0, 1, or 2: $numPairs")
        }
    }
    return handType
}

fun calcWinnings(allHands: List<Hand>): Int {
    var winnings = 0
    val sorted: List<Hand> = allHands.sortedWith(
        compareBy(
            {it.strength},
            {it.cards[0].rank},
            {it.cards[1].rank},
            {it.cards[2].rank},
            {it.cards[3].rank},
            {it.cards[4].rank},
        )
    ).reversed()
    println(sorted)
    sorted.mapIndexed{ index, hand -> 
        val rank = allHands.size - index
        winnings += hand.bid * rank
    }
    return winnings
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    val allHands = getHandsFromPuzzle(puzzle)
    println("Winnings: ${calcWinnings(allHands)}")
}