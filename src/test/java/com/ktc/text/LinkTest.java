package com.ktc.text;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LinkTest {
  @Test
  public void testLink() {
    TokenText node1 = TokenText.create("node1");
    TokenText node2 = TokenText.create("node2");
    Link link = new Link(Link.NEXT_WORD, node1, node2, "next");
    assertEquals(Link.NEXT_WORD, link.getProperty());
    assertEquals(node1, link.getSource());
    assertEquals(node2, link.getTarget());
    assertEquals("next", link.getKey());
    assertEquals("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#nextWord", link.getURI());
    assertEquals("nextWord", link.getLocalName());
    assertEquals("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#", link.getNameSpace());
  }
}
