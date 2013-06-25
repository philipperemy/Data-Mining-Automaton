package fr.automated.trading.systems.tradingrobot.datamanager.inputvectors;

import fr.automated.trading.systems.utils.CSVData;

import java.util.List;

public interface BuildInputVectors {

    List<List<Double>> build(CSVData csvData, int inputNeuronsCount);
}
