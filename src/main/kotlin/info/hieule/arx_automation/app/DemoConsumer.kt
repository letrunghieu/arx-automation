package info.hieule.arx_automation.app

import info.hieule.arx_automation.app.demo.adapters.ConsoleResultsConsumer
import info.hieule.arx_automation.app.demo.adapters.DemoDataProvider01
import info.hieule.arx_automation.use_cases.AnonymizingData

fun main(args: Array<String>) {
    val dataProvider = DemoDataProvider01()
    val resultsConsumer = ConsoleResultsConsumer()

    val useCase = AnonymizingData(dataProvider, resultsConsumer)
    useCase.execute(0)
}
