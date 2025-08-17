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

  public LiteralValuedProperty(Property property, String literal) {
    this.property = property;
    this.literal = literal;
  }

  @Override
  public String getLiteral() {
    return literal;
  }

  @Override
  public Resource getResource() {
    return null;
  }

  @Override
  public Property getProperty() {
    return property;
  }

  @Override
  public String getDescription() {
    return getLiteral();
  }

  @Override
  public boolean isNLPInfo() {
    return true;
  }
}
