package com.ktc.nlp;

import java.util.HashMap;
import java.util.Map;

import com.ktc.text.NodeAnnotation;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public enum FineGrainedNERTag implements NodeAnnotation {
  NONE("", "No named entity"),
  PERSON("PERSON", "Person"),
  ORGANIZATION("ORGANIZATION", "Organization"),
  LOCATION("LOCATION", "Location (unspecified type)"),
  CITY("CITY", "City"),
  STATE_OR_PROVINCE("STATE_OR_PROVINCE", "State or province"),
  COUNTRY("COUNTRY", "Country"),
  NATIONALITY("NATIONALITY", "Nationality"),
  TITLE("TITLE", "Honorific or title"),
  IDEOLOGY("IDEOLOGY", "Ideology"),
  RELIGION("RELIGION", "Religion"),
  CAUSE_OF_DEATH("CAUSE_OF_DEATH", "Cause of death"),
  CRIMINAL_CHARGE("CRIMINAL_CHARGE", "Criminal charge"),
  DATE("DATE", "Date"),
  TIME("TIME", "Time"),
  DURATION("DURATION", "Duration"),
  SET("SET", "Set (repeating time/event)"),
  MONEY("MONEY", "Monetary value"),
  PERCENT("PERCENT", "Percentage"),
  ORDINAL("ORDINAL", "Ordinal number"),
  NUMBER("NUMBER", "Number"),
  O("O", "other");

  private static final Map<String, FineGrainedNERTag> valueMap = new HashMap<>();
  private static final Property property;

  static {
    for (FineGrainedNERTag tag : values()) {
      valueMap.put(tag.getLiteral(), tag);
    }
    property = ResourceFactory.createProperty(
        "http://purl.org/olia/olia.owl#hasCategory"
    );
  }

  private final String value;
  private final String description;
  private final String literal;

  FineGrainedNERTag(String value, String description) {
    this.value = value;
    this.description = description;
    this.literal = value.substring(0,1).toUpperCase() + value.substring(1).toLowerCase();
  }

  public static FineGrainedNERTag create(String str) {
    FineGrainedNERTag tag = valueMap.get(str);
    if (tag == null) {
      throw new IllegalArgumentException(str + " is not a valid fine-grained NER tag");
    }
    return tag;
  }

  public String getLiteral() {
    return literal;
  }

  public String getDescription() {
    return description;
  }

  public Resource getResource() {
    return ResourceFactory.createResource(
        "http://purl.org/olia/olia.owl#" + getLiteral()
    );
  }

  public Property getProperty() {
    return property;
  }
}
