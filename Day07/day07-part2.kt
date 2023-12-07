package AdventDay07Part2

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
    T(11, 'T'),
    _9(10, '9'),
    _8(9, '8'),
    _7(8, '7'),
    _6(7, '6'),
    _5(6, '5'),
    _4(5, '4'),
    _3(4, '3'),
    _2(3, '2'),
    J(2, 'J'),
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
    var numJacks = 0
    hand.cards.forEach {card -> if (card.rank == 2) numJacks++}
    // println("Hand $hand has $numJacks Jacks")
    val grouping = hand.cards.groupingBy {it.rank}.eachCount()
    val numPairs = grouping.values.filter {it == 2}.count()
    val numTrips = grouping.values.filter {it == 3}.count()
    val numQuads = grouping.values.filter {it == 4}.count()
    val numQuints = grouping.values.filter {it == 5}.count()
    println("Quints: $numQuints, Quads: $numQuads, Trips: $numTrips, Pairs: $numPairs, Jacks: $numJacks")
    var handType = HandType.HighCard
    if (numQuints == 1) {
        handType = HandType.FiveOfAKind
    } else if (numQuads == 1) {
         if (numJacks == 1 || numJacks == 4) {
            handType = HandType.FiveOfAKind
        } else if (numJacks == 0) {
            handType = HandType.FourOfAKind
        }
    } else if (numTrips == 1) {
        if (numJacks == 0) {
            if (numPairs == 1) {
                handType = HandType.FullHouse
            } else {
                handType = HandType.ThreeOfAKind
            }
        } else if (numJacks == 3) {
            if (numPairs == 1) {
                handType = HandType.FiveOfAKind
            } else {
                handType = HandType.FourOfAKind
            }
        } else if (numJacks == 2) {
            handType = HandType.FiveOfAKind
        } else if (numJacks == 1) {
            handType = HandType.FourOfAKind
        }
    } else if (numPairs > 0) {
        handType = when (numPairs) {
            1 -> { //numJacks can only be 0, 1, or 2 here. If 0 -> onePair. If (1,2) -> ThreeOfAKind
                if (numJacks == 0) {
                    HandType.OnePair
                } else {
                    HandType.ThreeOfAKind
                }
            }
            2 -> {
                if (numJacks == 0) {
                    HandType.TwoPair
                } else if (numJacks == 1) {
                    HandType.FullHouse
                } else {
                    HandType.FourOfAKind
                }
            }
            else -> throw IllegalStateException("Number of pairs was not 0, 1, or 2: $numPairs")
        }
    } else if (numJacks == 1) {
        handType = HandType.OnePair
    }
    println("Hand type for hand $hand: $handType")
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