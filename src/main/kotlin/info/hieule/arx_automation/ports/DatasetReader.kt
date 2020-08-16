package info.hieule.arx_automation.ports

import info.hieule.arx_automation.shared.models.Dataset

interface DatasetReader {
    fun read(): Dataset
}
