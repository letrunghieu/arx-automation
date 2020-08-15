package info.hieule.arx_automation.app.front_end.input.hierarchies

import info.hieule.arx_automation.shared.models.Dataset

interface HierachyReader {
    fun read(): Dataset
}
