package com.ktc.text;

public enum PunctuationEnum {
  NONE(""),
  
  // ending punctuation
  PERIOD("."),
  QUESTION_MARK("?"),
  EXCLAMATION_MARK("!"),

  // delimiter punctuation
  COMMA(","),
  COLON(":"),
  SEMICOLON(";"),
  DASH("-"),
  SLASH("/"),
  BACKSLASH("\\"),
  ELLIPSIS("...");

  private final String punctuation;

  PunctuationEnum(String punctuation) {
    this.punctuation = punctuation;
  }

  public String getPunctuation() {
    return punctuation;
  }
}
