package com.ktc.text;

import java.util.UUID;

public class TripleObject extends TripleComponent {
  private final String objectGloss;

  public TripleObject(String objectGloss, UUID tripleComponentId) {
    super(NodeBase.RDF_OBJECT, tripleComponentId);
    this.objectGloss = objectGloss;
  }

  public TripleObject(String objectGloss)
  {
    super(NodeBase.RDF_OBJECT);
    this.objectGloss = objectGloss;
  }

  public String getObjectGloss() {
    return objectGloss;
  }
}
