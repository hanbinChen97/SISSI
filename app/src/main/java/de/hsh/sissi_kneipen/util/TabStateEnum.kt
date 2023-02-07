package de.hsh.sissi_kneipen.util

import de.hsh.sissi_kneipen.ui.SISSIActivity


enum class TabStateEnum : TabSwitcher {
    REC {
        override fun toRec(activity: SISSIActivity) {
        }

        override fun toMap(activity: SISSIActivity) {

            activity.switchFragment(activity.mapFragment)
            activity.supportActionBar?.title = TITLE_MAP
        }

        override fun toInfo(activity: SISSIActivity) {
            activity.switchFragment(activity.infoFragment)
            activity.supportActionBar?.title = TITLE_INFO
        }

        override fun toRating(activity: SISSIActivity) {
            activity.switchFragment(activity.ratingFragment)
            activity.supportActionBar?.title = TITLE_RATING
        }
    },
    MAP {
        override fun toRec(activity: SISSIActivity) {
            activity.switchFragment(activity.recFragment)
            activity.supportActionBar?.title = TITLE_REC
        }

        override fun toMap(activity: SISSIActivity) {
        }

        override fun toInfo(activity: SISSIActivity) {
            activity.switchFragment(activity.infoFragment)
            activity.supportActionBar?.title = TITLE_INFO
        }

        override fun toRating(activity: SISSIActivity) {
            activity.switchFragment(activity.ratingFragment)
            activity.supportActionBar?.title = TITLE_RATING
        }
    },
    INFO {
        override fun toRec(activity: SISSIActivity) {
            activity.switchFragment(activity.recFragment)
            activity.supportActionBar?.title = TITLE_REC
        }

        override fun toMap(activity: SISSIActivity) {
            activity.mapFragment.update(activity.gpsPosition, activity.repository.target, true)

            activity.switchFragment(activity.mapFragment)
            activity.supportActionBar?.title = TITLE_MAP

        }

        override fun toInfo(activity: SISSIActivity) {
        }

        override fun toRating(activity: SISSIActivity) {
            activity.switchFragment(activity.ratingFragment)
            activity.supportActionBar?.title = TITLE_RATING

        }
    },
    RATING;

    override fun toRec(activity: SISSIActivity) {
        activity.switchFragment(activity.recFragment)
        activity.supportActionBar?.title = TITLE_REC
    }

    override fun toMap(activity: SISSIActivity) {
        activity.mapFragment.update(activity.gpsPosition, activity.repository.target, true)

        activity.switchFragment(activity.mapFragment)
        activity.supportActionBar?.title = TITLE_MAP
    }

    override fun toInfo(activity: SISSIActivity) {
        activity.switchFragment(activity.infoFragment)
        activity.supportActionBar?.title = TITLE_INFO
    }

    override fun toRating(activity: SISSIActivity) {
    }

}
