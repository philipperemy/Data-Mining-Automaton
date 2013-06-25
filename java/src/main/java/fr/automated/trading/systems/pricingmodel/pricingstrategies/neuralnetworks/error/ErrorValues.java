package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error;

import fr.automated.trading.systems.exception.ErrorValuesException;

import java.util.ArrayList;
import java.util.List;

public class ErrorValues {

    private List<Double> computedValues = new ArrayList<>();
    private List<Double> expectedValues = new ArrayList<>();

    public ErrorValues() {

    }

    public ErrorValues(List<Double> computedValues, List<Double> expectedValues) {
        this.computedValues = computedValues;
        this.expectedValues = expectedValues;
    }

    public List<Double> getComputedValues() {
        return computedValues;
    }

    public List<Double> getExpectedValues() {
        return expectedValues;
    }

    public void setComputedValues(List<Double> computedValues) {
        this.computedValues = computedValues;
    }

    public void setExpectedValues(List<Double> expectedValues) {
        this.expectedValues = expectedValues;
    }

    public int size() {
        try {
            if(expectedValues.size() != computedValues.size())
                throw new ErrorValuesException();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return expectedValues.size();
    }

}
