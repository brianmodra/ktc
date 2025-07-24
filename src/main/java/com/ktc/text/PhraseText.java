package com.ktc.text;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class PhraseText extends DocumentBaseText {
  private ArrayList<WordText> words;
  private PunctuationEnum punctuation;
  private EnclosingPunctuationEnum enclosingPunctuation;
  private boolean isDialogue = false;

  public PhraseText() {
    super(); // Generates UUID in base class
    this.words = new ArrayList<WordText>();
    this.punctuation = PunctuationEnum.NONE;
    this.enclosingPunctuation = EnclosingPunctuationEnum.NONE;
  }

  public PhraseText(UUID id) {
    super(id);
    this.words = new ArrayList<WordText>();
    this.punctuation = PunctuationEnum.NONE;
    this.enclosingPunctuation = EnclosingPunctuationEnum.NONE;
  }

  public PhraseText(ArrayList<WordText> words, PunctuationEnum punctuation, EnclosingPunctuationEnum enclosingPunctuation) {
    super(); // Generates UUID in base class
    this.words = words;
    this.punctuation = punctuation;
    this.enclosingPunctuation = enclosingPunctuation;
    words.forEach(word -> word.setParent(this));
  }

  public PhraseText(ArrayList<WordText> words, UUID id, PunctuationEnum punctuation, EnclosingPunctuationEnum enclosingPunctuation) {
    super(id);
    this.words = words;
    this.punctuation = punctuation;
    this.enclosingPunctuation = enclosingPunctuation;
    words.forEach(word -> word.setParent(this));
  }

  public ArrayList<WordText> getWords() {
    return words;
  }

  public void setWords(ArrayList<WordText> words) {
    this.words = words;
    words.forEach(word -> word.setParent(this));
  }

  public void addWord(WordText word) {
    this.words.add(word);
    word.setParent(this);
  }

  public void addWord(int index, WordText word) {
    this.words.add(index, word);
    word.setParent(this);
  }

  public PunctuationEnum getPunctuation() {
    return punctuation;
  }

  public void setPunctuation(PunctuationEnum punctuation) {
    this.punctuation = punctuation;
  }

  public EnclosingPunctuationEnum getEnclosingPunctuation() {
    return enclosingPunctuation;
  }

  public void setEnclosingPunctuation(EnclosingPunctuationEnum enclosingPunctuation) {
    this.enclosingPunctuation = enclosingPunctuation;
  }

  public boolean isDialogue() {
    return isDialogue;
  }

  public void setDialogue(boolean isDialogue) {
    this.isDialogue = isDialogue;
    if (isDialogue) {
      this.enclosingPunctuation = EnclosingPunctuationEnum.QUOTATION_MARK;
    } else if (this.enclosingPunctuation == EnclosingPunctuationEnum.QUOTATION_MARK) {
      this.enclosingPunctuation = EnclosingPunctuationEnum.NONE;
    }
  }

  public String getWordsAsString() {
    String str = this.words.stream()
                      .map(WordText::getWordString)
                      .collect(Collectors.joining(" "));
    if (this.punctuation != PunctuationEnum.NONE) {
      str = str + this.punctuation.getPunctuation();
    }
    if (this.enclosingPunctuation != EnclosingPunctuationEnum.NONE) {
      str = this.enclosingPunctuation.getOpeningPunctuation() + str + this.enclosingPunctuation.getClosingPunctuation();
    }
    return str;
  }

  @Override
  public String toString() {
    return "PhraseText [id=" + getId() + ", punctuation=" + punctuation + ", enclosingPunctuation=" + enclosingPunctuation + ", isDialogue=" + isDialogue + "]";
  }
}
