package com.ktc.text;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResourceFactory;

public class Link {
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
