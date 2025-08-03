package com.ktc.text;

import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

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
    return getChildren().stream()
                      .map(child -> child.getTokenString())
                      .collect(Collectors.joining(" "));
  }
}
