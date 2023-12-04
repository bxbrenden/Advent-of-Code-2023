package AdventDay03Part1

import java.io.File


fun readFile(filename: String): MutableList<String> {
    val lineList = mutableListOf<String>()
    File(filename).useLines { lines -> lines.forEach {lineList.add(it)} }
    return lineList
}

fun make2DArray(puzzle: MutableList<String>): List<List<Char>> {
    var grid = mutableListOf<MutableList<Char>>()
    for (line in puzzle) {
        var lineArray = mutableListOf<Char>()
        for (c in line) {
            lineArray.add(c)
        }
        grid.add(lineArray)
    }
    return grid
}

fun findDigitsInRow(row: List<Char>): List<Int> {
    var indices = mutableListOf<Int>()
    for ((i, c) in row.withIndex()) {
        if (c.isDigit()) {
            indices.add(i)
        }
    }
    println("Row \"$row\" contains digits at indices: $indices")
    return indices
}

fun findSymbolsInRow(row: List<Char>): List<Int> {
    var indices = mutableListOf<Int>()
    for ((i, c) in row.withIndex()) {
        val numRange = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.') 
        if (numRange.contains(c)) {
            continue
        } else {
            indices.add(i)
        }
    }
    println("Row \"$row\" contains symbols at indices: $indices")
    return indices
}

fun findAdjacentSymbols(rowIndex: Int, colIndex: Int, grid: List<List<Char>>): Int {
    println("Checking for adjacent symbols for ${grid[rowIndex][colIndex]}")
    val numRange = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.')
    var adjacentCount: Int = 0
    // Check diagonally above and left, directly above, and diagonally above and right
    val above = grid.getOrNull(rowIndex - 1)
    if (above != null) {
        val aboveLeft = grid[rowIndex - 1].getOrNull(colIndex - 1)
        val aboveCenter = grid[rowIndex - 1].getOrNull(colIndex)
        val aboveRight = grid[rowIndex - 1].getOrNull(colIndex + 1)
        if (aboveLeft != null) {
            if (aboveLeft !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol $aboveLeft above-left of [$rowIndex][$colIndex]")
            }
        }
        if (aboveCenter != null) {
            if (aboveCenter !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol $aboveCenter above-center of [$rowIndex][$colIndex]")
            }
        }
        if (aboveRight != null) {
            if (aboveRight !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol $aboveRight above-right of [$rowIndex][$colIndex]")
            }
        }
    }

    // Check directly left
    val left = grid[rowIndex].getOrNull(colIndex - 1)
    if (left != null) {
        if (left !in numRange) {
            adjacentCount += 1
            println("Found adjacent symbol $left left of [$rowIndex][$colIndex]")
        }
    }

    // Check right
    val right = grid[rowIndex].getOrNull(colIndex + 1)
    if (right != null) {
        if (right !in numRange) {
            adjacentCount += 1
            println("Found adjacent symbol $right right of [$rowIndex][$colIndex]")
        }
    }

    // Check diagonally below and left, directly below, and diagonally below and right
    val below = grid.getOrNull(rowIndex + 1)
    if (below != null) {
        val belowLeft = grid[rowIndex + 1].getOrNull(colIndex - 1)
        val belowCenter = grid[rowIndex + 1].getOrNull(colIndex)
        val belowRight = grid[rowIndex + 1].getOrNull(colIndex + 1)
        if (belowLeft != null) {
            if (belowLeft !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol $belowLeft below-left of [$rowIndex][$colIndex]")
            }
        }
        if (belowCenter != null) {
            if (belowCenter !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol $belowCenter below-center of [$rowIndex][$colIndex]")
            }
        }
        if (belowRight != null) {
            if (belowRight !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol $belowRight below-right of [$rowIndex][$colIndex]")
            }
        }
    }

    return adjacentCount
}

