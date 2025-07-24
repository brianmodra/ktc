package com.ktc.text;

public enum EnclosingPunctuationEnum {
  NONE(""),
  PARENTHESIS("()"),
  BRACKETS("[]"),
  BRACES("{}"),
  QUOTATION_MARK("\"");

  private final String punctuation;

  EnclosingPunctuationEnum(String punctuation) {
    this.punctuation = punctuation;
  }

  public String getPunctuation() {
    return punctuation;
  }

  public String getOpeningPunctuation() {
    if (punctuation.length() == 0) {
      return "";
    }
    return String.valueOf(punctuation.charAt(0));
  }

  public String getClosingPunctuation() {
    if (punctuation.length() < 2) {
      return "";
    }
    return String.valueOf(punctuation.charAt(1));
  }
}
