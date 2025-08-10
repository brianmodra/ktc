package com.ktc.text;

import org.apache.jena.rdf.model.Property;

public class ActorText extends StructuredNode<ActorText, DocumentText, ActorText>{

  public ActorText() {
    super(ACTOR_TYPE);
  }

  @Override
  public Property getNextProperty() {
    return null;
  }

  @Override
  public Property getPreviousProperty() {
    return null;
  }

  @Override
  public boolean parentCanBe(Class<? extends NodeBase> parentClass) {
    return false;
  }
}
