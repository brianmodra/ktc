package com.ktc.text;

import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class ChapterText extends StructuredNode<ChapterText, DocumentText, ParagraphText> {

  public ChapterText() {
    super(CHAPTER_TYPE);
  }

  public ChapterText(UUID chapterId) {
    super(CHAPTER_TYPE, chapterId);
  }

  @Override
  public Property getNextProperty() {
    return Link.NEXT_CHAPTER;
  }

  @Override
  public Property getPreviousProperty() {
    return Link.PREVIOUS_CHAPTER;
  }

  @Override
  public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
    return parentClass == DocumentText.class;
  }

  public String getParagraphsAsString() {
    return getChildren().stream()
                        .map(child -> child.getSentencesAsString())
                          .collect(Collectors.joining("\n"));
  }
} 