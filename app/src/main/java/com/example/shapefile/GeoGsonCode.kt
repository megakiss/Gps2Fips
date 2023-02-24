package com.example.shapefile

import android.graphics.PointF
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser

class GeoGsonCode(jsonString: String) {
    data class Feature(
        val name: String,
        val geoId: Int,
        val coordinates: ArrayList<ArrayList<PointF>>
    )

    val features = ArrayList<Feature>()

    // https://www.algorithms-and-technologies.com/point_in_polygon/java
    private fun pointInPolygon(polygon: ArrayList<PointF>, point: PointF): Boolean {
        var cross = 0
        polygon.forEachIndexed { index, curPoint ->
            val prePoint = polygon[if (index == 0) polygon.size - 1 else index - 1]
            // 찾는 점의 Y 좌표는 폴리곤 두 점의 Y 좌표 사이에 있어야 한다.
            if (curPoint.y < point.y != prePoint.y < point.y) {
                // 찾는 점의 X 좌표가 폴리곤 라인 상의 X 좌표보다 작으면 교차 카운트에 1을 더한다.
                val x =
                    (prePoint.x - curPoint.x) * (point.y - curPoint.y) / (prePoint.y - curPoint.y) + curPoint.x
                if (point.x <= x) cross++
            }
        }
        return (cross % 2 == 1)
    }

    fun getFipsCode(latitude: Double, longitude: Double): Int {
        val point = PointF(longitude.toFloat(), latitude.toFloat())
        features.forEach { feature ->
            feature.coordinates.forEach { polygon ->
                if (pointInPolygon(polygon, point)) {
                    Log.i(TAG, "${feature.name} ${feature.geoId}")
                    return feature.geoId
                }
            }
        }
        return 0
    }

    init {
        val jsonObject: JsonObject = JsonParser.parseString(jsonString).asJsonObject
        val jsonFeatures = jsonObject.getAsJsonArray("features")
        jsonFeatures.forEach { jsonFeature ->
            val jsonProperties = jsonFeature.asJsonObject.getAsJsonObject("properties")
            var name = "NO NAME"
            jsonProperties.get("NAME")?.let {name = it.asString}
            jsonProperties.get("SIG_KOR_NM")?.let {name = it.asString}
            var geoID = 0
            jsonProperties.get("GEOID")?.let {geoID = it.asInt}
            jsonProperties.get("SIG_CD")?.let {geoID = it.asInt}
            //Log.i(TAG, "name:$name geoID:$geoID")

            val jsonGeometry = jsonFeature.asJsonObject.getAsJsonObject("geometry")
            val jsonCoordinates = jsonGeometry.getAsJsonArray("coordinates")
            val coordinates = ArrayList<ArrayList<PointF>>()
            when (jsonGeometry.get("type").asString) {
                "Polygon" -> {
                    val coordinate = ArrayList<PointF>()
                    jsonCoordinates.asJsonArray.forEach { polygon ->
                        polygon.asJsonArray.forEach { xy ->
                            coordinate.add(
                                (PointF(
                                    xy.asJsonArray[0].asFloat, xy.asJsonArray[1].asFloat
                                ))
                            )
                        }
                    }
                    coordinates.add(coordinate)
                }
                "MultiPolygon" -> {
                    jsonCoordinates.asJsonArray.forEach { polygons ->
                        val coordinate = ArrayList<PointF>()
                        polygons.asJsonArray.forEach { polygon ->
                            polygon.asJsonArray.forEach { xy ->
                                coordinate.add(
                                    (PointF(
                                        xy.asJsonArray[0].asFloat, xy.asJsonArray[1].asFloat
                                    ))
                                )
                            }
                        }
                        coordinates.add(coordinate)
                    }
                }
            }
            features.add(Feature(name, geoID, coordinates))
        }
    }

    companion object {
        private const val TAG = "UsaFips"
    }
}