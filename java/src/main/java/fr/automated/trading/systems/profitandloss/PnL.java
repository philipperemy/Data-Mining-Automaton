package fr.automated.trading.systems.profitandloss;

import fr.automated.trading.systems.portfoliosmanager.PricesConstants;
import fr.automated.trading.systems.portfoliosmanager.Trade;

import java.util.ArrayList;
import java.util.List;

public class PnL {

    private final List<Double> PnLHistory = new ArrayList<>();

    public List<Double> getPnLHistory() {
        return PnLHistory;
    }

    public double calculatePnL(List<Trade> trades) {
        double PnL = 0.0;
        for (Trade trade : trades) {
            if (trade.isClosed()) {
                if (trade.getPosition().equals(PricesConstants.BUY))
                    PnL += (trade.getPriceOut() - trade.getPriceIn()) * trade.getVolume();
                else
                    PnL += (trade.getPriceIn() - trade.getPriceOut()) * trade.getVolume();
            }
        }

        PnLHistory.add(PnL);
        return PnL;
    }

}
