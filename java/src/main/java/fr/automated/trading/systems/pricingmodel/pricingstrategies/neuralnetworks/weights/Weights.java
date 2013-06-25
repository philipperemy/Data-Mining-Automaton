package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.weights;

public class Weights {

    private final int sizeX;
    private final int sizeY;
    private double[][] weights;

    public Weights(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        weights = new double[sizeX][sizeY];
        random();
    }

    public double[][] getWeights() {
        return weights;
    }

    public void setWeights(double[][] weights) {
        this.weights = weights;
    }

    public double getWeight(int x, int y) {
        return weights[x][y];
    }

    public void setWeight(int x, int y, double value) {
        weights[x][y] = value;
    }


    public Weights clone() {
        Weights returnedWeights = new Weights(sizeX, sizeY);
        double[][] array = new double[sizeX][sizeY];
        for(int i=0;i<sizeX;i++) {
            System.arraycopy(weights[i], 0, array[i], 0, sizeY);
        }
        returnedWeights.setWeights(array);
        return returnedWeights;
    }

    public void random() {
        for(int i=0;i<sizeX;i++)
            for (int j=0;j<sizeY;j++)
                setWeight(i,j, (Math.random() - 0.5));
    }
}
