package de.hsh.sissi_kneipen.model

import kotlinx.serialization.Serializable

@Serializable
class Arrival(val bar: Bar, var time: Long) {
    override fun toString(): String {
        return "${bar.id}, $time"
    }
}