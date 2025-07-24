package com.ktc.structure;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import com.ktc.text.PhraseText;
import com.ktc.text.EnclosingPunctuationEnum;
import com.ktc.text.PunctuationEnum;
import com.ktc.text.WordText;

public class StringStructure implements DocumentStructureInterface {
  Resource stringResource;
  PhraseText phraseText;
  Model model;

  public StringStructure(Model model, SentenceStructure sentence, PhraseText phraseText, StringStructure previousString) {
    Resource prevResource = (previousString != null) ? previousString.getStringResource() : null;
    stringResource = DocumentStructureInterface.createString(model, sentence.getSentenceResource(), phraseText, prevResource);
    this.phraseText = phraseText;
    this.model = model;
  }

  public void addOpeningPunctuation() {
    if (phraseText.getEnclosingPunctuation() != EnclosingPunctuationEnum.NONE) {
      DocumentStructureInterface.addOpeningPunctuationToSentence(model, stringResource, phraseText.getId(), phraseText.getEnclosingPunctuation());
    }
  }

  public void addWords() {
    WordStructure previousWord = null;
    for (WordText word : phraseText.getWords()) {
      WordStructure wordStructure = new WordStructure(model, this, word, previousWord);
      previousWord = wordStructure;
    }
  }

  public void addClosingPunctuation() {
    if (phraseText.getPunctuation() != PunctuationEnum.NONE) {
      DocumentStructureInterface.addPunctuationToSentence(model, stringResource, phraseText.getId(), phraseText.getPunctuation());
    }
    if (phraseText.getEnclosingPunctuation() != EnclosingPunctuationEnum.NONE) {
      DocumentStructureInterface.addClosingPunctuationToSentence(model, stringResource, phraseText.getId(), phraseText.getEnclosingPunctuation());
    }
  }

  public Resource getStringResource() {
    return stringResource;
  }
} 