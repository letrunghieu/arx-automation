package info.hieule.arx_automation.app.adapter.dataset_reader

import info.hieule.arx_automation.ports.DatasetReader
import info.hieule.arx_automation.shared.models.Dataset
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class CsvDatasetReader(private val filePath: Path, private val separator: Char = ';') : DatasetReader {
    override fun read(): Dataset {
        val reader = Files.newBufferedReader(filePath)
        val csvParser = CSVParser(reader, CSVFormat.newFormat(';'))

        val dataRows: ArrayList<Array<String>> = arrayListOf()
        for (csvRecord in csvParser) {
            dataRows.add(csvRecord.toList().toTypedArray())
        }

        return Dataset(data = dataRows)
    }
}
