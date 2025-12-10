fun main() {
    data class Manual(val diagramm: String, val  indices: List<List<Int>>, var joltage: MutableList<Int>, val groupedIndices: Map<Int, List<List<Int>>>) 

    fun parseDiagrammString (diagram: String): String {
        return diagram.map { if (it == '#') '1' else '0' }.joinToString("")
    }

    fun parseSchematicsString (indices: List<Int>, n: Int): String {
        return CharArray(n) { i ->
            if (i in indices) '1' else '0'
        }.concatToString()
    }

    fun parseDiagramm (diagram: String): Int {
        var mask = 0
        diagram.forEach { c ->
            mask = mask shl 1
            if (c == '#') mask = mask or 1
        }
        return mask
    }

    fun parseSchematics (indices: List<Int>, lightCount: Int): Int {
        var mask = 0
        for (i in indices) {
            val bit = 1 shl (lightCount - 1 - i)
            mask = mask or bit
        }
        return mask
    }

    fun mapToManual(input: List<String>): List<Manual> {
        return input.map { line ->
            val diagramm = Regex("""\[(.*?)\]""").find(line)!!.groupValues[1]

            val parenMatches = Regex("""\((.*?)\)""").findAll(line).toList()

            val joltage: MutableList<Int> = Regex("""\{(.*?)\}""")
                .find(line)!!
                .groupValues[1]
                .split(',')
                .map { it.trim().toInt() }.toMutableList()

            val indices = parenMatches.map { match ->
                val inner = match.groupValues[1]
                if (inner.isBlank()) emptyList()
                else inner.split(',').map { it.trim().toInt() }
            }
            val allNumbers = indices.flatten().toSet()
            val grouped = allNumbers.associateWith { number ->
                indices.filter { number in it }
            }.toSortedMap()
            Manual(
                diagramm = diagramm,
                indices = indices,
                joltage = joltage,
                groupedIndices = grouped
            )
        }
    }
    fun <T> powerSet(list: List<T>): List<List<T>> {
        return list.fold(listOf(listOf())) { acc, element ->
            acc + acc.map { it + element }
        }
    }
    fun xorStrings(a: String, b: String): String {
        return a.zip(b).joinToString("") { (c1, c2) ->
            if (c1 == c2) "0" else "1"
        }
    }

    fun getMinSolveasBinary(manual: Manual): Int {
        val diagrammAsBinary = parseDiagramm(manual.diagramm)
        val diagrammSize = manual.diagramm.length
        val schematics = manual.indices.map { parseSchematics(it, diagrammSize)}
        val pset = powerSet(schematics)
        val minimalSubset = pset.filter { it.isNotEmpty() }.sortedBy { it.size }.firstOrNull { subset -> subset.fold(0) { acc, item -> acc.xor(item) } == diagrammAsBinary }
        if (minimalSubset == null) {
            throw RuntimeException("NoSubset found?")
        }
        return minimalSubset.size
    }

    fun getMinSolveasString(manual: Manual): Int {
        val diagrammAsBinary = parseDiagrammString(manual.diagramm)
        val diagrammSize = diagrammAsBinary.length
        val schematics = manual.indices.map { parseSchematicsString(it, diagrammSize)}
        val pset = powerSet(schematics)
        val emptyDiagramm = CharArray(diagrammSize) { '0' }.concatToString()
        val minimalSubset = pset.filter { it.isNotEmpty() }.sortedBy { it.size }.firstOrNull { subset -> subset.fold(emptyDiagramm) { acc, item -> xorStrings(acc, item) } == diagrammAsBinary }
        if (minimalSubset == null) {
            throw RuntimeException("NoSubset found?")
        }
        return minimalSubset.size
    }

    fun minSolvesForManuals(manuals: List<Manual>): List<Int> {
        //return manuals.map { getMinSolveasString(it) }
        return manuals.map { getMinSolveasBinary(it) }
    }

    fun getMinButtenPress(manual: Manual): Int {
        return 2
    }

    fun minButtonsPressedForManuals(manuals: List<Manual>): List<Int> {
        return manuals.map { getMinButtenPress(it) }
    }
        
    fun part1(input: List<String>): Int {
        val manuals = mapToManual(input)
        val minSolvesForManuals = minSolvesForManuals(manuals)
        return minSolvesForManuals.sum()
    }
    fun part2(input: List<String>): Int {
        val manuals = mapToManual(input)
        val minPressedButtons = minButtonsPressedForManuals(manuals)
        return 2
    }
    val input = readInput("Day10")
    part1(input).println() 
    part2(input).println()
}