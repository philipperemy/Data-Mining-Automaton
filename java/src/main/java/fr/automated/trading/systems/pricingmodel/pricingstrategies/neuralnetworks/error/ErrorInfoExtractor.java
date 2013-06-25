package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.error;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ErrorInfoExtractor {

    public static Map<Double, Double> extractOccurrences(ErrorValues errorValues) {
        Map<Double, Double> occurrences = new HashMap<>();

        Set<Double> noRedundancy = new HashSet<>();
        for(int i=0; i<errorValues.size(); i++) {
            noRedundancy.add(errorValues.getComputedValues().get(i));
        }

        for (Double aNoRedundancy : noRedundancy) {
            occurrences.put(aNoRedundancy, (double) 0);
        }

        for(int i=0; i<errorValues.size(); i++) {
            double lastOccurrence = occurrences.get(errorValues.getComputedValues().get(i));
            lastOccurrence++;
            occurrences.put(errorValues.getComputedValues().get(i), lastOccurrence);
        }

        return occurrences;
    }

    public static Map<String, Integer> extractModelError(ErrorValues errorValues) {

        Map<String, Integer> modelErrors = new HashMap<>();

        int ok = 0, ko = 0;

        for(int i=0; i<errorValues.size(); i++) {

            double computedValue = errorValues.getComputedValues().get(i);
            double expectedValue = errorValues.getExpectedValues().get(i);

            if(computedValue == expectedValue)
                ok++;
            else
                ko++;

        }

        modelErrors.put("OK", ok);
        modelErrors.put("ok", ok);

        modelErrors.put("KO", ko);
        modelErrors.put("ko", ko);

        return modelErrors;
    }
}
