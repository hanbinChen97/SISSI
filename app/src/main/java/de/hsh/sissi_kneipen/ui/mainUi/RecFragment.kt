package de.hsh.sissi_kneipen.ui.mainUi

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.databinding.RecFragmentBinding
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.Recommendation
import de.hsh.sissi_kneipen.ui.SISSIActivity
import de.hsh.sissi_kneipen.util.*
import de.hsh.sissi_kneipen.viewmodel.Rec
import de.hsh.sissi_kneipen.viewmodel.RecsViewModel
import kotlinx.android.synthetic.main.item_rec.*
import kotlinx.android.synthetic.main.map_fragment.*
import kotlinx.android.synthetic.main.rec_fragment.*
import java.util.*


class RecFragment : Fragment() {

    companion object {
        fun newInstance() = RecFragment()
    }

    lateinit var mBinding: RecFragmentBinding
    lateinit var viewModel: RecsViewModel
    lateinit var selectedBar: Bar
    var state = InfoStateEnum.NOTHING

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        selectedBar = Bar("")
        mBinding = DataBindingUtil.inflate(inflater, R.layout.rec_fragment, container, false)
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RecsViewModel::class.java)

        // TODO: Use the ViewModel
        mBinding.recsVm = viewModel
        mBinding.lifecycleOwner = this

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myActivity = activity as SISSIActivity
        recList.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectedItem: Rec = parent?.getItemAtPosition(position) as Rec
                val barId= selectedItem.id
                selectedBar = myActivity.repository.sissiDAO.getBar(barId)

                if (selectedBar.id == myActivity.repository.myCurrentBar.id) {
                    state = InfoStateEnum.SOJOURN
                    state.infoState(myActivity)
                }
                else {
                    if (myActivity.mapFragment.destination.text == DES_PREFIX + selectedBar.name || myActivity.repository.myCurrentBar.id != "") {
                        state = InfoStateEnum.NOTHING
                        state.infoState(myActivity)
                    } else {
                        state = InfoStateEnum.BUTTON
                        state.infoState(myActivity)
                    }
                }

                myActivity.infoFragment.viewModel.update(selectedBar)
                toInfoAndUpdateTabs()
            }
    }


    private fun toInfoAndUpdateTabs() {
        Handler().postDelayed({
            val tabsFragment = (activity as SISSIActivity).tabsFragment
            tabsFragment.state.toInfo(activity as SISSIActivity)
            tabsFragment.state = TabStateEnum.INFO
            tabsFragment.refreshTabs()
        }, 1000)
    }

    fun update(recommendations: ArrayList<Recommendation>) {
        viewModel.update(recommendations)
    }


}
