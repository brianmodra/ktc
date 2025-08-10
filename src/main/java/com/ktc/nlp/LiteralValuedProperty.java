package com.ktc.nlp;

import com.ktc.text.NodeAnnotation;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Example use in Turtle
 * :word1 a nif:Word ;
 *        nif:anchorOf "cat" ;
 *        rdfs:label "the cat" .
 */

public class LiteralValuedProperty implements NodeAnnotation {
  private final Property property;
  private final String literal;

  public LiteralValuedProperty(Property propert, String literal) {
    this.property = propert;
    this.literal = literal;
  }

  public String getLiteral() {
    return literal;
  }

  public Resource getResource() {
    return null;
  }

  public Property getProperty() {
    return property;
  }
}
