package fr.automated.trading.systems.tradingrobot.datamanager.expectedvalues;

import fr.automated.trading.systems.utils.CSVData;
import fr.automated.trading.systems.utils.csvparser.CSVDataInterface;

import java.util.List;

public interface BuildExpectedValues {

    List<Double> build(CSVData csvData);

}
