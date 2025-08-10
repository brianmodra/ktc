package com.ktc.text;

import java.util.UUID;

public class TriplePredicate extends TripleComponent {
  private final String predicateGloss;

  public TriplePredicate(String predicateGloss, UUID tripleComponentId) {
    super(NodeBase.RDF_PREDICATE, tripleComponentId);
    this.predicateGloss = predicateGloss;
  }
  public TriplePredicate(String predicateGloss) {
    super(NodeBase.RDF_PREDICATE);
    this.predicateGloss = predicateGloss;
  }

  public String getPredicateGloss() {
    return predicateGloss;
  }
}
