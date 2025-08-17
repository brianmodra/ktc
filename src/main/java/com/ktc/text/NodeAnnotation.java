package com.ktc.text;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public interface NodeAnnotation {
  Property getProperty();
  Resource getResource(); // can be null
  String getLiteral();
  String getDescription();
  boolean isNLPInfo();
}
