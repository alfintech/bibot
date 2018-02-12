package io.github.bibot.strategy;

import io.github.bibot.domain.BotState;
import io.github.bibot.domain.price.Price;

public abstract class Strategy {
	
	public enum Signal{BUY, SELL, HOLD};
	
	public Signal tick(Price price, BotState state) {

		if(!state.isOpenPosition() && isBuy(price)) {
			return Signal.BUY;
		} else if(state.isOpenPosition() && isSell(price)) {
			return Signal.SELL;
		} else {
			return Signal.HOLD;
		}
	}
	
	public abstract boolean isBuy(Price price);
	
	public abstract boolean isSell(Price price);

}