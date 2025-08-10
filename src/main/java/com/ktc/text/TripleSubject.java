package com.ktc.text;

import java.util.UUID;

public class TripleSubject extends TripleComponent {
  private final String subjectGloss;

  public TripleSubject(String subjectGloss) {
    super(NodeBase.RDF_SUBJECT);
    this.subjectGloss = subjectGloss;
  }

  public TripleSubject(String subjectGloss, UUID tripleComponentId) {
    super(NodeBase.RDF_SUBJECT, tripleComponentId);
    this.subjectGloss = subjectGloss;
  }

  public String getSubjectGloss()
  {
    return subjectGloss;
  }

}
