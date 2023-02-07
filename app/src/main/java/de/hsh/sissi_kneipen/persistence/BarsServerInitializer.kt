package de.hsh.sissi_kneipen.persistence

import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.util.PUB_PATH
import kotlinx.serialization.internal.ArrayListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.net.URL
import java.util.*
import kotlin.collections.HashMap

class BarsServerInitializer : BarsInitializer {
    private val json = Json(JsonConfiguration.Stable)

    override fun loadBars(): HashMap<String, Bar> {
        val barsHashMap = HashMap<String, Bar>()
        val barsResult = URL("$PUB_PATH/poi").readText()
        val bars = json.parse(ArrayListSerializer(Bar.serializer()), barsResult) as ArrayList<Bar>
        for (bar in bars){
            barsHashMap[bar.id] = bar
        }
        return barsHashMap
    }
}