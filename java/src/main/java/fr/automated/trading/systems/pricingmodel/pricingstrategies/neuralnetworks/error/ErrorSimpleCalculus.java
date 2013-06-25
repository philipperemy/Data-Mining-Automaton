package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error;

import fr.automated.trading.systems.exception.ErrorCalculusException;

import java.util.List;

public class ErrorSimpleCalculus implements ErrorCalculus {

    public double calculate(List<Double> computedValues, List<Double> expectedValues) throws ErrorCalculusException {

        double errorRate = 0.0;

        if(computedValues.size() != expectedValues.size())
            throw new ErrorCalculusException();

        int size = computedValues.size();

        for(int i=0; i<size; i++)
            errorRate += Math.abs(expectedValues.get(i) - computedValues.get(i));

        errorRate /= size;
        return errorRate;
    }
}
