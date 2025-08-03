package com.ktc.text;

import java.util.UUID;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

public abstract class TripleComponent extends StructuredNode<TripleComponent, TripleStatement, TokenText> {
  public TripleComponent(Resource resource) {
    super(resource);
  }

  public TripleComponent(Resource resource, UUID tripleComponentId) {
    super(resource, tripleComponentId);
  }

  @Override
  public Property getPreviousProperty() {
    return null;
  }

  @Override
  public Property getNextProperty() {
    return null;
  }

  @Override
  public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
    return parentClass == TripleStatement.class;
  }
}
