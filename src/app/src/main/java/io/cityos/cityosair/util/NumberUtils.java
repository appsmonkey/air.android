package io.cityos.cityosair.util;

public interface NumberUtils {
  enum PatternEnum {
    NUMBER_OF_DECIMALS_0("0;-0"),
    NUMBER_OF_DECIMALS_1("0.0;-0.0"),
    NUMBER_OF_DECIMALS_2("0.00;-0.00"),
    NUMBER_OF_DECIMALS_3("0.000;-0.000"),
    NUMBER_OF_DECIMALS_4("0.0000;-0.0000"),
    NUMBER_OF_DECIMALS_5("0.00000;-0.0000");

    private final String pattern;

    PatternEnum(String pattern) {
      this.pattern = pattern;
    }

    @Override public String toString() {
      return pattern;
    }
  }

  String format(Object input, int numberOfDecimalPoints);
}
