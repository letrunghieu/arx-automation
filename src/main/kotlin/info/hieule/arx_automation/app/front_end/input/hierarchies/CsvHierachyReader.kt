package info.hieule.arx_automation.app.front_end.input.hierarchies

import info.hieule.arx_automation.shared.models.Dataset
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Paths

class CsvHierachyReader(
    private val datasetName: String,
    private val attributeName: String,
    private val separator: Char = ';'
): HierachyReader {
    override fun read(): Dataset {
        val path = Paths.get("hierarchies", "${this.datasetName}_int_hierarchy_${this.attributeName}.csv")

        val reader = Files.newBufferedReader(path)
        val csvParser = CSVParser(reader, CSVFormat.newFormat(';'))

        val dataRows: ArrayList<Array<String>> = arrayListOf()
        for(csvRecord in csvParser) {
            dataRows.add(csvRecord.toList().toTypedArray())
        }

        return Dataset(data = dataRows)
    }
}
