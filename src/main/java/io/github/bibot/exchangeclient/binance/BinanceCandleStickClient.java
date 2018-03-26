package io.github.bibot.exchangeclient.binance;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import io.github.bibot.domain.candleStick.CandleStick;
import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.credentials.APICredentials;
import io.github.bibot.exchangeclient.CandleStickClient;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class BinanceCandleStickClient  extends AbstractBinanceClient implements CandleStickClient{

    private static final Logger LOG = Logger.getLogger(BinanceCandleStickClient.class);

    public BinanceCandleStickClient(APICredentials credentials) {
        super(credentials);
    }

    @Override
    public List<CandleStick> getCandleStickHistory(CurrencyPair pair) {
        List<Candlestick> candlesticks = client.getCandlestickBars( pair.toString(), CandlestickInterval.ONE_MINUTE);
        LinkedList<CandleStick> convertedCandleSticks = new LinkedList<>();
        for( Candlestick candlestick : candlesticks){
            convertedCandleSticks.add(convertBinanceCandlestickToCandleStick(candlestick));
        }
        return convertedCandleSticks;
    }

    @Override
    public CandleStick getLatestCandleStick(CurrencyPair pair) {
        List<Candlestick> candlestickList = client.getCandlestickBars(pair.toString(), CandlestickInterval.ONE_MINUTE, 1 , null, Calendar.getInstance().getTimeInMillis());
        if(candlestickList.size()==1) {
            Candlestick binanceCandlestick = candlestickList.get(0);
            return convertBinanceCandlestickToCandleStick(binanceCandlestick);
        }else{
            throw new RuntimeException("No Candlestick received");
        }
    }

    private CandleStick convertBinanceCandlestickToCandleStick(Candlestick candlestick){
        Date openTime = new Date(candlestick.getOpenTime());
        Date closeTime = new Date(candlestick.getCloseTime());
        BigDecimal open = BigDecimal.valueOf(Double.parseDouble(candlestick.getOpen()));
        BigDecimal close = BigDecimal.valueOf(Double.parseDouble(candlestick.getClose()));
        BigDecimal high = BigDecimal.valueOf(Double.parseDouble(candlestick.getHigh()));
        BigDecimal low = BigDecimal.valueOf(Double.parseDouble(candlestick.getLow()));
        BigDecimal volume = BigDecimal.valueOf(Double.parseDouble(candlestick.getVolume()));
        return new CandleStick(openTime, closeTime, open, low, high, close, volume);
    }
}
