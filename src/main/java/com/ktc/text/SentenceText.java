package com.ktc.text;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class SentenceText extends DocumentBaseText {
  private ArrayList<PhraseText> phrases;

  public SentenceText(ArrayList<PhraseText> phrases) {
    super(); // Generates UUID in base class
    this.setPhrases(phrases);
  }

  public SentenceText(ArrayList<PhraseText> phrases, UUID sentenceId) {
    super(sentenceId); // Uses provided UUID
    this.setPhrases(phrases);
  }

  public ArrayList<PhraseText> getPhrases() {
    return phrases;
  }

  public void setPhrases(ArrayList<PhraseText> phrases) {
    this.phrases = phrases;
    phrases.forEach(phrase -> phrase.setParent(this));
  }

  public void addPhrase(PhraseText phrase) {
    this.phrases.add(phrase);
    phrase.setParent(this);
  }

  public void addPhrase(int index, PhraseText phrase) {
    this.phrases.add(index, phrase);
    phrase.setParent(this);
  }

  public String getPhrasesAsString() {
    return this.phrases.stream()
                      .map(PhraseText::getWordsAsString)
                      .collect(Collectors.joining(" "));
  }

  @Override
  public String toString() {
    return "SentenceText [id=" + getId() + "]";
  }
}
