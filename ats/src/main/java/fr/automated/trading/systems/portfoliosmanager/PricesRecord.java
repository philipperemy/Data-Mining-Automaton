package fr.automated.trading.systems.portfoliosmanager;

import fr.automated.trading.systems.utils.utils.AtsLogger;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class PricesRecord {

    private final List<Double> prices = new ArrayList<>();
	private final List<TradeRecords> observers = new ArrayList<>();
	
	public void addPrice(double price) {
		AtsLogger.log("Prices records updated");
		prices.add(price);
		notifyObservers();
	}
	
	public List<Double> getPrices() {
		return prices;
	}
	
	public double getPrice(int i) {
		return prices.get(i);
	}

	public double getLastPrice() {
		return prices.get(prices.size()-1);
	}
	
	public double getPreviousPrice() {
		return prices.get(prices.size()-2);
	}
	
	public void registerObserver(TradeRecords observer) {
		AtsLogger.log("Observer added");
		observers.add(observer);
	}
	
	public void notifyObservers() {
		AtsLogger.log("Notify Observers");
        for (TradeRecords observer : observers) {
            observer.update(getLastPrice());
        }
	}
	
	public void debug() {
	    for(double price : prices) {
            AtsLogger.log(price);
        }

    }
}
