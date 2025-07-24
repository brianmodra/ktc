package com.ktc.structure;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import com.ktc.text.WordText;

public class WordStructure implements DocumentStructureInterface {
  Resource wordResource;

  public WordStructure(Model model, StringStructure string, WordText wordText, WordStructure previousWord) {
    Resource prevResource = (previousWord != null) ? previousWord.getWordResource() : null;
    wordResource = DocumentStructureInterface.createWord(model, string.getStringResource(), wordText, prevResource);
  }

  public Resource getWordResource() {
    return wordResource;
  }
} 