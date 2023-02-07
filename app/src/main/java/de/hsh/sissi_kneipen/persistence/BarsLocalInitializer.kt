package de.hsh.sissi_kneipen.persistence

import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.GpsPosition

class BarsLocalInitializer : BarsInitializer {
    override fun loadBars(): HashMap<String, Bar> {
        val bar0 = Bar(
            "11",
            "Duke Irish Pub",
            ".",
            "pub",
            "https://sissi.inform.hs-hannover.de/media/pub/poi/11.jpg",
            GpsPosition(52.385007, 9.722524))
        val bar1 = Bar(
            "10",
            "Destille",
            "Urige Kneipe mit rot-grünem Dekor, die bis in die frühen Morgenstunden deftige internationale Küche serviert.",
            "pub",
            "https://sissi.inform.hs-hannover.de/media/pub/poi/10.jpg",
            GpsPosition(52.382474, 9.72174)
        )
        val bar2 = Bar(
            "7",
            "Klein Kröpcke",
            "Einst eine Szenekneipe, inzwischen längst ein Traditionslokal. Seit jeher trifft man sich im Klein Kröpcke. Ob alteingesessene Nordstädter, Studenten oder Stammtische: Es wird gegessen und getrunken, Skat gespielt, geknobelt und geplauscht.",
            "pub",
            "https://sissi.inform.hs-hannover.de/media/pub/poi/2.jpg",
            GpsPosition(52.386169, 9.717215)
        )
        val bar3 = Bar(
            "6",
            "Spektakel",
            "Das Wohnzimmer der Oststadt.",
            "pub",
            "https://sissi.inform.hs-hannover.de/media/pub/poi/3.jpg",
            GpsPosition(52.385032, 9.749804)
        )

        return hashMapOf<String, Bar>(bar0.id to bar0, bar1.id to bar1, bar2.id to bar2, bar3.id to bar3)
    }

}