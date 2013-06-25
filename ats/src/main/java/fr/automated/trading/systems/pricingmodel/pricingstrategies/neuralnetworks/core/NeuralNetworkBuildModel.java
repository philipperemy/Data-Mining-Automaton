package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core;


import fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.binarycounter.BinaryCounter;
import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class NeuralNetworkBuildModel extends NeuralNetworkProcess {

    List<List<Double>> modelRecords = new ArrayList<>();

    public void fill() {

        BinaryCounter binaryCounter = new BinaryCounter(16);

        for(int i=0; i<65536;i++) {
            double[] input = binaryCounter.stringToArray(binaryCounter.incCounter());
            double runningValue = neuralNetwork.run(input);
            addRecord(input, runningValue);
        }
    }

    public void addRecord(double[] input, double runningValue) {
        List<Double> record = new ArrayList<>();
        for(int i=0; i<input.length; i++) {
            record.add(input[i]);
        }
        record.add(runningValue);
        modelRecords.add(record);
    }

    public void printRecords() {
        try {

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("model.model")));
            for(List<Double> record : modelRecords) {
                printRecord(record, writer);
            }
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void printRecord(List<Double> record, PrintWriter writer) {
        try {

            for(int i=0; i<record.size(); i++) {
                if(i==0) {
                    writer.print(" [ " + record.get(i) + ";");
                }
                else if(i!=record.size()-1) {
                    writer.print(record.get(i) + ";");
                } else {
                    writer.println(record.get(i) + " ]");
                }

                writer.flush();
            }
        } catch (Exception e) {
            AtsLogger.logException("", e);
        }
    }

}
