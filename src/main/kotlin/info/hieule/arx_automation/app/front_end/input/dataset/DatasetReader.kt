package info.hieule.arx_automation.app.front_end.input.dataset

import org.deidentifier.arx.Data

interface DatasetReader {
    fun read(): Data
}