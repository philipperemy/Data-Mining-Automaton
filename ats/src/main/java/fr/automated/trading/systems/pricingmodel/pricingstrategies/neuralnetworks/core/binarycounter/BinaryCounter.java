package fr.automated.trading.systems.pricingmodel.pricingstrategies.neuralnetworks.core.binarycounter;

public class BinaryCounter {

    private int count = 0;
    private int n = 16;

    public BinaryCounter(int n) {
        this.n = n;
    }

    public String incCounter() {
        String bin = Integer.toBinaryString(count);

        while (bin.length() < n) {
            StringBuilder sb = new StringBuilder();
            sb.append("0");
            sb.append(bin);
            bin = sb.toString();
        }

        if(bin.length() > n )
            throw new RuntimeException("BinaryCounter - too many inc done.");

        count++;
        return bin;
    }

    public static void main(String[] args) {
        BinaryCounter binaryCounter = new BinaryCounter(16);
        for(int i=0; i<65536; i++) {
            System.out.println(binaryCounter.incCounter());
        }
    }

    public double[] stringToArray(String hex) {
        double input[] = new double[hex.length()];
        for(int i=0; i<hex.length(); i++) {
            input[i] = hex.charAt(i);
            if(input[i] == 48.0)
                input[i] = 0;
            else
                input[i] = 1;
        }
        return input;
    }

}
