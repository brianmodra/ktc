package com.ktc.text;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

  @Override
  public void unlink() {
    NodeBase parent = getParentNode();
    if (parent != null) {
      parent.unlink();
    }
    super.unlink();
  }

  @Override
  public List<TokenText> getChildren() {
    return allChildNodes().stream()
        .filter(node -> node instanceof TokenText)
        .map(node -> ((TokenText) node))
        .collect(Collectors.toList());
  }
}
