package io.github.bibot.exchangeclient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.github.bibot.domain.CurrencyPair;
import io.github.bibot.domain.price.Price;
import io.github.bibot.exchangeclient.simulator.SimulatedPriceIterator;

public class SimulatedPriceClient implements PriceClient {

	private Map<CurrencyPair, Iterator<Price>> currencySimulatorMap = new HashMap<>();

	public void addSimulator(CurrencyPair pair, Iterator<Price> simulator) {
		this.currencySimulatorMap.put(pair, simulator);
	}
	
	@Override
	public Price latestPrice(CurrencyPair pair) {

		if(!currencySimulatorMap.containsKey(pair)) {
			currencySimulatorMap.put(pair, SimulatedPriceIterator.createDefaultInstance());
		}
		
		return currencySimulatorMap.get(pair).next();
	}

}
