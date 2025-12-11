fun main() {
    data class Node(
        val name: String,
        val next: List<String>
    )

    class Graph(nodes: List<Node>) {
        private val adjacency: Map<String, Node> = nodes.associateBy { it.name }

        fun neighbors(name: String): List<String> =
            adjacency[name]?.next ?: emptyList()
        
        override fun toString(): String {
            return adjacency.values
                .sortedBy { it.name }
                .joinToString("\n") { node ->
                    val targets = if (node.next.isEmpty()) ";" else node.next.joinToString(", ")
                    "${node.name} -> $targets"
        }
    }
    }

    fun countAllPaths(graph: Graph, start: String, target: String): Long {
        var result = 0L
        val path = mutableListOf<String>()
        fun dfs(node: String) {
            if (node in path) return 
            path.add(node)
            if (node == target) {
                result += 1
            } else {
                for (next in graph.neighbors(node)) {
                    dfs(next)
                }
            }
            path.removeAt(path.size - 1)
        }
        dfs(start)
        return result
        /** dfs(you) ->
          path = [you]
          neighbors = [bbb, ccc]    
          dfs(bbb)
            path = [you, bbb]
            neighbors = [ddd, eee]
            dfs(ddd)
                path = [you, bbb, ddd]
                neighbors = [ggg]
                dfs(ggg)
                    path = [you, bbb, ddd, ggg]
                    neighbors = [out]
                    dfs(out)
                        path = [you, bbb, ddd, ggg, out]
                        //add To Result
                        remove(out)
                    remove(ggg)
                remove(ddd)
            dfs(eee)
                path = [you, bbb, eee]
                neighbors = [out]
                dfs(out)
                    path = [you, bbb, eee, out]
                    //add to result
                    remove(out)
                remove(eee)
            remove(ddd)
            dfs(ccc)
            ....
    */
    }

    fun countPathsMemo(graph: Graph, node: String, target: String, required: Set<String> = setOf(), memo: MutableMap<Pair<String, Set<String>>, Long> = mutableMapOf()): Long {
        if (node == target) return if (required.isEmpty()) 1 else 0

        val key = node to required
        if (memo.containsKey(key)) return memo[key]!!

        var total = 0L
        val newRequired = if (node in required) required - node else required

        for (next in graph.neighbors(node)) {
            total += countPathsMemo(graph, next, target, newRequired, memo)
        }

        memo[key] = total
        return total
    }
    

    fun mapInputToGraph(input: List<String>): Graph {
        return Graph(input.map { line -> 
            val splitted = line.split(":")
            val device = splitted[0].trim()
            val outputs = splitted[1].split(" ").filter { it.isNotBlank() && it.isNotEmpty() }.map { it.trim() }
            Node(device, outputs)
        })
    }
    fun part1(input: List<String>): Long {
        val graph = mapInputToGraph(input)
        return countPathsMemo(graph, "you", "out")
    }

    fun part2(input: List<String>): Long {
        val graph = mapInputToGraph(input)
        val needed = setOf("fft", "dac")
        return countPathsMemo(graph, "svr", "out", needed)
    }
    val input = readInput("Day11")
    part1(input).println() 
    part2(input).println()
}