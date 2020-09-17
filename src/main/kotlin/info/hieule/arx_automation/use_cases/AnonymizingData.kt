package info.hieule.arx_automation.use_cases

import info.hieule.arx_automation.ports.DataProvider
import info.hieule.arx_automation.ports.ResultsConsumer
import info.hieule.arx_automation.shared.models.AnonymizationRequest
import org.deidentifier.arx.ARXAnonymizer
import org.deidentifier.arx.ARXConfiguration
import org.deidentifier.arx.criteria.DistinctLDiversity
import org.deidentifier.arx.criteria.KAnonymity
import org.deidentifier.arx.metric.Metric

class AnonymizingData(
    private val dataProvider: DataProvider,
    private val resultsConsumer: ResultsConsumer
) : UseCase<AnonymizationRequest, Int> {

    private companion object {
        const val K_KEY = "k"
        const val L_KEY = "l"
        const val SUPPRESION_LIMIT_KEY = "suppressionLimit"
    }

    override fun execute(request: AnonymizationRequest): Int {
        val data = this.dataProvider.getData()
        val anonymizer = ARXAnonymizer()
        val configuration = ARXConfiguration.create()

        if (request.anonymizeModelConfigs.containsKey(K_KEY)) {
            configuration.addPrivacyModel(KAnonymity(request.anonymizeModelConfigs[K_KEY].toString().toInt()))
        }
        configuration.suppressionLimit = 0.99
        if (request.anonymizeModelConfigs.containsKey(SUPPRESION_LIMIT_KEY)) {
            configuration.suppressionLimit = request.anonymizeModelConfigs[SUPPRESION_LIMIT_KEY].toString().toDouble()
        }

        if (request.anonymizeModelConfigs.containsKey(L_KEY)) {
            configuration.addPrivacyModel(
                DistinctLDiversity(
                    request.sensitiveAttributes[0],
                    request.anonymizeModelConfigs[L_KEY].toString().toInt()
                )
            )
        }
        configuration.qualityModel = Metric.createLossMetric(0.0)
        val result = anonymizer.anonymize(data, configuration)
        resultsConsumer.consume(result, data, request)

        return 0
    }
}
