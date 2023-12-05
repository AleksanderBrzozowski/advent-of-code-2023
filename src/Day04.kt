fun main() {
    // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val (_, numbers) = line.split(": ") // '41 48 83 86 17 | 83 86  6 31 17  9 48 53'
            // '41 48 83 86 17', '83 86  6 31 17  9 48 53'
            val (winningNumbersRaw, selectedNumbersRaw) = numbers.split(" | ")
            val winningNumbers = winningNumbersRaw.split(" ").mapNotNull { it.toIntOrNull() }
                .toSet()
            val selectedNumbers = selectedNumbersRaw.split(" ").mapNotNull { it.toIntOrNull() }

            selectedNumbers.fold<Int, Int>(0) { score, number ->
                if (number in winningNumbers) {
                    if (score == 0) 1 else score * 2
                } else {
                    score
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val wonCopiesOfCards = mutableMapOf<Int, Int>()
        input.forEachIndexed { index, line ->
            val (_, numbers) = line.split(": ") // '41 48 83 86 17 | 83 86  6 31 17  9 48 53'
            // '41 48 83 86 17', '83 86  6 31 17  9 48 53'
            val (winningNumbersRaw, selectedNumbersRaw) = numbers.split(" | ")
            val winningNumbers = winningNumbersRaw.split(" ").mapNotNull { it.toIntOrNull() }
                .toSet()
            val selectedNumbers = selectedNumbersRaw.split(" ").mapNotNull { it.toIntOrNull() }
            val numberOfWinningNumbers = selectedNumbers.filter { it in winningNumbers }.size
            val wonCardIndexes = generateSequence(index + 1) { it + 1 }
                .take(numberOfWinningNumbers)
                .filter { it < input.size }
            val numberOfCards = (wonCopiesOfCards[index] ?: 0) + 1 // copies + one original
            wonCardIndexes.forEach { wonCardIndex ->
                wonCopiesOfCards.compute(wonCardIndex) { _, numberOfCopies ->
                    if (numberOfCopies == null) numberOfCards else numberOfCopies + numberOfCards
                }
            }
        }
        return wonCopiesOfCards.values.sum() + input.size
    }

    val testInput = readInput("Day04_test")
    val input = readInput("Day04")
    part1(testInput).println()
    part1(input).println()
    part2(testInput).println()
    part2(input).println()
}