package info.hieule.arx_automation.app.springboot.adapter.dataset_writer

import info.hieule.arx_automation.app.springboot.consumer.configuration.CkanConnectorConfiguration
import info.hieule.arx_automation.ports.DatasetWriter
import info.hieule.arx_automation.shared.models.Dataset
import org.springframework.stereotype.Component

@Component
class CkanDatasetWriter(
        private val ckanConnectorConfiguration: CkanConnectorConfiguration
) : DatasetWriter {
    override fun write(dataset: Dataset): Dataset {
        TODO("Not yet implemented")
    }
}
