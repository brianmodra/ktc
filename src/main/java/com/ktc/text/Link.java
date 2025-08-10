package com.ktc.text;

import java.lang.ref.WeakReference;

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

  // SEM-based properties for semantic relationships
  public static final Property DENOTES = ResourceFactory.createProperty("http://www.ontologydesignpatterns.org/ont/semantics.owl#denotes");

  private final Property property;
  private final WeakReference<NodeBase> source;
  private final NodeBase target;
  private final String key;
  private WeakReference<Link> reverseLink = null;

  public Link(Property property, NodeBase source, NodeBase target, String key) {
    this.property = property;
    this.source = new WeakReference<>(source);
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
    return source.get();
  }

  public NodeBase getTarget() {
    return target;
  }

  public void setReverseLink(Link reverseLink) {
    this.reverseLink = new WeakReference<>(reverseLink);
  }

  public Link getReverseLink() {
    return reverseLink.get();
  }

  public void removeReverseLink() {
    reverseLink.clear();
  }
}
