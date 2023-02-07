package de.hsh.sissi_kneipen.model

import de.hsh.sissi_kneipen.util.PICTURE_PATH
import kotlinx.serialization.Serializable

@Serializable
class Bar(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val thumbnail_link: String,
    val position: GpsPosition
) {
    constructor(id: String) : this(id, "", "", "", "", GpsPosition(0.0, 0.0))

    fun getImageUrl(): String {
        return "$PICTURE_PATH/$id.jpg"
    }

    override fun toString(): String {
        return "$id, $name"
    }
}