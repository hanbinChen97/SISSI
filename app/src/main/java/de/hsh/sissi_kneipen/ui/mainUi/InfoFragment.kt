package de.hsh.sissi_kneipen.ui.mainUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.databinding.InfoFragmentBinding
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.ui.SISSIActivity
import de.hsh.sissi_kneipen.util.TITLE_INFO
import de.hsh.sissi_kneipen.util.TabStateEnum
import de.hsh.sissi_kneipen.viewmodel.InfoViewModel
import kotlinx.android.synthetic.main.info_fragment.*
import android.support.v4.app.*
import android.util.Log
import android.widget.TextView
import de.hsh.sissi_kneipen.util.SISSI
import de.hsh.sissi_kneipen.util.TITLE_MAP

class InfoFragment : Fragment() {

    companion object {
        fun newInstance() = InfoFragment()
    }

    lateinit var mBinding: InfoFragmentBinding
    lateinit var viewModel: InfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.info_fragment, container, false)
        return mBinding.root
    }

    private fun setButtonToMap() {
        val myActivity = (activity as SISSIActivity)
        info_button.setOnClickListener {
            val selectedBar = myActivity.repository.sissiDAO.getBar(viewModel.id)
            myActivity.repository.target = selectedBar
            Log.i(SISSI, "target selected: ${selectedBar.id} ${selectedBar.name}")
            // set tab 3 clickable
            myActivity.findViewById<TextView>(R.id.tab3).isClickable = true
            myActivity.mapFragment.viewModel.update(selectedBar)
            toMapAndUpdateTabs()
        }
    }

    private fun toMapAndUpdateTabs() {
        //to Map
        val tabsFragment = (activity as SISSIActivity).tabsFragment
        tabsFragment.state.toMap(activity as SISSIActivity)
        //update Tabs state
        tabsFragment.state = TabStateEnum.MAP
        tabsFragment.refreshTabs()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(InfoViewModel::class.java)

        mBinding.infoVm = viewModel
        mBinding.lifecycleOwner = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonToMap()
    }

    fun update(bar: Bar) {
        viewModel.update(bar)
    }

}
