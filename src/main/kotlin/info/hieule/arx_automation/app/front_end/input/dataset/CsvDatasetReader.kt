package info.hieule.arx_automation.app.front_end.input.dataset

import org.deidentifier.arx.Data
import java.nio.charset.Charset
import java.nio.file.Paths

class CsvDatasetReader(private val datasetName: String, private val separator: Char = ';'): DatasetReader {
    override fun read(): Data {
        val path = Paths.get("data", "${this.datasetName}_int.csv")
        return Data.create(path.toString(), Charset.defaultCharset(), this.separator);
    }
}