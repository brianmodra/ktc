package com.ktc.text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.jena.rdf.model.Property;

public class TripleStatement extends StructuredNode<TripleStatement, SentenceText, TripleComponent> {
  public TripleStatement() {
    super(NodeBase.POS);
  }

  public TripleStatement(UUID tripleId) {
    super(NodeBase.POS, tripleId);
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
    return parentClass == SentenceText.class;
  }

  @Override
  public List<TripleComponent> getChildren() {
    return allChildNodes().stream()
        .filter(node -> node instanceof TripleComponent)
        .map(node -> ((TripleComponent) node))
        .collect(Collectors.toList());
  }
}
