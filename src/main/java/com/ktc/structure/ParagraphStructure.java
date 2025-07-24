package com.ktc.structure;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import com.ktc.text.ParagraphText;

public class ParagraphStructure implements DocumentStructureInterface {
  Resource paragraphResource;

  public ParagraphStructure(Model model, ChapterStructure chapter, ParagraphText paragraphText, ParagraphStructure previousParagraph) {
    Resource prevResource = (previousParagraph != null) ? previousParagraph.getParagraphResource() : null;
    paragraphResource = DocumentStructureInterface.createParagraph(model, chapter.getChapterResource(), paragraphText, prevResource);
  }

  public Resource getParagraphResource() {
    return paragraphResource;
  }
} 