fun main() {
    fun isValidIdPart2(id: String): Boolean {
        for (substringLength in 1..id.length/2){
            //makes sense only to check cases if multiple of current substring is able to be fully x times in full id
            if(id.length % substringLength == 0) {
                val pattern = id.take(substringLength)
                //check if pattern n times repeated the same as id
                if(pattern.repeat(id.length / substringLength) == id) {
                    return false
                }

            }
        }
        return true
    }
    fun isValidId(id: Long, extraCheck: Boolean): Boolean {
        val stringId = id.toString()
        if(id.toString().length % 2 != 0 && !extraCheck) return true
        if(stringId.take(stringId.length /2) == stringId.substring( stringId.length /2, stringId.length)) return false
        if(extraCheck){
             return isValidIdPart2(stringId)
        }
        return true
    }

    fun part1(input: List<String>, extraCheck: Boolean): Long {
        return input.first()
            .split(',')
            .flatMap { range ->
                val (start, end) = range.split('-').map { it.toLong() }
                (start..end).filter { !isValidId(it, extraCheck) }
            }
            .sum()
    }
    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input, false).println() //64215794229
    part1(input, true).println() //4174379265

}
