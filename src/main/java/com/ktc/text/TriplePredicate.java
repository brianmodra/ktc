package com.ktc.text;

import java.util.UUID;

public class TriplePredicate extends TripleComponent {
  public TriplePredicate() {
    super(NodeBase.RDF_PREDICATE);
  }

  public TriplePredicate(UUID tripleComponentId) {
    super(NodeBase.RDF_PREDICATE, tripleComponentId);
  }
}
