package com.example.server

import com.example.server.models.Point
import com.example.server.models.RouteRequest
import com.example.server.models.RouteResponse
import com.example.server.models.Shop

class NavigationService {
    private val shops = listOf(
        Shop("1", "Apple Store", 1, 10.0, 20.0, "electronics"),
        Shop("2", "Zara", 1, 30.0, 25.0, "clothing"),
        Shop("3", "Starbucks", 1, 15.0, 40.0, "cafe"),
        Shop("4", "Nike", 2, 25.0, 15.0, "sport"),
        Shop("5", "Sephora", 2, 35.0, 35.0, "beauty"),
        Shop("6", "Cinema", 3, 20.0, 20.0, "entertainment")
    )

    private val graph = buildGraph()

    private fun buildGraph(): Map<String, List<Pair<String, Double>>> {
        return mapOf(
            "10.0,20.0,1" to listOf("15.0,40.0,1" to 25.0, "30.0,25.0,1" to 20.5),
            "15.0,40.0,1" to listOf("10.0,20.0,1" to 25.0, "30.0,25.0,1" to 22.0),
            "30.0,25.0,1" to listOf("10.0,20.0,1" to 20.5, "15.0,40.0,1" to 22.0),
            "25.0,15.0,2" to listOf("35.0,35.0,2" to 28.0),
            "35.0,35.0,2" to listOf("25.0,15.0,2" to 28.0),
            "20.0,20.0,3" to listOf()
        )
    }

    fun getAllShops(): List<Shop> = shops
    fun getShopsByFloor(floor: Int): List<Shop> = shops.filter { it.floor == floor }
    fun searchShops(query: String): List<Shop> = shops.filter { it.name.contains(query, ignoreCase = true) }

    fun findPath(request: RouteRequest): RouteResponse {
        val startKey = "${request.startX},${request.startY},${request.floor}"
        val endKey = "${request.endX},${request.endY},${request.floor}"

        // Dijkstra
        val distances = mutableMapOf<String, Double>()
        val previous = mutableMapOf<String, String>()
        val unvisited = mutableSetOf<String>()

        graph.keys.forEach { node ->
            distances[node] = if (node == startKey) 0.0 else Double.POSITIVE_INFINITY
            unvisited.add(node)
        }

        while (unvisited.isNotEmpty()) {
            val current = unvisited.minByOrNull { distances[it] ?: Double.POSITIVE_INFINITY } ?: break
            if (current == endKey || distances[current] == Double.POSITIVE_INFINITY) break
            unvisited.remove(current)

            graph[current]?.forEach { (neighbor, weight) ->
                if (neighbor in unvisited) {
                    val newDist = distances[current]!! + weight
                    if (newDist < (distances[neighbor] ?: Double.POSITIVE_INFINITY)) {
                        distances[neighbor] = newDist
                        previous[neighbor] = current
                    }
                }
            }
        }

        val path = mutableListOf<Point>()
        var cur = endKey
        while (cur != startKey) {
            val parts = cur.split(",")
            path.add(Point(parts[0].toDouble(), parts[1].toDouble(), parts[2].toInt()))
            cur = previous[cur] ?: break
        }
        path.add(Point(request.startX, request.startY, request.floor))
        path.reverse()

        val instructions = buildList {
            add("Начните движение от точки (${path[0].x}, ${path[0].y})")
            for (i in 1 until path.size) {
                val prev = path[i-1]
                val curr = path[i]
                if (prev.floor != curr.floor) {
                    add("Перейдите на этаж ${curr.floor}")
                } else {
                    val dx = curr.x - prev.x
                    val dy = curr.y - prev.y
                    val direction = when {
                        dx > 0 -> "направо"
                        dx < 0 -> "налево"
                        dy > 0 -> "вперед"
                        else -> "назад"
                    }
                    add("Идите $direction до точки (${curr.x}, ${curr.y})")
                }
            }
            add("Вы прибыли в пункт назначения!")
        }

        return RouteResponse(
            points = path,
            distance = distances[endKey] ?: 0.0,
            instructions = instructions
        )
    }
}
//