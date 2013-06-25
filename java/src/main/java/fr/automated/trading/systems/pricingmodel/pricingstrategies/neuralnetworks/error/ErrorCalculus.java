package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error;

import fr.automated.trading.systems.exception.ErrorCalculusException;

import java.util.List;

public interface ErrorCalculus {
    
    double calculate(List<Double> computedValues, List<Double> expectedValues) throws ErrorCalculusException;
}
