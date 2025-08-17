package com.ktc.text;

import java.util.UUID;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public class SentenceText extends StructuredNode<SentenceText, ParagraphText, TokenText> {
  public SentenceText() {
    super(SENTENCE_TYPE);
  }

  public SentenceText(UUID sentenceId) {
    super(SENTENCE_TYPE, sentenceId);
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
  public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
    return parentClass == ParagraphText.class;
  }

  public final String getTokensAsString() {
    StringBuilder sb = new StringBuilder();
    TokenText token = firstChild();
    while (token != null) {
      sb.append(token.getTokenString());
      token = token.getNext();
    }
    return sb.toString();
  }

  public boolean removeNLP() {
    boolean removedSomething = false;
    for (TokenText text: getChildren()) {
      if (text.removeNLP()) {
        removedSomething = true;
      }
    }
    return removedSomething;
  }
}
