package info.hieule.arx_automation.ports

import org.deidentifier.arx.ARXResult

interface ResultsConsumer {
    fun consume(result: ARXResult)
}
