package io.cityos.cityosair.util;

import java.text.DecimalFormat;

public class NumberUtilsImpl implements NumberUtils {

  private DecimalFormat format = new DecimalFormat();

  public String format(Object input, int numberOfDecimalPoints) {
    return format(input, getPatternStringWithVariousNumberOfDecimalPoints(numberOfDecimalPoints));
  }

  private synchronized String format(Object input, String pattern) {
    this.format.applyPattern(pattern);
    return this.format.format(input);
  }

  private String getPatternStringWithVariousNumberOfDecimalPoints(int numberOfDecimalPoints) {
    if (numberOfDecimalPoints >= 0 && numberOfDecimalPoints <= 5) {
      PatternEnum patternEnum = PatternEnum.valueOf("NUMBER_OF_DECIMALS_" + numberOfDecimalPoints);
      return patternEnum.toString();
    }
    //In other case generate pattern
    StringBuilder pattern = new StringBuilder("0");

    for (int i = 0; i < numberOfDecimalPoints; i++) {
      if (i == 0) pattern.append('.');

      pattern.append('0');
    }

    String positivePart = pattern.toString();
    pattern.append(";-");
    pattern.append(positivePart);
    return pattern.toString();
  }
}
