package de.hsh.sissi_kneipen.model

import kotlinx.serialization.Serializable

@Serializable
class GpsPosition(var latitude: Double, var longitude: Double) {
    override fun toString(): String {
        return "latitude:$latitude, longitude:$longitude"
    }
}