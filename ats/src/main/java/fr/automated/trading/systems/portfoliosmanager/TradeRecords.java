package fr.automated.trading.systems.portfoliosmanager;

import java.util.List;

public interface TradeRecords {

	void saveTrade(String ref, double priceIn, double pricePredicted, int count, PricesConstants position);
	
	void updateLastTrade(double priceOut);

	void update(double price);

    List<Trade> getTrades();

}
