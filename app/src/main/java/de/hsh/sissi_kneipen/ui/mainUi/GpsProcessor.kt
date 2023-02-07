package de.hsh.sissi_kneipen.ui.mainUi

import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.GpsPosition

interface GpsProcessor {
    fun update(
        gpsPosition: GpsPosition,
        target: Bar,
        autoAdjust: Boolean
    )
}
