package com.ktc.structure;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import com.ktc.text.ChapterText;

public class ChapterStructure implements DocumentStructureInterface {
  Resource chapterResource;

  public ChapterStructure(Model model, DocumentStructure document, ChapterText chapterText, ChapterStructure previousChapter) {
    Resource prevResource = (previousChapter != null) ? previousChapter.getChapterResource() : null;
    chapterResource = DocumentStructureInterface.createChapter(model, document.getDocumentResource(), chapterText, prevResource);
  }

  public Resource getChapterResource() {
    return chapterResource;
  }
}
