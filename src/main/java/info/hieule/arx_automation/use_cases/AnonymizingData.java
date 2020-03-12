package info.hieule.arx_automation.use_cases;

import info.hieule.arx_automation.ports.DataProvider;
import info.hieule.arx_automation.ports.ResultsConsumer;
import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.criteria.KAnonymity;

import java.io.IOException;

public class AnonymizingData {
    public void execute(DataProvider dataProvider, ResultsConsumer resultsConsumer) throws IOException {
        Data data = dataProvider.getData();

        ARXAnonymizer anonymizer = new ARXAnonymizer();

        ARXConfiguration configuration = ARXConfiguration.create();
        configuration.addPrivacyModel(new KAnonymity(3));
        configuration.setSuppressionLimit(0d);

        ARXResult result = anonymizer.anonymize(data, configuration);

        resultsConsumer.consume(result);
    }
}
