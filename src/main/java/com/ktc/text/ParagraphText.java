package com.ktc.text;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class ParagraphText extends NodeBase<ParagraphText> {

  public ParagraphText() {
    super(PARAGRAPH_TYPE);
  }

  public ParagraphText(UUID paragraphId) {
    super(PARAGRAPH_TYPE, paragraphId);
  }

  public SentenceText getFirstSentence() {
    return (SentenceText) firstChildNode();
  }

  public SentenceText getLastSentence() {
    return (SentenceText) lastChildNode();
  }

  public ParagraphText getNext() {
    return (ParagraphText) getNextNode();
  }

  public ParagraphText getPrevious() {
    return (ParagraphText) getPreviousNode();
  }

  public void setNext(ParagraphText next) {
    setNextNode(next);
  }

  public void setPrevious(ParagraphText previous) {
    setPreviousNode(previous);
  }

  public List<SentenceText> getSentences() {
    return getChildNodes();
  }

  public void setParent(ChapterText parent) {
    setParentNode(parent);
  }

  public ChapterText getParent() {
    return (ChapterText) getParentNode();
  }

  public void addSentence(SentenceText sentence) {
    addChildNode(sentence);
  }

  public void addSentence(int index, SentenceText sentence) {
    addChildNode(index, sentence);
  }

  @Override
  public String getChildKey() {
    return "sentence";
  }

  @Override
  public String getKey() {
    return "paragraph";
  }

  @Override
  public Property getNextProperty() {
    return Link.NEXT_PARAGRAPH;
  }

  @Override
  public Property getPreviousProperty() {
    return Link.PREVIOUS_PARAGRAPH;
  }

  public String getSentencesAsString() {
    return getSentences().stream()
                      .map(SentenceText::getTokensAsString)
                      .collect(Collectors.joining(" "));
  }

  @Override
  public String toString() {
    return "ParagraphText [id=" + getId() + "]";
  }
}

