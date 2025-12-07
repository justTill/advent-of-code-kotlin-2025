fun main() {
    data class MathColumn(
        val numbers: List<String>,
        val operator: String
    ) 
    fun getSplitIndicies(input: List<String>): List<Int> {
        if (input.isEmpty()) return emptyList()

        return input.map { str ->
            str.mapIndexedNotNull { index, c ->
                if (c == ' ') index else null
            }.toSet()
        }.reduce { alreadyIntersect, current -> alreadyIntersect.intersect(current) }.sorted()
    }

    fun splitAtIndices(str: String, indices: List<Int>): List<String> {
        if (indices.isEmpty()) return listOf(str)

        val parts = mutableListOf<String>()
        var last = 0

        for (i in indices) {
            if (i <= str.length) {
                parts.add(str.substring(last, i))
                last = i + 1
            }
        }

        if (last <= str.length) {
            parts.add(str.substring(last))
        }
        return parts
    }

    fun getSortedMathList(input: List<String>): List<MathColumn> {
        val indicies = getSplitIndicies(input)
        val mathList = mutableListOf<MutableList<String>>()
        input.forEach { line ->
           splitAtIndices(line, indicies).filter{ it.isNotBlank() && it.isNotEmpty()}.forEachIndexed{ index, number ->
              if (mathList.size <= index) {
                    mathList.add(mutableListOf())
                }
                if(number != "" && "+" !in number && "*" !in number) {
                    mathList[index].add(number)
                }
            }
         }
        input.last().trim().split(" ").filter { it.isNotEmpty() }.forEachIndexed {index, operator -> 
            mathList[index].add(operator)
        }
        return mathList.map { column ->      
            MathColumn(column.dropLast(1), column.last().trim() )
        }
    }


    fun calculateWithOperatorAtTheEnd(mathList: List<MathColumn>) : List<Long> {
        return mathList.map { mathColumn ->
            when (mathColumn.operator) {
                "+" -> mathColumn.numbers.sumOf { it.trim().toLong() }
                "*" -> mathColumn.numbers.map { it.trim().toLong() }.fold(1L) { currentTotal, current -> currentTotal * current }
            else -> throw IllegalArgumentException("Unbekannter Operator: $mathColumn.operator")
            }
        }   
    }

    fun mapToCephalopodsCountingSystem(mathList: List<MathColumn>) : List<MathColumn> {
        return mathList.map{ mathColumn -> 
            val maxLength = mathColumn.numbers.maxOf { it.length }
            val mappedList = mutableListOf<String>()
            for (i in 0 until maxLength) {
                mappedList.add(mathColumn.numbers.map { it.getOrNull(i) }.filter { it != null }.joinToString(""))
            }
            MathColumn(mappedList, mathColumn.operator)
        }
    }


    fun solveMathProblemsPart1 (input: List<String>): Long = calculateWithOperatorAtTheEnd(getSortedMathList(input)).sum()
    fun solveMathProblemsPart2 (input: List<String>): Long = calculateWithOperatorAtTheEnd(mapToCephalopodsCountingSystem(getSortedMathList(input))).sum()

    val input = readInput("Day06")
    solveMathProblemsPart1(input).println() 
    solveMathProblemsPart2(input).println()
}