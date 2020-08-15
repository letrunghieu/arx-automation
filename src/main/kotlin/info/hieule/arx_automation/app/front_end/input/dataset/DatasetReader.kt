package info.hieule.arx_automation.app.front_end.input.dataset

import info.hieule.arx_automation.shared.models.Dataset

interface DatasetReader {
    fun read(): Dataset
}
