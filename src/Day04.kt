

import kotlin.math.max



fun main() {
    fun getNumberOfAts(row: String?, index: Int) : Int {
        if (row == null) return 0
        return (if (row.getOrElse(index -1) { '.' } == '@') 1 else 0) +
               (if (row.getOrElse(index) { '.' } == '@') 1 else 0) +
               (if (row.getOrElse(index+1) { '.' } == '@') 1 else 0) 
    }

    fun markValidRowsWithX(before: String?, current: String, after:String?) : String {
        return current.mapIndexed { index, char -> 
            var isValid = false
            if (char == '@') {
                var atCount = getNumberOfAts(before, index) +
                              getNumberOfAts(current, index) +
                              getNumberOfAts(after, index)         
                if(atCount < 5 ){
                    isValid = true
                }
            } 
            if (isValid) 'x' else char
        }.joinToString("")
    }

    fun part1(input: List<String>): Int {
        return input.mapIndexed { index, current ->
            markValidRowsWithX(input.getOrNull(index-1), current, input.getOrNull(index + 1))
         }.sumOf { row -> row.count { it == 'x'} }
    }
    fun part2(input: List<String>): Int{
        var numberOfRollsAllTime = 0
        var numberOfRollsRemoved = 0 
        var currentInput = input
        do {
            var processedRows = currentInput.mapIndexed { index, current ->
                    markValidRowsWithX(currentInput.getOrNull(index-1), current, currentInput.getOrNull(index + 1))
                }
            numberOfRollsRemoved = processedRows.sumOf { row -> row.count { it == 'x' } }
            currentInput = processedRows.map { row -> row.replace('x', '.')}
            numberOfRollsAllTime += numberOfRollsRemoved
        }    while (numberOfRollsRemoved != 0) 
        return numberOfRollsAllTime
    }
    val input = readInput("Day04")
    part1(input).println() 
    part2(input).println()
}