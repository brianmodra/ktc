package com.ktc.text;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class DocumentText extends NodeBase<DocumentText> {
  public DocumentText() {
    super(DOCUMENT_TYPE);
  }

  public DocumentText(UUID documentId) {
    super(DOCUMENT_TYPE, documentId);
  }

  public List<ChapterText> getChapters() {
    return getChildNodes();
  }

  public void addChapter(ChapterText chapter) {
    addChildNode(chapter);
  }

  public void addChapter(int index, ChapterText chapter) {
    addChildNode(index, chapter);
  }

  public ChapterText getFirstChapter() {
    return (ChapterText) firstChildNode();
  }

  public ChapterText getLastChapter() {
    return (ChapterText) lastChildNode();
  }

  @Override
  public String getChildKey() {
    return "chapter";
  }

  @Override
  public String getKey() {
    return "document";
  }

  @Override
  public Property getNextProperty() {
    return null;
  }

  @Override
  public Property getPreviousProperty() {
    return null;
  }

  public String getChaptersAsString() {
    return getChapters().stream()
                        .map(ChapterText::toString)
                        .collect(Collectors.joining("\n"));
  }

  @Override
  public String toString() {
    return "DocumentText [id=" + getId() + "]";
  }
} 