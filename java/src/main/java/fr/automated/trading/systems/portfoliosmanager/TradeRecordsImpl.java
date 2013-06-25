package fr.automated.trading.systems.portfoliosmanager;

import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.util.ArrayList;
import java.util.List;

public class TradeRecordsImpl implements TradeRecords {

	private final List<Trade> trades = new ArrayList<>();

    private boolean lastTradeIsClosed = true;

	public void saveTrade(String ref, double priceIn, double pricePredicted, int volume, PricesConstants position) {
        if(lastTradeIsClosed()) {
            Trade trade = new Trade(ref, priceIn, pricePredicted, volume, position);
            trades.add(trade);
            bookNewTrade();
            AtsLogger.log("trade recorded : [ " + ref + ", " + priceIn + ", " + pricePredicted + ", " + volume + ", " + position.toString() + " ]");
        } else {
            throw new RuntimeException("Could not book trade. Last trade has not been closed !");
        }
    }

    public List<Trade> getTrades() {
        return trades;
    }

    private boolean lastTradeIsClosed() {
        return lastTradeIsClosed;
    }

    private void closeLastTrade() {
        lastTradeIsClosed = true;
    }

    private void bookNewTrade() {
        lastTradeIsClosed = false;
    }

	public void update(double price) {
		updateLastTrade(price);
	}

	public void updateLastTrade(double priceOut) {
		if(trades.size() >= 1) {
			Trade trade = trades.get(trades.size()-1);
			if(!trade.isClosed()) {
                trade.setPriceOut(priceOut);
                closeLastTrade();
            }
		}
	}
	
	public void debug() {
        for (Trade trade : trades) trade.debug();
	}
}