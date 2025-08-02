package com.ktc.text;

import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class DocumentText extends StructuredNode<DocumentText, DocumentText, ChapterText> {
  public DocumentText() {
    super(DOCUMENT_TYPE);
  }

  public DocumentText(UUID documentId) {
    super(DOCUMENT_TYPE, documentId);
  }

  @Override
  public Property getNextProperty() {
    return null;
  }

  @Override
  public Property getPreviousProperty() {
    return null;
  }

  @Override
  public String getParentKey() {
    return null;
  }

  @Override
  public String getChildKey() {
    return "chapter";
  }

  public String getChaptersAsString() {
    return getChildren().stream()
                        .map(child -> child.getParagraphsAsString())
                        .collect(Collectors.joining("\n"));
  }
} 