fun findContiguousInts(line: List<Char>, rowNumber: Int, output: HashMap<Int, List<Pair<Int, Int>>>, lineSize: Int, recurseCount: Int = 0): HashMap<Int, List<Pair<Int, Int>>> {
    println("Finding contiguous ints on line: \"$line\"")
    println("The recurseCount value is $recurseCount")
    // var output = hashMapOf<Int, List<Pair<Int, Int>>>()
    var recurseOffset: Int = 0
    if (recurseCount != 0) {
        recurseOffset = lineSize - line.size
    }
    var numStartsAt: Int = -1
    var numEndsAt: Int = -1
    for ((i, c) in line.withIndex()) {
        if (c.isDigit() && numStartsAt == -1) {
            numStartsAt = i
            println("Found starting digit $c at index $numStartsAt")
            break
        }
    }
    if (numStartsAt == -1) {
        // there are no more remaining ints in this row
        return output
    }
    if (numStartsAt == line.size - 1) {
        // We have a one-digit number
        numEndsAt = numStartsAt
        println("Found a one-digit number that both starts and ends at index $numEndsAt")
        if (numStartsAt == -1 || numEndsAt == -1) {
            throw IllegalStateException("Finding contiguous numbers failed for row \"$line\"")
        }
        val fullNumber: Int = listOf(line.slice(numStartsAt..numEndsAt))
            .joinToString(separator = "")
            .replace(",", "")
            .replace(" ", "")
            .replace("[", "")
            .replace("]", "")
            .replace(".", "")
            .replace("*", "")
            .replace("$", "")
            .replace("%", "")
            .toInt()
        output[fullNumber] = mutableListOf(Pair(rowNumber, numEndsAt + recurseOffset))
        println("Output is now equal to:\n$output")
        if (line.size <= 1) {
            println("Returning output")
            return output
        } else {
            println("Recursing using sub-line: ${line.slice(numEndsAt + 1..line.size - 1)}")
            return findContiguousInts(line.slice(numEndsAt + 1..line.size - 1), rowNumber, output, lineSize, recurseCount + 1)
        }
    }

    for ((i, c) in line.slice(numStartsAt..line.size - 1).withIndex()) {
        if (c.isDigit()) {
            println("Found digit $c at index ${numStartsAt + i}. Continuing...")
            continue
        } else {
            numEndsAt = numStartsAt + i - 1
            println("Found ending digit $c at index $numEndsAt")
            break
        }
    }
    if (numStartsAt == -1 || numEndsAt == -1) {
        throw IllegalStateException("Finding contiguous numbers failed for row \"$line\"")
    }
    val fullNumber: Int = listOf(line.slice(numStartsAt..numEndsAt))
        .joinToString(separator = "")
        .replace(",", "")
        .replace(" ", "")
        .replace("[", "")
        .replace("]", "")
        .replace(".", "")
        .replace("*", "")
        .replace("$", "")
        .replace("%", "")
        .toInt()
    var coordsList = mutableListOf<Pair<Int, Int>>()
    for (x in numStartsAt..numEndsAt) {
        coordsList.add(Pair(rowNumber, x + recurseOffset))
    }
    output[fullNumber] = coordsList
    println("Output is now equal to:\n$output")
    if (line.size <= 1) {
        println("Returning output")
        return output
    } else {
        println("Recursing using sub-line: ${line.slice(numEndsAt + 1..line.size - 1)}")
        return findContiguousInts(line.slice(numEndsAt + 1..line.size - 1), rowNumber, output, lineSize, recurseCount + 1)
    }
}

fun main(args: Array<String>) {
    val inputFile = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readFile(inputFile)
    val _2DArray: List<List<Char>> = make2DArray(puzzle)
    val lineSize = _2DArray.size
    _2DArray.forEach {println(it)}
    // _2DArray.map {findDigitsInRow(it)}
    // _2DArray.map {findSymbolsInRow(it)}

    // var totalAdjacent: Int = 0
    // for ((i, _) in _2DArray.withIndex()) {
    //     for ((j, _) in _2DArray[0].withIndex()) {
    //         if (_2DArray[i][j].isDigit()) {
    //             totalAdjacent += findAdjacentSymbols(i, j, _2DArray)
    //         }
    //     }
    // }
    // println("Total Adjacent symbols before deduplication: $totalAdjacent")

    var relevant = hashMapOf<Int, List<Pair<Int, Int>>>()
    for ((index, line) in _2DArray.withIndex()) {
        relevant = findContiguousInts(line, index, relevant, lineSize)
    }
    println(relevant)
    var totalAdjacent = mutableSetOf<Int>()
    for ((key, value) in relevant) {
        for (v in value) {
            if (findAdjacentSymbols(v.first, v.second, _2DArray) > 0) {
                totalAdjacent.add(key)
            }
        }
    }
    print("Generating answer using set:\n$totalAdjacent")
    print("ANSWER: ${totalAdjacent.sum()}")
}