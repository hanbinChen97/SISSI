package de.hsh.sissi_kneipen.ui.mainUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.ui.SISSIActivity
import de.hsh.sissi_kneipen.util.DES_PREFIX
import de.hsh.sissi_kneipen.util.InfoStateEnum
import de.hsh.sissi_kneipen.util.NOT_IN_BAR
import de.hsh.sissi_kneipen.util.TabStateEnum
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.tab_fragment.*

class TabsFragment : Fragment() {
    private lateinit var myActivity: SISSIActivity
    var state = TabStateEnum.RATING
    var infoState = InfoStateEnum.NOTHING
    companion object {
        fun newInstance() = TabsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myActivity = activity as SISSIActivity
        return inflater.inflate(R.layout.tab_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as SISSIActivity
        refreshTabs()

        tab1.setOnClickListener(View.OnClickListener {
            activity.repository.getRecommendations()
            state.toRec(activity)
            state = TabStateEnum.REC
            refreshTabs()
            tabSwitch(myActivity.pubNow)
        })

        tab2.setOnClickListener(View.OnClickListener {
            state.toMap(activity)
            state = TabStateEnum.MAP
            refreshTabs()
            tabSwitch(myActivity.pubNow)
        })

        tab3.setOnClickListener(View.OnClickListener {
            state.toInfo(activity)
            state = TabStateEnum.INFO
            refreshTabs()
            tabSwitch(myActivity.pubNow)
        })

        tab4.setOnClickListener(View.OnClickListener {
            state.toRating(activity)
            state = TabStateEnum.RATING
            refreshTabs()
            tabSwitch(myActivity.pubNow)
        })

    }

    fun refreshTabs() {
        view?.findViewById<TextView>(R.id.tab1)?.setBackgroundResource(R.color.colorNotSelected)
        view?.findViewById<TextView>(R.id.tab2)?.setBackgroundResource(R.color.colorNotSelected)
        view?.findViewById<TextView>(R.id.tab3)?.setBackgroundResource(R.color.colorNotSelected)
        view?.findViewById<TextView>(R.id.tab4)?.setBackgroundResource(R.color.colorNotSelected)
        when (state) {
            TabStateEnum.REC -> view?.findViewById<TextView>(R.id.tab1)?.setBackgroundResource(R.color.colorSelected)
            TabStateEnum.MAP -> view?.findViewById<TextView>(R.id.tab2)?.setBackgroundResource(R.color.colorSelected)
            TabStateEnum.INFO -> view?.findViewById<TextView>(R.id.tab3)?.setBackgroundResource(R.color.colorSelected)
            TabStateEnum.RATING -> view?.findViewById<TextView>(R.id.tab4)?.setBackgroundResource(R.color.colorSelected)
        }

    }

    private fun tabSwitch(pubNow: Bar){
        if (myActivity.mapFragment.destination.text == DES_PREFIX && pubNow.id == NOT_IN_BAR) {
            tab3.isClickable = false
            tab3.setBackgroundResource(R.color.colorDisable)
        } else {
            tab3.isClickable = true
            tab3.setBackgroundResource(R.color.colorNotSelected)
        }

        if (pubNow.id == "" && myActivity.repository.target.id == "") {
            infoState = InfoStateEnum.NOTHING
            infoState.infoState(myActivity)
        }

        if (myActivity.repository.target.id != "") {
            if (DES_PREFIX + myActivity.repository.target.name == myActivity.mapFragment.destination.text) {
                infoState = InfoStateEnum.NOTHING
                infoState.infoState(myActivity)
            } else {
                infoState = InfoStateEnum.BUTTON
                infoState.infoState(myActivity)
            }
        }

        if (myActivity.tabsFragment.state == TabStateEnum.INFO)
            tab3.setBackgroundResource(R.color.colorSelected)
    }

}
