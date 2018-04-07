package io.altanalytics.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalUtil {

	private static int SUPPORTED_SCALE = 10;
	
	public static BigDecimal multiply(BigDecimal value, BigDecimal multiplicand) {
		return value.multiply(multiplicand, MathContext.DECIMAL64);
	}

	public static BigDecimal divide(BigDecimal value, BigDecimal divisor) {
		return value.divide(divisor, MathContext.DECIMAL64);
	}

	public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
		return value1.add(value2, MathContext.DECIMAL64);
	}

	public static BigDecimal subtract(BigDecimal value1, BigDecimal value2) {
		return value1.subtract(value2, MathContext.DECIMAL64);
	}
	
	public static BigDecimal subtractToZero(BigDecimal value1, BigDecimal value2) {
		BigDecimal result = value1.subtract(value2, MathContext.DECIMAL64);
		if(result.doubleValue() < 0) {
			return BigDecimal.ZERO;
		}
		return result;
	}
	
	public static boolean greaterThan(BigDecimal value1, BigDecimal value2) {
		return value1.doubleValue() > value2.doubleValue();
	}
	
	public static boolean lessThan(BigDecimal value1, BigDecimal value2) {
		return value1.doubleValue() < value2.doubleValue();
	}

	public static double toDouble(BigDecimal value) {
		return value.setScale(SUPPORTED_SCALE, RoundingMode.HALF_UP).doubleValue();
	}

}
