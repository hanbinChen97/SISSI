package de.hsh.sissi_kneipen.ui.mainUi

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.ui.SISSIActivity
import de.hsh.sissi_kneipen.util.DES_PREFIX
import de.hsh.sissi_kneipen.util.InfoStateEnum
import de.hsh.sissi_kneipen.util.TabStateEnum
import de.hsh.sissi_kneipen.viewmodel.RatingViewModel
import de.hsh.sissi_kneipen.viewmodel.RatingsViewModel
import de.hsh.sissi_kneipen.viewmodel.Rec
import kotlinx.android.synthetic.main.item_rec.*
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.rating_fragment.*
import kotlinx.android.synthetic.main.rec_fragment.*

class RatingFragment : Fragment() {

    companion object {
        fun newInstance() = RatingFragment()
    }

    lateinit var viewModel: RatingsViewModel
    lateinit var selectedRating: Bar
    private lateinit var myActivity: SISSIActivity
    var state = InfoStateEnum.NOTHING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectedRating = Bar("")
        myActivity = activity as SISSIActivity
        return inflater.inflate(R.layout.rating_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RatingsViewModel::class.java)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ratingList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedItem: RatingViewModel= parent?.getItemAtPosition(position) as RatingViewModel
                myActivity.repository.sissiDAO.getBars().forEach {
                    if (selectedItem.name == it.value.name) {
                        selectedRating = myActivity.repository.sissiDAO.getBar(it.value.id)
                        myActivity.repository.target = selectedRating
                        if ( myActivity.repository.target.id == myActivity.repository.myCurrentBar.id) {
                            state = InfoStateEnum.SOJOURN
                            state.infoState(myActivity)
                        }
                        else {
                            if (myActivity.mapFragment.destination.text == DES_PREFIX + myActivity.repository.target.name || myActivity.repository.myCurrentBar.id != "") {
                                state = InfoStateEnum.NOTHING
                                state.infoState(myActivity)
                            }
                            else {
                                state = InfoStateEnum.BUTTON
                                state.infoState(myActivity)
                            }
                        }
                    }
                }

                myActivity.infoFragment.viewModel.update(selectedRating)
                toInfoAndUpdateTabs()
            }
    }

    private fun toInfoAndUpdateTabs() {
        Handler().postDelayed({
            val tabsFragment = myActivity.tabsFragment
            tabsFragment.state.toInfo(myActivity)
            tabsFragment.state = TabStateEnum.INFO
            tabsFragment.refreshTabs()
        }, 1000)
    }
}
