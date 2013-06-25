package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error;

import fr.automated.trading.systems.tradingrobot.datamanager.DataManager;
import fr.automated.trading.systems.utils.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ErrorSetReduction {

    public static ErrorValues transform(ErrorValues errorValues, DataManager dataManager, int delimiter) {

        List<Double> computedTransformedValues = new ArrayList<>();
        List<Double> expectedTransformedValues = new ArrayList<>();

        ErrorValues errorTransformedValues = new ErrorValues();

        int end = dataManager.getResults().size();

        for(int line = delimiter; line<end; line++) {
            double computedValue = errorValues.getComputedValues().get(line-delimiter);
            double expectedValue = errorValues.getExpectedValues().get(line-delimiter);

            if(expectedValue == 1.0) {
                if(dataManager.getCsvData().getValue(line, Constants.CLOSE_IDENTIFIER) < dataManager.getCsvData().getValue(line, Constants.OPEN_IDENTIFIER)) {
                    computedTransformedValues.add(computedValue);
                    expectedTransformedValues.add(expectedValue);
                }
            }

            if(expectedValue == 0.0) {
                if(dataManager.getCsvData().getValue(line, Constants.CLOSE_IDENTIFIER) > dataManager.getCsvData().getValue(line, Constants.OPEN_IDENTIFIER)) {
                    computedTransformedValues.add(computedValue);
                    expectedTransformedValues.add(expectedValue);
                }
            }
        }

        errorTransformedValues.setComputedValues(computedTransformedValues);
        errorTransformedValues.setExpectedValues(expectedTransformedValues);

        return errorTransformedValues;

    }

}
