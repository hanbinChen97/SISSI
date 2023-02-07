package de.hsh.sissi_kneipen.util

import android.view.View
import android.widget.LinearLayout
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.ui.SISSIActivity

enum class InfoStateEnum: InfoSwitcher {
    BUTTON {
        override fun infoState(activity: SISSIActivity) {
            activity.findViewById<LinearLayout>(R.id.goLayout).visibility = View.VISIBLE
            activity.findViewById<LinearLayout>(R.id.timeLayout).visibility = View.GONE
        }
    },
    SOJOURN {
        override fun infoState(activity: SISSIActivity) {
            activity.findViewById<LinearLayout>(R.id.goLayout).visibility = View.GONE
            activity.findViewById<LinearLayout>(R.id.timeLayout).visibility = View.VISIBLE
        }
    },
    NOTHING {
        override fun infoState(activity: SISSIActivity) {
            activity.findViewById<LinearLayout>(R.id.goLayout).visibility = View.GONE
            activity.findViewById<LinearLayout>(R.id.timeLayout).visibility = View.GONE
        }
    }
}