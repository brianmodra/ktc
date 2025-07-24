package com.ktc.text;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.UUID;

public class ParagraphText extends DocumentBaseText {
  private ArrayList<SentenceText> sentences;

  public ParagraphText(ArrayList<SentenceText> sentences) {
    super(); // Generates UUID in base class
    this.setSentences(sentences);
  }

  public ParagraphText(ArrayList<SentenceText> sentences, UUID paragraphId) {
    super(paragraphId); // Uses provided UUID
    this.setSentences(sentences);
  }

  public ArrayList<SentenceText> getSentences() {
    return sentences;
  }

  public void setSentences(ArrayList<SentenceText> sentences) {
    this.sentences = sentences;
    sentences.forEach(sentence -> sentence.setParent(this));
  }

  public void addSentence(SentenceText sentence) {
    this.sentences.add(sentence);
    sentence.setParent(this);
  }

  public void addSentence(int index, SentenceText sentence) {
    this.sentences.add(index, sentence);
    sentence.setParent(this);
  }

  public String getSentencesAsString() {
    return this.sentences.stream()
                      .map(SentenceText::getPhrasesAsString)
                      .collect(Collectors.joining(" "));
  }

  @Override
  public String toString() {
    return "ParagraphText [id=" + getId() + "]";
  }
}

