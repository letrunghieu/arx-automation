package info.hieule.arx_automation.app.demo.adapters

import info.hieule.arx_automation.ports.DataProvider
import org.deidentifier.arx.AttributeType.Hierarchy
import org.deidentifier.arx.Data

class DemoDataProvider01 : DataProvider {
    override fun getData(): Data {
        val data = Data.create()

        data.add("age", "gender", "zipcode")
        data.add("34", "male", "81667")
        data.add("45", "female", "81675")
        data.add("66", "male", "81925")
        data.add("70", "female", "81931")
        data.add("34", "female", "81931")
        data.add("70", "male", "81931")
        data.add("45", "male", "81931")

        val age = Hierarchy.create()
        age.add("34", "<50", "*")
        age.add("45", "<50", "*")
        age.add("66", ">=50", "*")
        age.add("70", ">=50", "*")

        val gender = Hierarchy.create()
        gender.add("male", "*")
        gender.add("female", "*")

        val zipcode = Hierarchy.create()
        zipcode.add("81667", "8166*", "816**", "81***", "8****", "*****")
        zipcode.add("81675", "8167*", "816**", "81***", "8****", "*****")
        zipcode.add("81925", "8192*", "819**", "81***", "8****", "*****")
        zipcode.add("81931", "8193*", "819**", "81***", "8****", "*****")

        data.definition.setAttributeType("age", age)
        data.definition.setAttributeType("gender", gender)
        data.definition.setAttributeType("zipcode", zipcode)

        return data
    }
}
