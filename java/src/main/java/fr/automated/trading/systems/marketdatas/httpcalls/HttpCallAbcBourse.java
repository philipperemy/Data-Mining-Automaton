package fr.automated.trading.systems.marketdatas.httpcalls;

public class HttpCallAbcBourse extends HttpCall {

    public HttpCallAbcBourse() {
        this.httpURL = "http://www.abcbourse.com/graphes/display.aspx?s=";
        this.suffixURL = "p";
    }
}
