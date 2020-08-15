package info.hieule.arx_automation.app.front_end.input.dataset

import info.hieule.arx_automation.shared.models.Dataset
import org.deidentifier.arx.Data
import java.nio.charset.Charset
import java.nio.file.Paths

class CsvDatasetReader(private val datasetName: String, private val separator: Char = ';') : DatasetReader {
    override fun read(): Dataset {
        val path = Paths.get("data", "${this.datasetName}_int.csv")
        val data = Data.create(path.toString(), Charset.defaultCharset(), this.separator);

        val dataRows: ArrayList<Array<String>> = arrayListOf()
        for (row in data.handle.iterator()) {
            dataRows.add(row)
        }

        return Dataset(data = dataRows)
    }
}
