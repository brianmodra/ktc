package com.ktc.text;

import java.util.UUID;

public class TripleSubject extends TripleComponent {
  public TripleSubject() {
    super(NodeBase.RDF_SUBJECT);
  }

  public TripleSubject(UUID tripleComponentId) {
    super(NodeBase.RDF_SUBJECT, tripleComponentId);
  }
}
