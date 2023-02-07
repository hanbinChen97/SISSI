package de.hsh.sissi_kneipen.model

import kotlinx.serialization.Serializable

@Serializable
class Recommendation(val rating: Int, val bar: Bar) {
    override fun toString(): String {
        return "$rating, ${bar.id}"
    }
}