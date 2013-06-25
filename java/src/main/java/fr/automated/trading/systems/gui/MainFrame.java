package fr.automated.trading.systems.gui;

import fr.automated.trading.systems.gui.console.AWTConsole;
import fr.automated.trading.systems.marketdatas.MarketData;
import fr.automated.trading.systems.marketdatas.symbols.MarketDataSymbols;
import fr.automated.trading.systems.marketdatas.symbols.YahooCac40Symbols;
import fr.automated.trading.systems.utils.utils.AtsLogger;
import fr.automated.trading.systems.utils.utils.Utils;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

//TODO rename all exceptions with AtsLogger.log(blabla)
public class MainFrame {

    public MainFrame() {

        String[] companies = new YahooCac40Symbols().getCompaniesNames();
        java.util.Arrays.sort(companies);
        ImageIcon noIcon = new ImageIcon();
        JOptionPane selectionPane = new JOptionPane(),
                confirmationPane = new JOptionPane();

        String company = (String) selectionPane.showInputDialog(
                null,
                Texts.EQUITY_SELECTION,
                Texts.EQUITY_SELECTION_WINDOW_TITLE,
                JOptionPane.QUESTION_MESSAGE,
                noIcon,
                companies,
                companies[0]);

        if(company != null) {
            MarketDataSymbols marketDataSymbols = new YahooCac40Symbols();
            SharedGuiCache.symbol = marketDataSymbols.getSymbol(company);
            confirmationPane.showMessageDialog(null, dispMarketDataConfirmation(company), Texts.MARKET_DATA_CONFIRMATION_WINDOW_TITLE, JOptionPane.INFORMATION_MESSAGE);

            ProcessingFrame processingFrame = new ProcessingFrame(Texts.MARKET_DATA_DOWNLOAD_IN_PROGRESS, 200, 25);
            processingFrame.setUndecorated(true);
            processingFrame.setAlwaysOnTop(true);
            processingFrame.showFrame();

            String marketDataFilename = "files/gui/" + SharedGuiCache.symbol + ".csv";
            MarketData marketData = new MarketData();
            marketData.getOnlyYahooMarketData(SharedGuiCache.symbol, marketDataFilename);
            String lastDateRetrieved = marketData.getLastDateRetrieved();

            try {
                Properties properties = new Properties();
                FileInputStream propertiesInputFileStream = new FileInputStream(MainGui.propertiesFilename);
                properties.load(propertiesInputFileStream);
                properties.setProperty("file.filename", marketDataFilename);
                properties.store(new FileOutputStream(SharedGuiCache.tmpPropertiesFilename), null);
            } catch (Exception ex) {
                AtsLogger.logException("", ex);
            }

            processingFrame.hideFrame();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            //TODO put it in market data
            if(Utils.todayIsBusinessDay()) {
                if(!lastDateRetrieved.equals(dateFormat.format(new Date()))) {
                    marketData.addLastPricesFromAbcBourseToFile(SharedGuiCache.symbol, marketDataFilename);
                }
            }

            new AWTConsole();
            runTradingSystem();


        } else {
            throw new RuntimeException();
        }
    }

    public String dispMarketDataConfirmation(String company) {
        StringBuilder sb = new StringBuilder();
        sb.append(Texts.MARKET_DATA_CONFIRMATION_1);
        sb.append(company);
        sb.append(Texts.MARKET_DATA_CONFIRMATION_2);
        return sb.toString();
    }

    public void runTradingSystem() {
        (new ProcessingThread()).doInBackground();
        StringBuilder result = new StringBuilder();
        result.append("<html>");
        try {
            BufferedReader reader = new BufferedReader(new FileReader("files/results.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("<P><P>");
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        result.append("</html>");

        ProcessingFrame outputFrame = new ProcessingFrame(result.toString());
        outputFrame.setAlwaysOnTop(true);
        outputFrame.showFrame();
    }


}
