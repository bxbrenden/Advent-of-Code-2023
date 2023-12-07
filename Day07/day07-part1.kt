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

class Hand(val cardsList: String, val bidStr: String) : Comparator<Hand> {
    val cards: List<Card>
    val bid: Int
    val handType: HandType
    val strength: Int
        get() {
            return this.handType.strength * 100_000 +
            this.cards[0].rank * 10_000 +
            this.cards[1].rank * 1_000 +
            this.cards[2].rank * 100 +
            this.cards[3].rank * 10 +
            this.cards[4].rank
        }

    override fun compare(ownHand: Hand, other: Hand): Int {
        if (ownHand.strength == other.strength) {
            for (i in 1..5) {
                if (ownHand.cards[i].rank > other.cards[i].rank) {
                    return 1
                }
            }
            return 0
        } else if (ownHand.strength > other.strength) {
            return 1
        } else {
            return -1
        }
    }

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
    println("Categorizing hand: $hand")
    println("$hand.n")
    val grouping = hand.cards.groupingBy {it.rank}.eachCount()
        .filter {it.value > 1}
    println("Grouping: $grouping")
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
    println("Hand type for hand $hand: $handType")
    return handType
}

fun main(args: Array<String>) {
    val filename = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readInput(filename)
    val allHands = getHandsFromPuzzle(puzzle)
    println(allHands)
    allHands.forEach {hand -> categorizeHand(hand)}
    val hand1 = Hand("23333", "123")
    val hand2 = Hand("32222", "123")
    println("Strength of $hand1: ${hand1.strength}")
    println("Strength of $hand2: ${hand2.strength}")
    println(allHands.sortedWith(compareBy {it.strength} ).reversed())
}