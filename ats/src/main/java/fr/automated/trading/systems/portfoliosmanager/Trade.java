package fr.automated.trading.systems.portfoliosmanager;

import fr.automated.trading.systems.utils.utils.AtsLogger;

public class Trade {
	
	private String ref;
	private double priceIn;
	private double priceOut;
	private double pricePredicted = 0;
	private int volume = 1;
	private PricesConstants position;
	private boolean isClosed = false;

	public Trade(String ref, double priceIn, double pricePredicted, int volume, PricesConstants position) {
		setRef(ref);
		setPriceIn(priceIn);
		setPricePredicted(pricePredicted);
		setVolume(volume);
        setPosition(position);
		setClosed(false);
	}
	
	public void setPosition(PricesConstants position) {
		this.position = position;
	}
	
	public PricesConstants getPosition() {
		return position;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public double getPriceIn() {
		return priceIn;
	}

	public void setPriceIn(double priceIn) {
		this.priceIn = priceIn;
	}

	public double getPriceOut() {
		return priceOut;
	}

	public void setPriceOut(double priceOut) {
		this.priceOut = priceOut;
		setClosed(true);
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public void setPricePredicted(double pricePredicted) {
		this.pricePredicted = pricePredicted;
	}
	
	public void debug() {
		AtsLogger.log("Trade " + ref + " position = " + getPosition() + " : priceIn = " + priceIn + " pricePredicted = " + pricePredicted + " priceOut = " + priceOut + " volume = " + volume);
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void setClosed(boolean isClosed) {
		this.isClosed = isClosed;
	}
}