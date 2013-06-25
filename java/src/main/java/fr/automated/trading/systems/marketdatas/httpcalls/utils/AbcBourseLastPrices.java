package fr.automated.trading.systems.marketdatas.httpcalls.utils;

import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbcBourseLastPrices {

    private final String source;

    public AbcBourseLastPrices(String AbcBourseSource) {
        this.source = AbcBourseSource;
    }

    public List<Double> getLastPrices() {
        try {
            List<Double> prices = new ArrayList<>();
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            prices.add(format.parse(getOpenLastPrice()).doubleValue());
            prices.add(format.parse(getHighLastPrice()).doubleValue());
            prices.add(format.parse(getLowLastPrice()).doubleValue());
            prices.add(format.parse(getCloseLastPrice()).doubleValue());
            return prices;
        }  catch (ParseException e) {
            AtsLogger.logException("AbcBourse stream read error", e);
            return null;
        }
    }

    private String getPriceRegex(String regex, int shift) {

        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(source);

        int index = -1;
        while (matcher.find()) {
            index = matcher.start() + shift;
        }

        while(!Character.isDigit(source.charAt(index))) {
            index++;
        }

        AtsLogger.log("digit at " + index + " indexed by " + source.charAt(index));

        StringBuilder sb = new StringBuilder();
        while(Character.isDigit(source.charAt(index)) || Character.toString(source.charAt(index)).equals(",")) {
            sb.append(source.charAt(index));
            index++;
        }

        return sb.toString();
    }


    private String getPrice(String identifier, int shift) {

        int index = source.indexOf(identifier) + shift;

        while(!Character.isDigit(source.charAt(index))) {
            index++;
        }

        AtsLogger.log("digit at " + index + " indexed by " + source.charAt(index));

        StringBuilder sb = new StringBuilder();
        while(Character.isDigit(source.charAt(index)) || Character.toString(source.charAt(index)).equals(",")) {
            sb.append(source.charAt(index));
            index++;
        }

        return sb.toString();
    }

    private String getPrice(String identifier) {
        return getPrice(identifier, 0);
    }

    public String getOpenLastPrice() {
        return getPrice("Ouverture");
    }

    public String getHighLastPrice() {
        return getPrice("Plus haut");
    }

    public String getLowLastPrice() {
        return getPrice("Plus bas");
    }

    public String getCloseLastPrice() {
        return getSpotLastPrice();
    }

    public String getSpotLastPrice() {
        //return getPriceRegex("<div id=\"vZone\" name=\".{0,}[0-9]{1,},[0-9]{1,}\"><b style=\"font-size:[0-9]{1,}px\">", 54);
        String regex = "<div id=\"vZone\" name=\".{0,}[0-9]{1,},[0-9]{1,}\"><b class=\"f20\">";
        return getPriceRegex(regex, 42);
    }

}
