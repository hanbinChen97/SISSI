package de.hsh.sissi_kneipen.persistence

import de.hsh.sissi_kneipen.model.Bar
import java.util.*

interface BarsInitializer {
    fun loadBars(): HashMap<String, Bar>
}