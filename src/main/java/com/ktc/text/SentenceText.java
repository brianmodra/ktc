package com.ktc.text;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class SentenceText extends NodeBase<SentenceText> {
  public SentenceText() {
    super(SENTENCE_TYPE);
  }

  public SentenceText(UUID sentenceId) {
    super(SENTENCE_TYPE, sentenceId);
  }

  public TokenText lastToken() {
    return (TokenText) lastChildNode();
  }

  public TokenText firstToken() {
    return (TokenText) firstChildNode();
  }

  public List<TokenText> getTokens() {
    return getChildNodes();
  }

  public void addToken(TokenText token) {
    addChildNode(token);
  }

  public void addToken(int index, TokenText token) {
    addChildNode(index, token);
  }

  public void setParent(ChapterText parent) {
    setParentNode(parent);
  }

  public ChapterText getParent() {
    return (ChapterText) getParentNode();
  }

  public void setNext(SentenceText next) {
    setNextNode(next);
  }

  public void setPrevious(SentenceText previous) {
    setPreviousNode(previous);
  }

  public SentenceText getNext() {
    return (SentenceText) getNextNode();
  }

  public SentenceText getPrevious() {
    return (SentenceText) getPreviousNode();
  }

  @Override
  public Property getNextProperty() {
    return Link.NEXT_SENTENCE;
  }

  @Override
  public Property getPreviousProperty() {
    return Link.PREVIOUS_SENTENCE;
  }

  @Override
  public String getKey() {
    return "sentence";
  }

  @Override
  public String getChildKey() {
    return "token";
  }

  public final String getTokensAsString() {
    return getTokens().stream()
                      .map(TokenText::getTokenString)
                      .collect(Collectors.joining(" "));
  }

  @Override
  public String toString() {
    return "SentenceText [id=" + getId() + "]";
  }
}
