package de.hsh.sissi_kneipen.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import de.hsh.sissi_kneipen.model.GpsPosition

class GpsViewModel : ViewModel() {
    fun update(gps: GpsPosition) {
        latitude.set(gps.latitude)
        longitude.set(gps.longitude)

    }

    var latitude = ObservableField<Double>(0.0)
    var longitude =  ObservableField<Double>(0.0)

}
