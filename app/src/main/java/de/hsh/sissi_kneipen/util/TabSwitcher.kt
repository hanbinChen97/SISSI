package de.hsh.sissi_kneipen.util

import de.hsh.sissi_kneipen.ui.SISSIActivity

interface TabSwitcher {
    fun toRec(activity: SISSIActivity)
    fun toMap(activity: SISSIActivity)
    fun toInfo(activity: SISSIActivity)
    fun toRating(activity: SISSIActivity)
}