package com.ktc.text;

import java.util.ArrayList;
import java.util.UUID;

public class ChapterText extends DocumentBaseText {
  private ArrayList<ParagraphText> paragraphs;

  public ChapterText(ArrayList<ParagraphText> paragraphs) {
    super(); // Generates UUID in base class
    this.setParagraphs(paragraphs);
  }

  public ChapterText(ArrayList<ParagraphText> paragraphs, UUID chapterId) {
    super(chapterId); // Uses provided UUID
    this.setParagraphs(paragraphs);
  }

  public ArrayList<ParagraphText> getParagraphs() {
    return paragraphs;
  }

  public void setParagraphs(ArrayList<ParagraphText> paragraphs) {
    this.paragraphs = paragraphs;
    paragraphs.forEach(paragraph -> paragraph.setParent(this));
  }

  public void addParagraph(ParagraphText paragraph) {
    this.paragraphs.add(paragraph);
    paragraph.setParent(this);
  }

  public void addParagraph(int index, ParagraphText paragraph) {
    this.paragraphs.add(index, paragraph);
    paragraph.setParent(this);
  }

  @Override
  public String toString() {
    return "ChapterText [id=" + getId() + "]";
  }
} 