package info.hieule.arx_automation.app.demo.adapters

import info.hieule.arx_automation.ports.ResultsConsumer
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import org.deidentifier.arx.ARXResult
import org.deidentifier.arx.Data
import java.text.DecimalFormat

class ConsoleResultsConsumer : ResultsConsumer {
    override fun consume(result: ARXResult, data: Data, request: AnonymizationRequest) {
        val df1 = DecimalFormat("#####0.00")
        val sTotal = df1.format(result.time / 1000.toDouble()) + "s"
        println(" - Time needed: $sTotal")

        val optimum = result.globalOptimum
        val qis = ArrayList<String>(data.definition.quasiIdentifyingAttributes)

        if (optimum == null) {
            println(" - No solution found!")
            return
        }

        val identifiers = Array(qis.size) { StringBuffer() }
        val generalizations = Array(qis.size) { StringBuffer() }

        var lengthI = 0
        var lengthG = 0

        // Initialise
        for (i in 0 until qis.size) {
            identifiers[i] = StringBuffer()
            generalizations[i] = StringBuffer()

            identifiers[i].append(qis[i])
            generalizations[i].append(optimum.getGeneralization(qis[i]))

            if (data.definition.isHierarchyAvailable(qis[i])) {
                generalizations[i].append("/")
                    ?.append(data.definition.getHierarchy(qis[i])[0].size - 1)
            }

            lengthI = lengthI.coerceAtLeast(identifiers[i].length)
            lengthG = lengthG.coerceAtLeast(generalizations[i].length)
        }

        // Padding
        for (i in 0 until qis.size) {
            while (identifiers[i].length < lengthI) {
                identifiers[i].append(" ")
            }
            while (generalizations[i].length < lengthG) {
                generalizations[i].append(" ");
            }
        }

        // Print
        println(" - Information loss: ${result.globalOptimum.lowestScore} / ${result.globalOptimum.highestScore}")
        println(" - Optimal generalization")
        for (i in 0 until qis.size) {
            println("   * ${identifiers[i]}: ${generalizations[i]}")
        }
        println(" - Statistics")
        println(result.getOutput(result.globalOptimum, false).statistics.equivalenceClassStatistics)
    }
}
