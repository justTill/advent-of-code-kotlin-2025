fun main() {
    fun part1(input: List<String>): Int {
        var zeroCount = 0
        input.fold(50) { current, line ->
            val letter = line.takeWhile { it.isLetter() }
            val number = line.dropWhile { it.isLetter() }.toInt()
            val newPosition = when (letter) {
                "R" -> (current + number) % 100
                else -> (current - number % 100 + 100) % 100
            }
            if (newPosition == 0) zeroCount++
            newPosition
        }
        return zeroCount
    }


    fun part2(input: List<String>): Int {
        var zeroCount = 0
        var fullRotations = 0
        input.fold(50) { current, line ->
            val letter = line.takeWhile { it.isLetter() }
            var number = line.dropWhile { it.isLetter() }.toInt()
            fullRotations += number / 100
            number %= 100
            var newPosition = current
            var addToZero = false

            if(letter == "R"){
                newPosition = current + number
                addToZero = newPosition > 100
            }
            if(letter == "L"){
                val tmp = current - number % 100
                addToZero = tmp < 0 && current != 0
                newPosition = tmp + 100
            }
            newPosition %= 100
            if (newPosition == 0 || addToZero) {
                zeroCount++
            }
            newPosition
        }
        return zeroCount + fullRotations
    }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
