package fr.automated.trading.systems.marketdatas.httpcalls;

public class HttpCallYahoo extends HttpCall {

    public HttpCallYahoo() {
        this.httpURL = "http://ichart.finance.yahoo.com/table.csv?s=";
        this.suffixURL = "";
    }

}
