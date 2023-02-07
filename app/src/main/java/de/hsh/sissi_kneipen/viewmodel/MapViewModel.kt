package de.hsh.sissi_kneipen.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.util.DES_PREFIX

class MapViewModel : ViewModel() {
    fun update(bar: Bar) {
        name.set(DES_PREFIX +bar.name)
    }

    var name = ObservableField(DES_PREFIX)
}