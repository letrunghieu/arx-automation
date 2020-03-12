package info.hieule.arx_automation.ports;

import org.deidentifier.arx.ARXResult;

public interface ResultsConsumer {
    void consume(ARXResult result);
}
