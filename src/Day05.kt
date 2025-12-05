fun main() { 
    data class IdRange(val start: Long, val end: Long) {
        val size: Long get() = end - start + 1
    }

    fun parseRanges(input: List<String>): List<IdRange> =
        input.map { line ->
            val (start, end) = line.split('-').map { it.toLong() }
            IdRange(start, end)
    }

    fun isFresh(id: Long, ranges: List<IdRange>): Boolean =
        ranges.any { id in it.start..it.end }

    fun part1(input: List<String>): Int {
        val splitIndex = input.indexOf("")
        val ranges = parseRanges(input.subList(0, splitIndex))
        return input
            .subList(splitIndex + 1, input.size)
            .map { it.toLong() }
            .count { id -> isFresh(id, ranges) }
    }

    fun mergeRanges(ranges: List<IdRange>): List<IdRange> {
        if (ranges.isEmpty()) return emptyList()

        val sorted = ranges.sortedBy { it.start }
        val merged = mutableListOf<IdRange>()
        var current = sorted.first()

        for (r in sorted.drop(1)) {
            if (r.start <= current.end + 1) {
                current = IdRange(current.start, maxOf(current.end, r.end))
            } else {
                merged += current
                current = r
            }
        }

        merged += current
        return merged
    }

    fun part2(input: List<String>): Long {
        val splitIndex = input.indexOf("")
        val ranges = parseRanges(input.subList(0, splitIndex))
        return mergeRanges(ranges).sumOf { it.size }
    }
    val input = readInput("Day05")
    part1(input).println() 
    part2(input).println()
}