package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error;

import java.util.ArrayList;
import java.util.List;

public class ErrorValuesTransformers {

    public static ErrorValues transform(ErrorValues errorValues) {

        List<Double> computedTransformedValues = new ArrayList<>();
        List<Double> expectedTransformedValues = new ArrayList<>();

        ErrorValues errorTransformedValues = new ErrorValues();

        for(int i=0; i<errorValues.size(); i++) {

            if(errorValues.getComputedValues().get(i) > 0.5) {
                computedTransformedValues.add((double) 1);
            } else {
                computedTransformedValues.add((double) 0);
            }
            expectedTransformedValues.add(errorValues.getExpectedValues().get(i));
        }


        errorTransformedValues.setComputedValues(computedTransformedValues);
        errorTransformedValues.setExpectedValues(expectedTransformedValues);

        return errorTransformedValues;

    }

}
