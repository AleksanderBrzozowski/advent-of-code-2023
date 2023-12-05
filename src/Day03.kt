fun main() {
    class Matrix(val rows: List<List<Char>>) {

        fun Pair<Int, Int>.adjacentSymbols(): List<Char> {
            val (x, y) = this
            val xIndexes = listOf(x - 1, x, x + 1).filter { it in rows[0].indices }
            val yIndexes = listOf(y - 1, y, y + 1).filter { it in rows.indices }
            return xIndexes.flatMap { xIndex ->
                yIndexes.mapNotNull { yIndex ->
                    if (xIndex to yIndex != this) rows[yIndex][xIndex] else null
                }
            }
        }

        fun Pair<Int, Int>.adjacentSymbolIndexes(): List<Pair<Int, Int>> {
            val (x, y) = this
            val xIndexes = listOf(x - 1, x, x + 1).filter { it in rows[0].indices }
            val yIndexes = listOf(y - 1, y, y + 1).filter { it in rows.indices }
            return xIndexes.flatMap { xIndex ->
                yIndexes.mapNotNull { yIndex ->
                    if (xIndex to yIndex != this) xIndex to yIndex else null
                }
            }
        }
    }

    fun List<String>.parseToMatrix(): Matrix = Matrix(this.map { it.toCharArray().toList() })

    fun part1(input: List<String>): Int {
        fun Char.isPartNumber(): Boolean = this != '.' && !this.isDigit()

        val matrix = input.parseToMatrix()
        return matrix.run {
            var hasAdjacentSymbol = false
            var sum = 0
            var consecutiveDigits: List<Int> = emptyList()
            rows.forEachIndexed { y, row ->
                row.forEachIndexed { x, character ->
                    when (val digit = character.digitToIntOrNull()) {
                        null -> {
                            if (hasAdjacentSymbol) {
                                val number = consecutiveDigits.joinToString(separator = "").toIntOrNull() ?: 0
                                sum += number
                            }
                            hasAdjacentSymbol = false
                            consecutiveDigits = emptyList()
                        }

                        else -> {
                            val adjacentSymbols = (x to y).adjacentSymbols()
                            hasAdjacentSymbol = hasAdjacentSymbol || adjacentSymbols.any { it.isPartNumber() }
                            consecutiveDigits += digit
                        }
                    }
                }
            }

            sum
        }
    }

    fun part2(input: List<String>): Int {
        fun Char.isGearSymbol(): Boolean = this == '*'

        fun Matrix.adjacentNumbers(adjacentDigits: List<Pair<Int, Int>>): List<Int> {
            fun Sequence<Int>.readDigits(point: Pair<Int, Int>, visitedDigits: Set<Pair<Int, Int>>): List<Pair<Int, Int>> {
                val (_, y) = point
                return takeWhile { x -> rows[y][x].isDigit() && !visitedDigits.contains(x to y) }
                    .map { it to y }
                    .toList()
            }

            val visitedDigits = mutableSetOf<Pair<Int, Int>>()
            return adjacentDigits.mapNotNull { (x, y) ->
                if (visitedDigits.contains(x to y)) {
                    return@mapNotNull null
                }
                val leftDigits = generateSequence(x - 1) { it - 1 }
                    .takeWhile { it >= 0 }
                    .readDigits(x to y, visitedDigits)
                    .reversed()
                val rightDigits = generateSequence(x + 1) { it + 1 }
                    .takeWhile { it < rows[y].size }
                    .readDigits(x to y, visitedDigits)
                visitedDigits += leftDigits + rightDigits
                val number = (leftDigits + (x to y) + rightDigits)
                    .map { (x, y) -> rows[y][x] }
                    .joinToString(separator = "")
                    .toInt()
                number
            }
        }

        val matrix = input.parseToMatrix()
        var sum = 0
        matrix.apply {
            rows.forEachIndexed { y, row ->
                row.forEachIndexed inner@{ x, character ->
                    if (!character.isGearSymbol()) {
                        return@inner
                    }
                    val adjacentDigits = (x to y).adjacentSymbolIndexes()
                        .filter { (x, y) -> rows[y][x].isDigit() }
                    val adjacentNumbers = adjacentNumbers(adjacentDigits)
                    if (adjacentNumbers.size == 2) {
                        sum += adjacentNumbers[0] * adjacentNumbers[1]
                    }
                }
            }
        }
        return sum
    }

    val testInput = readInput("Day03_test")
    val input = readInput("Day03")
    part2(testInput).println()
    part2(input).println()
}