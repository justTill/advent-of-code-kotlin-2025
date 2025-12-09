
import kotlin.math.sqrt


fun main() {
    data class Vector3(
        val x: Double,
        val y: Double,
        val z: Double
    ) {
        // Abstand zwischen zwei Vektoren (Punkt-zu-Punkt-Distanz)
        fun distanceTo(other: Vector3): Double {
            val dx = x - other.x
            val dy = y - other.y
            val dz = z - other.z
            return sqrt(dx * dx + dy * dy + dz * dz)
        }
        // Länge des Vektors
        fun length(): Double = sqrt(x * x + y * y + z * z)
    }

    fun indexOfCircuitContaining(foundCircuits: List<List<Vector3>>, v: Vector3): Int {
        for (i in foundCircuits.indices) {
            if (v in foundCircuits[i]) return i
        }
        return -1
    }

    fun mapToVectors (input: List<String>) : List<Vector3> {
        return input.map { line ->
            val (x, y, z) = line.split(",").map { it.trim().toDouble() }
            Vector3(x, y, z)
        }
    }
    
   fun getClosestVectors(input: List<Vector3>, foundCircuits: List<List<Vector3>> ): Pair<Vector3, Vector3>? {
        val usedVectors = foundCircuits.flatten().toSet()

        return input.flatMapIndexed { i, v1 ->
            input.drop(i + 1).map { v2 ->
                v1 to v2
            }
        }
        .filter { (v1, v2) ->
            val v1Used = v1 in usedVectors
            val v2Used = v2 in usedVectors
            !(v1Used && v2Used)
        }
        .minByOrNull { (v1, v2) -> v1.distanceTo(v2) }
    }
    fun getShortesCircuitsFromVectors(input: List<Vector3>) : List<List<Vector3>> {
        var alreadyChecked = mutableListOf<Vector3>()
        var foundCircuits = mutableListOf(mutableListOf<Vector3>())
        for (i in 0 until input.size) {
            val (v1, v2) = getClosestVectors(input, foundCircuits) ?: continue
            val indexV1 = indexOfCircuitContaining(foundCircuits, v1)
            val indexV2 = indexOfCircuitContaining(foundCircuits, v2)
            println("V1: $v1 and v2: $v2")
            when {
                indexV1 == -1 && indexV2 == -1 -> {
                    println("Both in no circuit -> create new one")
                    foundCircuits.add(mutableListOf(v1, v2))
                }

                // Nur v1 ist in einem Circuit -> füge v2 vorne oder hinten hinzu
                indexV1 != -1 && indexV2 == -1 -> {
                    println("v1 in a circuit but not v2")
                    val circuit = foundCircuits[indexV1]
                    when {
                        v1 == circuit.first() -> circuit.add(0, v2)
                        v1 == circuit.last() -> circuit.add(v2)
                        else -> foundCircuits.add(mutableListOf(v2)) // Falls irgendwo in Circuit
                    }
                }

                // Nur v2 ist in einem Circuit -> füge v1 vorne oder hinten hinzu
                indexV1 == -1 && indexV2 != -1 -> {
                    println("v2 in a circuit but not v1")
                    val circuit = foundCircuits[indexV2]
                    when {
                        v2 == circuit.first() -> circuit.add(0, v1)
                        v2 == circuit.last() -> circuit.add(v1)
                        else -> foundCircuits.add(mutableListOf(v1))
                    }
                }

                indexV1 != -1 && indexV2 != -1 -> {
                    
                    println("Both in a circuit should not happen")
                    continue
                }
            }
        }
        println(foundCircuits)
        return foundCircuits.sortedBy { it.size }.asReversed()
    }

    fun part1(input: List<String>): Int {
        val vectors = mapToVectors(input)
        val circuits = getShortesCircuitsFromVectors(vectors)    
        circuits.take(3).forEach {  println(it)}
        return circuits.map { it.size }.sortedDescending().take(3).reduce(Int::times)
    }
    fun part2(input: List<String>): Long {
       return 2
    }
    val input = readInput("Day08")
    part1(input).println() 
    part2(input).println()
}