package fr.automated.trading.systems.utils.utils;

import fr.automated.trading.systems.exception.PropertiesException;
import fr.automated.trading.systems.tradingrobot.robot.TradingRobotType;

import java.io.*;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Utils {

    public static void loadProperties(String propertiesFilename) throws IOException {

        FileInputStream propertiesInputFileStream = null;
        try {
            Properties properties = new Properties();
            propertiesInputFileStream = new FileInputStream(propertiesFilename);
            properties.load(propertiesInputFileStream);

            SharedMemory.filename = properties.getProperty("file.filename");
            SharedMemory.inputCount = Integer.parseInt(properties.getProperty("neural.core.input-neurons-count"));
            SharedMemory.hiddenCount = Integer.parseInt(properties.getProperty("neural.core.hidden-neurons-count"));
            SharedMemory.momentum = Double.parseDouble(properties.getProperty("neural.core.momentum"));
            SharedMemory.learningRate = Double.parseDouble(properties.getProperty("neural.core.learning-rate"));
            SharedMemory.epochs = Integer.parseInt(properties.getProperty("neural.execution.max-epochs"));
            SharedMemory.percentage = Integer.parseInt(properties.getProperty("file.delimiter-training-testing-percentage"));
            SharedMemory.tradingRobotType = Utils.convertStringToTradingRobotType(properties.getProperty("trading.robot.type"));
            SharedMemory.neuralNetworkExecutionMode = Utils.convertStringToNeuralNetworkMode(properties.getProperty("neural.execution.mode"));

            SharedMemory.check();
            SharedMemory.debug();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(propertiesInputFileStream != null) {
                propertiesInputFileStream.close();
            }
        }
    }

    public static void displayList(List<Double> list) {
        for(Double value : list) {
            System.out.print(value + Constants.SEPARATOR);
        }
        System.out.println("");
    }

    public String listToString(List<Double> list) {
        StringBuilder sb = new StringBuilder();
        for(Double number : list) {
            sb.append(String.valueOf(number));
        }
        return sb.toString();
    }

    public static void displayArray(double[] array) {
        for (double anArray : array) {
            AtsLogger.log(anArray + Constants.SEPARATOR);
        }
    }

    public static double max(List<Double> values) {
        if(values != null) {
            double max = values.get(0);
            for(double value : values) {
                if(value>max)
                    max = value;
            }
            return max;
        }  else {
            throw new RuntimeException("values are null");
        }

    }

    public static double maxDoubleList(ArrayList<ArrayList<Double>> values) {
        if(values!=null) {
            double max = values.get(0).get(0);
            for(List<Double> value : values) {
                if(max(value)>max)
                    max = max(value);
            }
            return max;
        } else {
            throw new RuntimeException("values are null");
        }

    }

    public static void writeDoubleList(List<List<Double>> values, String filename) {
        try {
            int countLines = values.size();
            int countColumns = values.get(0).size();

            PrintWriter writer =  new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            for(int x = 0; x < countLines; x++) {
                for(int y = 0; y < countColumns; y++) {
                    if(y == countColumns-1)
                        writer.print(values.get(x).get(y));
                    else
                        writer.print(values.get(x).get(y) + Constants.SEPARATOR);
                }

                if(x != countLines-1)
                    writer.println("");
            }
            writer.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static TradingRobotType convertStringToTradingRobotType(String string) throws PropertiesException {
        TradingRobotType[] tradingRobotTypes = TradingRobotType.values();
        for(TradingRobotType tradingRobotType: tradingRobotTypes) {
            if(string.equals(tradingRobotType.name()))
                return tradingRobotType;
        }
        throw new PropertiesException("error in trading robot type conversion");
    }

    public static NeuralExecutionMode convertStringToNeuralNetworkMode(String string) throws PropertiesException {
        NeuralExecutionMode[] neuralExecutionModes = NeuralExecutionMode.values();
        for(NeuralExecutionMode neuralExecutionMode: neuralExecutionModes) {
            if(string.equals(neuralExecutionMode.name()))
                return neuralExecutionMode;
        }
        throw new PropertiesException("error in neural network mode conversion");
    }

    public static List<Double> strtokList(String result) {
        List<Double> results = new ArrayList<>();
        StringTokenizer stringtokenizer = new StringTokenizer(result, Constants.SEPARATOR);

        while (stringtokenizer.hasMoreTokens()) {
            results.add(Double.parseDouble(stringtokenizer.nextToken()));
        }

        return results;
    }

    public static double[] strtokArray(String result) {

        List<Double> resultsList = strtokList(result);
        double[] results = new double[resultsList.size()];
        for(int i=0; i<resultsList.size(); i++)
            results[i] = resultsList.get(i);

        return results;
    }

    public static double[][] multiListToArray(List<List<Double>> list) {
        int maxX = list.size();
        int maxY = list.get(0).size();
        double[][] array = new double[maxX][maxY];
        for(int x=0; x<maxX; x++) {
            for(int y=0; y<maxY; y++) {
                array[x][y] = list.get(x).get(y);
            }
        }
        return array;
    }

    public static int compareDates(String date1, String date2) {
        if(date1.equals(date2)) {
            return 0;
        }
        else {
            String[] date1Tokens = date1.split("-");
            String[] date2Tokens = date2.split("-");

            //year
            if(date1Tokens[0].equals(date2Tokens[0])) {

                if(date1Tokens[1].equals(date2Tokens[1])) {

                    if(date1Tokens[2].equals(date2Tokens[2])) {
                        return 0;
                    }
                    else {
                        double day1 = Double.valueOf(date1Tokens[2]);
                        double day2 = Double.valueOf(date2Tokens[2]);

                        if(day1 < day2)
                            return 2;
                        else
                            return 1;
                    }

                } else {
                    double month1 = Double.valueOf(date1Tokens[1]);
                    double month2 = Double.valueOf(date2Tokens[1]);

                    if(month1 < month2)
                        return 2;
                    else
                        return 1;
                }

            } else {
                double year1 = Double.valueOf(date1Tokens[0]);
                double year2 = Double.valueOf(date2Tokens[0]);

                if(year1 < year2)
                    return 2;
                else
                    return 1;
            }

        }
    }

    public static String getDayName(final java.util.Date date, final java.util.Locale locale) {
        SimpleDateFormat df=new SimpleDateFormat("EEEE", locale);
        return df.format(date);
    }

    //TODO careful for holidays
    public static boolean todayIsBusinessDay() {
        List<String> daysOff = new ArrayList<>();
        daysOff.add("samedi"); daysOff.add("dimanche");
        String today = getDayName(new Date(), Locale.FRENCH).toLowerCase().trim();
        return !daysOff.contains(today);
    }

    public static double[] doubleListToArray(List<Double> list) {
        double[] array = new double[list.size()];
        int i = 0;
        for(double element: list) {
            array[i] = element;
            i++;
        }
        return array;
    }

    public static String getMethodName() {
        // level 0 : getMethodName, level 1 : parent call method name
        return new Exception().getStackTrace()[1].getMethodName();
    }

    public static List<Double> reverseList(List<Double> list) {
        List<Double> reversedList = new ArrayList<>();
        for(int i=0; i<list.size(); i++) {
            reversedList.add(list.get(list.size()-1-i));
        }
        return reversedList;
    }

    public static void copyfile(String srFile, String dtFile){
        try{
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            AtsLogger.log("File copied.");
        }
        catch(FileNotFoundException ex){
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        }
        catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static String md5sum(String filename) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA1");
            FileInputStream fis = new FileInputStream(filename);
            byte[] dataBytes = new byte[1024];

            int nread;

            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            byte[] mdbytes = md.digest();

            //convert the byte to hex format
            StringBuilder sb = new StringBuilder("");
            for (byte mdbyte : mdbytes) {
                sb.append(Integer.toString((mdbyte & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void delete(String filename) {
        File f = new File(filename);
        if(f.exists())
            if(f.canWrite())
                f.delete();
    }

    public static double truncate(double value, int places) {
        double multiplier = Math.pow(10, places);
        return Math.floor(multiplier * value) / multiplier;
    }
}
