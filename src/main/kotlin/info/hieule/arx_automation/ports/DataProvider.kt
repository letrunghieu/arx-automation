package info.hieule.arx_automation.ports

import org.deidentifier.arx.Data

interface DataProvider {
    fun getData(): Data
}
