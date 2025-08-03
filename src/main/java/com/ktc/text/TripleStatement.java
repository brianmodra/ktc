package com.ktc.text;

import java.util.UUID;

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
}
