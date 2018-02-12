package io.github.bibot.backtest;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import io.github.bibot.bot.TraderBot;
import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.ExecutedTrade;
import io.github.bibot.domain.ExecutedTrade.Direction;
import io.github.bibot.domain.StrategyFund;
import io.github.bibot.exchangeclient.SimpleOrderClient;
import io.github.bibot.exchangeclient.SimulatedPriceClient;
import io.github.bibot.priceanalysis.BigDecimalOperations;
import io.github.bibot.strategy.Momentum;
import io.github.bibot.strategy.Strategy;

public class StrategyBackTestHarness {

	@Test
	public void backtest() {

		BigDecimal buyPercentageTrigger = new BigDecimal(0.9);
		BigDecimal sellPercentageTrigger = new BigDecimal(-0.5);
		int timeWindowOfAnalysis = 1000;
		int ticks = 10000;
		BigDecimal startingFund = new BigDecimal(1);

		Strategy momentum = new Momentum(buyPercentageTrigger, sellPercentageTrigger, timeWindowOfAnalysis);
		StrategyFund fund = new StrategyFund(startingFund);
		CurrencyPair pair = new CurrencyPair("CCY1", "CCY2");
		
		TraderBot bot = new TraderBot(momentum, fund, pair, new SimulatedPriceClient(), new SimpleOrderClient());
		
		for(int i=0; i<ticks; i++) {
			bot.tick();
		}
		
		reportProfit(startingFund, bot);
	}



	private void reportProfit(BigDecimal startingFund, TraderBot bot) {
		
		if(bot.getTrades()==null || getLatestSell(bot.getTrades()) == null) {
			System.out.println("No trades were executed");
		} else {
			ExecutedTrade latestSell = getLatestSell(bot.getTrades());
			BigDecimal closingFund = BigDecimalOperations.multiply(latestSell.price, latestSell.quantity);
			
			System.out.println("--------------------------------TRADES----------------------------------------");
			for(ExecutedTrade trade : bot.getTrades()) {
				System.out.println(trade);
			}
			System.out.println("--------------------------------SUMMARY---------------------------------------");
			System.out.println("Starting Fund: " +startingFund);
			System.out.println("Closing Fund: " +closingFund);
			System.out.println("profit: " +BigDecimalOperations.subtract(closingFund, startingFund));
			System.out.println("------------------------------------------------------------------------------");
		}
	}

	
	
	private ExecutedTrade getLatestSell(List<ExecutedTrade> trades) {

		ExecutedTrade latestSell = null;
		
		for(ExecutedTrade trade : trades) {
			if(trade.direction == Direction.SELL) {
				latestSell = trade;
			}
		}
		
		return latestSell;
	}
	
}
