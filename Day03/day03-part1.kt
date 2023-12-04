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
                println("Found adjacent symbol above-left of [$rowIndex][$colIndex]")
            }
        }
        if (aboveCenter != null) {
            if (aboveCenter !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol above-center of [$rowIndex][$colIndex]")
            }
        }
        if (aboveRight != null) {
            if (aboveRight !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol above-right of [$rowIndex][$colIndex]")
            }
        }
    }

    // Check directly left
    val left = grid[rowIndex].getOrNull(colIndex - 1)
    if (left != null) {
        if (left !in numRange) {
            adjacentCount += 1
            println("Found adjacent symbol left of [$rowIndex][$colIndex]")
        }
    }

    // Check right
    val right = grid[rowIndex].getOrNull(colIndex + 1)
    if (right != null) {
        if (right !in numRange) {
            adjacentCount += 1
            println("Found adjacent symbol right of [$rowIndex][$colIndex]")
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
                println("Found adjacent symbol below-left of [$rowIndex][$colIndex]")
            }
        }
        if (belowCenter != null) {
            if (belowCenter !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol below-center of [$rowIndex][$colIndex]")
            }
        }
        if (belowRight != null) {
            if (belowRight !in numRange) {
                adjacentCount += 1
                println("Found adjacent symbol below-right of [$rowIndex][$colIndex]")
            }
        }
    }

    return adjacentCount
}

fun main(args: Array<String>) {
    val inputFile = if (args.size > 0) args[0] else "sample_input.txt"
    val puzzle = readFile(inputFile)
    val _2DArray: List<List<Char>> = make2DArray(puzzle)
    _2DArray.forEach {println(it)}
    _2DArray.map {findDigitsInRow(it)}
    _2DArray.map {findSymbolsInRow(it)}

    var totalAdjacent: Int = 0
    for ((i, _) in _2DArray.withIndex()) {
        for ((j, _) in _2DArray[0].withIndex()) {
            if (_2DArray[i][j].isDigit()) {
                totalAdjacent += findAdjacentSymbols(i, j, _2DArray)
            }
        }
    }
    println("Total Adjacent symbols before deduplication: $totalAdjacent")
}