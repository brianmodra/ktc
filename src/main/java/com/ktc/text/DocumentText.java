package com.ktc.text;

import java.util.ArrayList;
import java.util.UUID;

public class DocumentText extends DocumentBaseText {
  private ArrayList<ChapterText> chapters;

  public DocumentText() {
    super(); // Generates UUID in base class
  }

  public DocumentText(ArrayList<ChapterText> chapters) {
    super(); // Generates UUID in base class
    this.setChapters(chapters);
  }

  public DocumentText(ArrayList<ChapterText> chapters, UUID documentId) {
    super(documentId); // Uses provided UUID
    this.setChapters(chapters);
  }

  public ArrayList<ChapterText> getChapters() {
    return chapters;
  }

  public void setChapters(ArrayList<ChapterText> chapters) {
    this.chapters = chapters;
    chapters.forEach(chapter -> chapter.setParent(this));
  }

  public void addChapter(ChapterText chapter) {
    this.chapters.add(chapter);
    chapter.setParent(this);
  }

  public void addChapter(int index, ChapterText chapter) {
    this.chapters.add(index, chapter);
    chapter.setParent(this);
  }

  @Override
  public String toString() {
    return "DocumentText [id=" + getId() + "]";
  }
} 