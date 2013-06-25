package fr.automated.trading.systems.marketdatas.httpcalls;

import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class HttpCall {

    protected String httpURL = "";
    protected String suffixURL = "";

    protected List<String> performHttpGetRequest(String request) throws IOException {
        boolean exception;
        List<String> list = null;
        do {
            exception = false;
            AtsLogger.log("Performing Http GET Request : " + request);
            BufferedReader reader = null;
            try {
                list = new ArrayList<>();
                URL url = new URL(request);
                URLConnection urlConnection = url.openConnection();

                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String text;
                int count = 0;

                while ((text = reader.readLine()) != null) {
                    list.add(text);
                    if (count % 100 == 0) {
                        AtsLogger.log(count + " lines read from request");
                    }
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                exception = true;
            } finally {
                reader.close();
            }
        } while (exception);

        return list;
    }

    protected String performHttpGetRequestString(String request) throws IOException {
        AtsLogger.log("Performing Http GET Request : " + request);
        StringBuilder sb = new StringBuilder();
        URL url = new URL(request);
        URLConnection urlConnection = url.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String text;
        while ((text = reader.readLine()) != null) {
            sb.append(text);
        }

        reader.close();
        return sb.toString();
    }

    public List<String> execute(String symbol) {
        String request = httpURL + symbol + suffixURL;
        try {
            return performHttpGetRequest(request);
        } catch (IOException e) {
            AtsLogger.logException("cannot make HTTP Call", e);
        }
        return null;
    }

    public String executeString(String symbol) {
        String request = httpURL + symbol + suffixURL;
        try {
            return performHttpGetRequestString(request);
        } catch (IOException e) {
            AtsLogger.logException("cannot make HTTP Call", e);
        }
        return null;
    }

}
