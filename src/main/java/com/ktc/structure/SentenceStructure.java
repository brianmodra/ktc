package com.ktc.structure;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import com.ktc.text.SentenceText;

public class SentenceStructure implements DocumentStructureInterface {
  Resource sentenceResource;

  public SentenceStructure(Model model, ParagraphStructure paragraph, SentenceText sentenceText, SentenceStructure previousSentence) {
    Resource prevResource = (previousSentence != null) ? previousSentence.getSentenceResource() : null;
    sentenceResource = DocumentStructureInterface.createSentence(model, paragraph.getParagraphResource(), sentenceText, prevResource);
  }

  public Resource getSentenceResource() {
    return sentenceResource;
  }
} 