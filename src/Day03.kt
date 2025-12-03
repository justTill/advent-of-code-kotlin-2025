

import kotlin.math.max



fun main() {

    fun getHightesVoltagePart1(battery: String) : Int {
        return battery.indices.flatMap { currentVoltageIndex -> 
            (currentVoltageIndex + 1 .. battery.length-1 ).map { nextVoltageIndex ->
                "${battery[currentVoltageIndex]}${battery[nextVoltageIndex]}".toInt()
            }
        }.maxOrNull()?: throw RuntimeException("No Value Found")

    }


    fun getHightesVoltagePart2(battery: String) : Long {
        /** Nehmen wir "818181911112111" und k = 12 als Beispiel:
            Stack leer, wir starten links: '8' → Stack: 8
            Nächste Ziffer '1' → kleiner als Stack-Spitze → einfach hinzufügen → Stack: 8,1
            Nächste '8' → größer als '1' und genügend Ziffern übrig → '1' entfernen → Stack: 8 → '8' hinzufügen → Stack: 8,8
            Weiter so bis zum Ende
            Endresultat: 888911112111 (die größte 12-stellige Zahl in Originalreihenfolge)
         */
        val newBatteryLength = 12
        val newBatteryVoltage = mutableListOf<Char>()

        for ((index, currentChar) in battery.withIndex()) {
            while (newBatteryVoltage.isNotEmpty() && currentChar > newBatteryVoltage.last() && newBatteryVoltage.size + (battery.length - index) > newBatteryLength) {
                newBatteryVoltage.removeAt(newBatteryVoltage.size - 1)
            }
            if (newBatteryVoltage.size < newBatteryLength) {
                newBatteryVoltage.add(currentChar)
            }
        }
        return newBatteryVoltage.joinToString("").toLong()

    }

    fun part1(input: List<String>): Int = input.sumOf { getHightesVoltagePart1(it) }
    fun part2(input: List<String>): Long = input.sumOf { getHightesVoltagePart2(it) }
    val input = readInput("Day03")
    part1(input).println() 
    part2(input).println()
}