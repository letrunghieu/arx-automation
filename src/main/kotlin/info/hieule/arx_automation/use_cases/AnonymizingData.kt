package info.hieule.arx_automation.use_cases

import info.hieule.arx_automation.ports.DataProvider
import info.hieule.arx_automation.ports.ResultsConsumer
import org.deidentifier.arx.ARXAnonymizer
import org.deidentifier.arx.ARXConfiguration
import org.deidentifier.arx.criteria.KAnonymity

class AnonymizingData {
    fun execute(dataProvider: DataProvider, resultsConsumer: ResultsConsumer) {
        val data = dataProvider.data
        val anonymizer = ARXAnonymizer()
        val configuration = ARXConfiguration.create()
        configuration.addPrivacyModel(KAnonymity(3))
        configuration.suppressionLimit = 0.0
        val result = anonymizer.anonymize(data, configuration)
        resultsConsumer.consume(result)
    }
}
