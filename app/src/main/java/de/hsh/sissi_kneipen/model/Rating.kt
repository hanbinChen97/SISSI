package de.hsh.sissi_kneipen.model

import kotlinx.serialization.Serializable

@Serializable
class Rating(val bar: Bar, val rating: Double)