package de.hsh.sissi_kneipen.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import de.hsh.sissi_kneipen.model.Bar
import de.hsh.sissi_kneipen.util.PICTURE_PATH

class InfoViewModel : ViewModel() {
    fun update(bar: Bar) {
        id = bar.id
        name.set(bar.name)
        description.set(bar.description)
        imageUrl.set(bar.getImageUrl())
    }

    var id = String()
    var name = ObservableField<String>("")
    var description = ObservableField<String>("")
    var imageUrl = ObservableField<String>("")
}
