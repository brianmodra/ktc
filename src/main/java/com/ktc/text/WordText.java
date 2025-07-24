package com.ktc.text;

import java.util.UUID;

public class WordText extends DocumentBaseText {
  private final String wordString;

  public WordText(String word) {
    super(); // Generates UUID in base class
    this.wordString = word;
  }

  public WordText(String word, UUID wordId) {
    super(wordId); // Uses provided UUID
    this.wordString = word;
  }

  public String getWordString() {
    return wordString;
  }

  @Override
  public String toString() {
    return wordString;
  }
}