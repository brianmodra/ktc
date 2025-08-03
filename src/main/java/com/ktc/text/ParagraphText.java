package com.ktc.text;

import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class ParagraphText extends StructuredNode<ParagraphText, ChapterText, SentenceText> {

  public ParagraphText() {
    super(PARAGRAPH_TYPE);
  }

  public ParagraphText(UUID paragraphId) {
    super(PARAGRAPH_TYPE, paragraphId);
  }

  @Override
  public Property getNextProperty() {
    return Link.NEXT_PARAGRAPH;
  }

  @Override
  public Property getPreviousProperty() {
    return Link.PREVIOUS_PARAGRAPH;
  }

  @Override
  public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
    return parentClass == ChapterText.class;
  }

  public String getSentencesAsString() {
    return getChildren().stream()
                      .map(child -> child.getTokensAsString())
                      .collect(Collectors.joining(" "));
  }
}

