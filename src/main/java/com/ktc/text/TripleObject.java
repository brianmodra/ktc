package com.ktc.text;

import java.util.UUID;

public class TripleObject extends TripleComponent {
  public TripleObject() {
    super(NodeBase.RDF_OBJECT);
  }
  
  public TripleObject(UUID tripleComponentId) {
    super(NodeBase.RDF_OBJECT, tripleComponentId);
  }
}
