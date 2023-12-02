fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line: String ->
            val digits = line.mapNotNull { character: Char -> character.digitToIntOrNull() }
            val (first, last) = digits.first() to digits.last()
            "$first$last".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val digitByDigitString = listOf(
            "one" to 1,
            "two" to 2,
            "three" to 3,
            "four" to 4,
            "five" to 5,
            "six" to 6,
            "seven" to 7,
            "eight" to 8,
            "nine" to 9,
        )
        return input.sumOf { line: String ->
            val digitsByString: List<Pair<Int, Int>> = digitByDigitString.flatMap { (digitString, digit) ->
                listOfNotNull(
                    line.indexOf(digitString).takeIf { it != -1 },
                    line.lastIndexOf(digitString).takeIf { it != -1 }
                )
                    .map { it to digit }
            }
            val digits: List<Pair<Int, Int>> = line.mapIndexedNotNull { index, character: Char ->
                val digit = character.digitToIntOrNull()
                if (digit != null) index to digit else null
            }
            val digitsSorted = (digitsByString + digits)
                .sortedBy { (index, _) -> index }
                .map { (_, digit) -> digit }

            val (first, last) = digitsSorted.first() to digitsSorted.last()
            "$first$last".toInt()
        }
    }

    val testInput = readInput("Day01")
    part1(testInput).println()
    val input = readInput("Day01")
    part2(input).println()
}
