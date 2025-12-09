fun main() {
    data class Point(val x: Long, val y: Long) {
        fun rectangleArea(other: Point): Long {
            val width = kotlin.math.abs(other.x - this.x)
            val height = kotlin.math.abs(other.y - this.y)
            return (width + 1) * (height +1)
        }
    }
    fun mapToPoints(input: List<String>) : List<Point> {
        return input.map {line ->
            val values = line.split(",")
            Point(values[0].toLong(), values[1].toLong())
        }
    }

    fun getBiggestRectangleBetweenPoints(points: List<Point>) : Long {
        var maxArea = 0L
        for (point in points) {
            val area = points.filter { p -> p != point && p.y != point.y && p.x != point.x  }
                .map { p -> p.rectangleArea(point) }
                .maxOf {it}
            if (area > maxArea) maxArea = area
        }
        return maxArea
    }

    fun part1(input: List<String>): Long {
        val points = mapToPoints(input)
        val biggestRectangleArea = getBiggestRectangleBetweenPoints(points)
        return biggestRectangleArea //4754955192
    }

    fun part2(input: List<String>): Long {        
        return 2
    }
    val input = readInput("Day09")
    part1(input).println() 
    part2(input).println()
}