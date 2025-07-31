package com.ktc.text;

import java.util.UUID;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

public class Link {
  public static String getDocumentUri(UUID documentId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#document_" + documentId.toString();
  }
  
  public static String getChapterUri(UUID chapterId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#chapter_" + chapterId.toString();
  }
  
  public static String getParagraphUri(UUID paragraphId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#paragraph_" + paragraphId.toString();
  }
  
  public static String getSentenceUri(UUID sentenceId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#sentence_" + sentenceId.toString();
  }
  
  public static String getStringUri(UUID stringId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#string_" + stringId.toString();
  }
  
  public static String getWordUri(UUID wordId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#word_" + wordId.toString();
  }

  public static String getTokenUri(UUID sentenceId) {
    return "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#token_" + sentenceId.toString();
  }

  public static String getSubjectUri(String subjectName) {
   return "http://www.w3.org/1999/02/22-rdf-syntax-ns#subject_" + subjectName.replace(" ", "_") + "_" + UUID.randomUUID().toString();
  }

  public static String getPredicateUri(String predicateName) {
    return "http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate_" + predicateName.replace(" ", "_") + "_" + UUID.randomUUID().toString();
  }

  public static String getObjectUri(String objectName) {
    return "http://www.w3.org/1999/02/22-rdf-syntax-ns#object_" + objectName.replace(" ", "_") + "_" + UUID.randomUUID().toString();
  }

  // Standard RDF Properties for document structure 
  // Using Dublin Core Terms for containment relationships
  public static final Property HAS_PART = ResourceFactory.createProperty("http://purl.org/dc/terms/hasPart");
  public static final Property IS_PART_OF = ResourceFactory.createProperty("http://purl.org/dc/terms/isPartOf");
  
  // NIF-based properties for text sequencing and ordering
  public static final Property NEXT_CHAPTER = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextChapter");
  public static final Property NEXT_PARAGRAPH = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextParagraph");
  public static final Property NEXT_SENTENCE = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextSentence");
  public static final Property NEXT_WORD = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextWord");
  public static final Property PREVIOUS_CHAPTER = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousChapter");
  public static final Property PREVIOUS_PARAGRAPH = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousParagraph");
  public static final Property PREVIOUS_SENTENCE = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousSentence");
  public static final Property PREVIOUS_WORD = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#previousWord");
  public static final Property ANCHOR_OF = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#anchorOf");
  
  // KTC-based properties for dramatic structure sequencing
  public static final Property NEXT_STRING = ResourceFactory.createProperty("http://ktc.com/nextString");
  public static final Property PREVIOUS_STRING = ResourceFactory.createProperty("http://ktc.com/previousString");
  
  private final Property property;
  private final NodeBase source;
  private final NodeBase target;
  private final String key;

  public Link(Property property, NodeBase source, NodeBase target, String key) {
    this.property = property;  
    this.source = source;
    this.target = target;
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public Property getProperty() {
    return property;
  }

  public String getNameSpace() {
    return property.getNameSpace();
  }

  public String getLocalName() {
    return property.getLocalName();
  }

  public String getURI() {
    return property.getURI();
  }

  public NodeBase getSource() {
    return source;
  }

  public NodeBase getTarget() {
    return target;
  }
  
}
