package de.hsh.sissi_kneipen.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.hsh.sissi_kneipen.R
import de.hsh.sissi_kneipen.databinding.GpsFragmentBinding
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.model.GpsPosition
import de.hsh.sissi_kneipen.ui.mainUi.GpsProcessor
import de.hsh.sissi_kneipen.viewmodel.GpsViewModel

class GpsFragment : Fragment(), GpsProcessor {

    companion object {
        fun newInstance() = GpsFragment()
    }

    lateinit var mBinding: GpsFragmentBinding
    lateinit var viewModel: GpsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.gps_fragment, container, false)

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GpsViewModel::class.java)

        mBinding.gpsVm = viewModel
        mBinding.lifecycleOwner = this

    }

    private fun runtime_permissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this.activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this.activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
            return true
        }
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            } else {
                runtime_permissions()
            }
        }
    }

    override fun update(
        gpsPosition: GpsPosition,
        target: Bar,
        autoAdjust: Boolean
    ) {
        viewModel.update(gpsPosition)
    }
}
