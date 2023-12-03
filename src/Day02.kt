import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val bag = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14,
        )
        return input.sumOf { line ->
            // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            val (gameNumberPart, subsetsOfCubesPart) = line.split(": ")
            val isContainedInBag = subsetsOfCubesPart.split("; ")
                .flatMap { cubeSetPart ->
                    cubeSetPart.split(", ")
                        .map { cubeByColor ->
                            val (numberOfCubes, color) = cubeByColor.split(" ")
                            numberOfCubes.toInt() to color
                        }
                }
                .all { (numberOfCubes, color) ->
                    val cubesInBag =
                        bag[color] ?: throw RuntimeException("Cubes for color: '$color' were not found in bag")
                    numberOfCubes <= cubesInBag
                }
            if (isContainedInBag) {
                val (_, gameNumberString) = gameNumberPart.split(" ")
                gameNumberString.toInt()
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            val (_, subsetsOfCubesPart) = line.split(": ")
            val minBag = subsetsOfCubesPart.split("; ")
                .flatMap { cubeSetPart ->
                    cubeSetPart.split(", ")
                        .map { cubeByColor ->
                            val (numberOfCubes, color) = cubeByColor.split(" ")
                            numberOfCubes.toInt() to color
                        }
                }
                .fold(emptyMap<String, Int>()) { minBag, (numberOfCubes, color) ->
                    minBag + (color to max(minBag[color] ?: 0, numberOfCubes))
                }
            minBag.values.reduce{ acc, v -> acc * v }
        }

    }

    val testInput = readInput("Day02_test")
    val input = readInput("Day02")
    part1(testInput).println()
    part1(input).println()
    part2(testInput).println()
    part2(input).println()
}