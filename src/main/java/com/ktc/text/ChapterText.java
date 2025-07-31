package com.ktc.text;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class ChapterText extends NodeBase<ChapterText> {

  public ChapterText() {
    super(CHAPTER_TYPE);
  }

  public ChapterText(UUID chapterId) {
    super(CHAPTER_TYPE, chapterId);
  }

  public List<ParagraphText> getParagraphs() {
    return getChildNodes();
  }

  public ParagraphText getFirstParagraph() {
    return (ParagraphText) firstChildNode();
  }

  public ParagraphText getLastParagraph() {
    return (ParagraphText) lastChildNode();
  }

  public void addParagraph(ParagraphText paragraph) {
    addChildNode(paragraph);
  }

  public void addParagraph(int index, ParagraphText paragraph) {
    addChildNode(index, paragraph);
  }

  public void setParent(DocumentText parent) {
    setParentNode(parent);
  }

  public DocumentText getParent() {
    return (DocumentText) getParentNode();
  }

  public ChapterText getNext() {
    return (ChapterText) getNextNode();
  }

  public ChapterText getPrevious() {
    return (ChapterText) getPreviousNode();
  }

  public void setNext(ChapterText next) {
    setNextNode(next);
  }

  public void setPrevious(ChapterText previous) {
    setPreviousNode(previous);
  }

  @Override
  public String getChildKey() {
    return "paragraph";
  }

  @Override
  public String getKey() {
    return "chapter";
  }

  @Override
  public Property getNextProperty() {
    return Link.NEXT_CHAPTER;
  }

  @Override
  public Property getPreviousProperty() {
    return Link.PREVIOUS_CHAPTER;
  }

  public String getParagraphsAsString() {
    return getParagraphs().stream()
                          .map(ParagraphText::toString)
                          .collect(Collectors.joining("\n"));
  }

  @Override
  public String toString() {
    return "ChapterText [id=" + getId() + "]";
  }
} 